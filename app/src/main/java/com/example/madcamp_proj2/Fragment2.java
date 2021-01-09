package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment implements AsyncTaskCallback{

    public static ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter recyclerViewAdapter;

    public Fragment2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        //GridView adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler1);
        recyclerViewAdapter = new RecyclerViewAdapter();

        recyclerViewAdapter = add_item_to_recyclerViewAdapter(recyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        Button button1 = (Button) view.findViewById(R.id.button);

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),ImageActivity.class);
                startActivity(intent);
                // TODO : click event
            }
        });


        return view;
    }

    public RecyclerViewAdapter add_item_to_recyclerViewAdapter(RecyclerViewAdapter myadapter) {

        //여기서 item 을 넣어줘야함
        //그말은 여기서 get api 를 이용해서 server의 이미지들을 가져와야한다는 뜻.
        //1.requestHTTpconnection을 이용해서 서버에 get /photos를 할 순 있음.
        //그럼 이거 부터해서 photo들의 path를 받아보자.


        String url = "http://192.249.18.232:8080/photos";
        //String json = getJsonString();
        //System.out.println(json);
        //AsyncTask를 통해 HTTPURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(url, null, null, this);
        networkTask.execute();


        //jsonParsing(json); // arraylist<childfragmentitem> 에 들어가게 됨.
/**
        for (int i = 0; i < dep_icon.length; i++) {
            GridViewItem gk = kaist.get(i);
            myadapter.addItem(gk.getIcon(), gk.getStr()); //건설환경공학과
        }
*/
        //adapter.clearItem();
        //listview = (ListView) view.findViewById(R.id.listview1);
        return myadapter;
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

                feed.setName(photoObject.getString("name"));
                feed.setImagePath(photoObject.getString("file_path"));
                feed.setPhotoContext(photoObject.getString("context"));
                //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                feedItems.add(feed);
                //String path_url = "http://192.249.18.232:8080/photos/uploads/" + feed.getImagePath();
                //ImageLoadTask task = new ImageLoadTask(path_url , feed);
                //task.execute();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("CHECK FeedItems : " + feedItems.size());
        for(int i = 0 ; i<feedItems.size(); i++){
            FeedItem ci = feedItems.get(i);
            recyclerViewAdapter.addItem(ci.getIcon(), ci.getName(), ci.getPhotoConText(), ci.getImagePath());
        }
        //gridViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}