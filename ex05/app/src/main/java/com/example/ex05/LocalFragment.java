package com.example.ex05;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LocalFragment extends Fragment {
    String query = "버거킹";
    int page = 1;
    List<HashMap<String, Object>> array = new ArrayList<>();
    LocalAdapter adapter = new LocalAdapter();
    boolean is_end = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        new LocalThread().execute();
        ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);

        EditText editQuery = view.findViewById(R.id.query);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editQuery.getText().toString();
                page = 1;
                array = new ArrayList<>();
                new LocalThread().execute();
            }
        });
        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_end){
                    Toast.makeText(getActivity(), "마지막페이지입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    page += 1;
                    new LocalThread().execute();
                }
            }
        });
        return view;
    }

    class LocalThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query + "&page=" + page;
            String result = KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //System.out.println("지역검색결과 : " + s);
            super.onPostExecute(s);
            LocalParser(s);
            adapter.notifyDataSetChanged();
            //System.out.println("Data : " + array.size());
        }
    }
    public void LocalParser(String result){
        try{
            JSONObject meta = new JSONObject(result).getJSONObject("meta");
            is_end = meta.getBoolean("is_end");
            JSONArray jarr = new JSONObject(result).getJSONArray("documents");
            for(int i = 0; i < jarr.length(); i++){
                JSONObject jobj = jarr.getJSONObject(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", jobj.getString("place_name"));
                map.put("address", jobj.getString(("address_name")));
                map.put("phone", jobj.getString("phone"));
                map.put("x", jobj.getDouble("x"));
                map.put("y", jobj.getDouble("y"));
                array.add(map);
            }
        }catch (Exception e){
            System.out.println("파싱 : " + e.toString());
        }
    }
    class LocalAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_blog, parent, false);
            HashMap<String, Object> map = array.get(position);
            TextView name = convertView.findViewById(R.id.title);
            String strName = map.get("name").toString();
            name.setText(strName);
            TextView address = convertView.findViewById(R.id.contents);
            String strPhone = map.get("phone").toString();
            String strAddress = map.get("address").toString();
            address.setText(strAddress + "\n" + strPhone);
            String x = map.get("x").toString();
            String y = map.get("y").toString();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("name", strName);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}