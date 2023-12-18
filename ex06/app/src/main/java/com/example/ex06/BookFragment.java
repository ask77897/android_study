package com.example.ex06;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BookFragment extends Fragment {
    String query = "그림";
    int start = 1;
    int page = 1;
    int last_page = 1;
    ArrayList<HashMap<String, Object>> array = new ArrayList<>();
    BookAdapter adapter = new BookAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        new BookThread().execute();
        RecyclerView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edQuery = view.findViewById(R.id.query);
                query = edQuery.getText().toString();
                array = new ArrayList<>();
                new BookThread().execute();
            }
        });
        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page < last_page){
                    page++;
                    start = (page - 1) * 10 +1;
                    new BookThread().execute();
                }else {
                    Toast.makeText(getActivity(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    class BookThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://openapi.naver.com/v1/search/book.json";
            String result = NaverAPI.con(url, query, start);
            //System.out.println("result : " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            BookParser(s);
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            //System.out.println("Book Data : " + array.size());
        }
    }
    public void BookParser(String result){
        try{
            JSONObject object = new JSONObject(result);
            int total=Integer.parseInt(object.getString("total"));
            last_page = total/10;
            if(total/10. != last_page){
                last_page=last_page + 1;
            }
            JSONArray jarr = object.getJSONArray("items");
            for (int i = 0; i < jarr.length(); i++){
                JSONObject jobj = jarr.getJSONObject(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", jobj.getString("title"));
                map.put("price", jobj.getString("discount"));
                map.put("image", jobj.getString("image"));
                map.put("link", jobj.getString("link"));
                array.add(map);
            }
        }catch(Exception e){
            System.out.println("parsing error : " + e.toString());
        }
    }
    class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

        @NonNull
        @Override
        public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_shop, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
            HashMap<String, Object> map = array.get(position);
            holder.title.setText(map.get("title").toString());
            int intPrice = Integer.parseInt(map.get("price").toString());
            DecimalFormat df = new DecimalFormat("#,###원");
            holder.price.setText(df.format(intPrice));
            String strImage = map.get("image").toString();
            if(strImage.equals("")){
                holder.image.setImageResource(R.drawable.baseline_book_24);
            }else {
                Picasso.with(getActivity()).load(strImage).into(holder.image);
            }
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("link", map.get("link").toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, price;
            ImageView image;
            CardView item;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                price = itemView.findViewById(R.id.price);
                image = itemView.findViewById(R.id.image);
                item = itemView.findViewById(R.id.item);
            }
        }
    }
}