package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyphotosActivity extends AppCompatActivity implements AsyncTaskCallback{

    public ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
    public RecyclerView recyclerView2;
    public RecyclerViewAdapter recyclerViewAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myphotos);

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
        recyclerViewAdapter2 = new RecyclerViewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager);

        String ID = getIntent().getStringExtra("userID");
        String url = "http://"+getString(R.string.ip)+":8080/photos/profile/"+ID;
        String method = "GET";

        //AsyncTask를 통해 HTTPURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(url, null, method ,null, this);
        networkTask.execute();


    }

    @Override
    public void method1(String s) {
        try{
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);
            JSONArray photosArray = jsonObject.getJSONArray("Photos");

            for(int i=0; i<photosArray.length(); i++)
            {
                JSONObject photoObject = photosArray.getJSONObject(i);
                FeedItem feed = new FeedItem();

                feed.setId(photoObject.getString("ID"));
                feed.setImagePath(photoObject.getString("file_path"));
                feed.setPhotoContext(photoObject.getString("context"));

                recyclerViewAdapter2.addItem(null, feed.getId(), feed.getPhotoConText(), feed.getImagePath());
                feedItems.add(feed);
            }

            recyclerView2.setAdapter(recyclerViewAdapter2);
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}