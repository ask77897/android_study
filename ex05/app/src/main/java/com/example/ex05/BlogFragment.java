package com.example.ex05;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BlogFragment extends Fragment {
    String query = "포켓몬";
    List<HashMap<String, Object>> array = new ArrayList<>();
    ListView list;
    BlogAdapter adapter;
    int page = 1;
    boolean is_end = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        new BlogThread().execute();
        list = view.findViewById(R.id.list);
        adapter = new BlogAdapter();
        list.setAdapter(adapter);

        EditText editQuery = view.findViewById(R.id.query);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editQuery.getText().toString();
                array = new ArrayList<>();
                new BlogThread().execute();
            }
        });

        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_end){
                    Toast.makeText(getActivity(), "마지막페이지입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    page+=1;
                    new BlogThread().execute();
                }
            }
        });
        return view;
    }

    class BlogThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v2/search/web?query=" + query + "&page=" + page;
            String result = KakaoAPI.connect(url);
            //System.out.println("............" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            blogParser(s);
            //System.out.println("....Data : " + array.size());
            adapter.notifyDataSetChanged();
        }
    }
    public void blogParser(String result) {
        try{
            JSONObject meta = new JSONObject(result).getJSONObject("meta");
            is_end = meta.getBoolean("is_end");
            JSONArray jarr = new JSONObject(result).getJSONArray("documents");
            for(int i = 0; i<jarr.length(); i++){
                JSONObject jobj = jarr.getJSONObject(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", jobj.getString("title"));
                map.put("url", jobj.getString("url"));
                map.put("contents", jobj.getString("contents"));

                array.add(map);
            }
        }catch (Exception e){
            System.out.println("파싱 : " + e.toString());
        }
    }
    class BlogAdapter extends BaseAdapter{

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
            TextView title = convertView.findViewById(R.id.title);
            String strTitle = map.get("title").toString();
            title.setText(Html.fromHtml(strTitle));
            String ttl = String.valueOf(Html.fromHtml(strTitle));

            TextView contents = convertView.findViewById(R.id.contents);
            String strContents = map.get("contents").toString();
            contents.setText(Html.fromHtml(strContents));

            String url = map.get("url").toString();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title", ttl);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}