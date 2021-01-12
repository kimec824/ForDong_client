package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
    public static GridView gridView;
    public static PhotoGridAdapter photoGridAdapter;

    public static ArrayList<String> groups; //갤러리 폴더 잡는거.

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
        gridView = (GridView) view.findViewById(R.id.gridView1);
        photoGridAdapter = new PhotoGridAdapter();

        //recyclerView adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler1);
        recyclerViewAdapter = new RecyclerViewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        //gridview 에 데이터 넣기.
        add_item_to_Adapter();



        ImageButton button1 = (ImageButton) view.findViewById(R.id.uploadbutton);
        ImageButton button2 = (ImageButton) view.findViewById(R.id.backbutton);


        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),ImageActivity.class);
                startActivity(intent);
                // TODO : click event
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                recyclerView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                button2.setVisibility(View.GONE);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //해당 폴더의 리사이클러뷰를 보여줘야함.
                String folder_name = groups.get(position);

                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);

                recyclerViewAdapter.clearItem();
                 for(int i = 0 ; i<feedItems.size(); i++) {
                     FeedItem ci = feedItems.get(i);
                     System.out.println("now is in "+ ci.getPhoto_group());
                     if(ci.getPhoto_group().equals(folder_name)){
                         System.out.println("input in "+ folder_name);
                         recyclerViewAdapter.addItem(ci.getId(), ci.getPhotoConText(), ci.getImagePath(), ci.getPhoto_group() );
                     }
                 }
                 //gridViewAdapter.notifyDataSetChanged();
                 recyclerView.setAdapter(recyclerViewAdapter);

                //hidden layout(button 3개) 를 숨기는 걸로 default.



            }
        });

        return view;
    }

    public void add_item_to_Adapter() {

        //여기서 item 을 넣어줘야함
        //그말은 여기서 get api 를 이용해서 server의 이미지들을 가져와야한다는 뜻.
        //1.requestHTTpconnection을 이용해서 서버에 get /photos를 할 순 있음.
        //그럼 이거 부터해서 photo들의 path를 받아보자.
        // 또, gridview 를 위해서 group_name 도 받아와야함.


        String url = "http://"+getString(R.string.ip)+":8080/photos";
        String method = "GET";

        //AsyncTask를 통해 HTTPURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(url, null, method ,null, this );
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
                System.out.println(feed.getId());
                feed.setImagePath(photoObject.getString("file_path"));
                feed.setPhotoContext(photoObject.getString("context"));
                feed.setPhoto_group(photoObject.getString("photo_group"));

                //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));
                feedItems.add(feed);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //일단 recyclerview에 넣기 전에 gridview에서 분기를 해야해.
        // 여기서는 gridview 에 넣자 그림을.(대표그림?)
        System.out.println("CHECK FeedItems : " + feedItems.size());
        groups = new ArrayList<String>();
        for(int i = 0 ; i<feedItems.size(); i++){
            FeedItem ci = feedItems.get(i);
            if(!groups.contains(ci.getPhoto_group())){
                photoGridAdapter.addItem( ci.getImagePath(), ci.getPhoto_group());
                groups.add(ci.getPhoto_group());
            }
        }
        gridView.setAdapter(photoGridAdapter);

        /**
        for(int i = 0 ; i<feedItems.size(); i++){
            FeedItem ci = feedItems.get(i);
            recyclerViewAdapter.addItem(ci.getIcon(), ci.getId(), ci.getPhotoConText(), ci.getImagePath());
        }
        //gridViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);
         */

    }
}