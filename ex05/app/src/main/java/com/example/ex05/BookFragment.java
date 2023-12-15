package com.example.ex05;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.channels.AsynchronousChannelGroup;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookFragment extends Fragment {
    String query = "피카소";
    int page = 1;
    List<HashMap<String, Object>> array = new ArrayList<>();
    BookAdapter adapter = new BookAdapter();
    Boolean is_end = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        new BookThread().execute();
        ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);

        EditText editQuery = view.findViewById(R.id.query);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editQuery.getText().toString();
                page = 1;
                array = new ArrayList<>();
                new BookThread().execute();
            }
        });
        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_end){
                    Toast.makeText(getActivity(), "마지막페이지입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    page += 1;
                    new BookThread().execute();
                }
            }
        });
        return view;
    }
    class BookThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v3/search/book?target=title&query=" + query + "&page=" + page;
            String result = KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            bookParser(s);
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            //System.out.println("Data : " + array.size());
            //System.out.println("도서결과 : " + s);
        }
    }
    public void bookParser(String result){
        try {
            JSONObject meta = new JSONObject(result).getJSONObject("meta");
            is_end = meta.getBoolean("is_end");
            JSONArray jarr = new JSONObject(result).getJSONArray("documents");
            for(int i = 0; i < jarr.length(); i++){
                JSONObject jobj = jarr.getJSONObject(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", jobj.getString("title"));
                map.put("price", jobj.getString("price"));
                map.put("image", jobj.getString("thumbnail"));
                map.put("contents", jobj.getString("contents"));
                map.put("authors", jobj.getString("authors"));
                array.add(map);
            }
        }catch(Exception e){
            System.out.println("북파싱 : " + e.toString());
        }
    }
    class BookAdapter extends BaseAdapter{

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
            convertView = getLayoutInflater().inflate(R.layout.item_book, parent, false);
            HashMap<String, Object> map = array.get(position);

            ImageView image = convertView.findViewById(R.id.image);
            String strImage = map.get("image").toString();
            if(strImage.equals("")){
                image.setImageResource(R.drawable.baseline_book_24_3);
            }else {
                Picasso.with(getActivity())
                        .load(strImage)
                        .into(image);
            }

            TextView title = convertView.findViewById(R.id.title);
            String strTitle = map.get("title").toString();
            title.setText(strTitle);

            TextView price = convertView.findViewById(R.id.price);
            String strPrice = map.get("price").toString();
            int intPrice = Integer.parseInt(strPrice);
            DecimalFormat df = new DecimalFormat("#,###원");
            price.setText(df.format(intPrice));

            TextView authors = convertView.findViewById(R.id.authors);
            String strAuthors = map.get("authors").toString();
            authors.setText(strAuthors);

            String strContents = map.get("contents").toString();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View layout = getLayoutInflater().inflate(R.layout.item_contents, parent, false);
                    ImageView image = layout.findViewById(R.id.image);
                    if(strImage.equals("")){
                        image.setImageResource(R.drawable.baseline_book_24_3);
                    }else {
                        Picasso.with(getActivity())
                                .load(strImage)
                                .into(image);
                    }
                    TextView contents = layout.findViewById(R.id.contents);
                    contents.setText(strContents);
                    new AlertDialog.Builder(getActivity())
                            .setTitle(strTitle)
                            .setView(layout)
                            .setPositiveButton("확인", null)
                            .show();
                }
            });
            return convertView;
        }
    }
}