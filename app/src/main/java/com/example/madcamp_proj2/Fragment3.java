package com.example.madcamp_proj2;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    private ListView listview;
    private ListViewAdapter_forBoard adapter;
    ArrayList<BoardItem> boardItems = new ArrayList<BoardItem>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment3, container, false);
        boardItems.clear();
        listview = (ListView) view.findViewById(R.id.boardlist);
        listview.setAdapter(adapter);
        //adapter.clearItem();
        String url = "http://192.249.18.235:8080/board";

        String method = "POST";
        Fragment3.NetworkTask networkPostTask = new Fragment3.NetworkTask(url, null, method);
        networkPostTask.execute();

        method = "GET";
        Fragment3.NetworkTask networkTask = new Fragment3.NetworkTask(url, null, method);
        networkTask.execute();

        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    /* Class for Network */
    ///////////////////////////////////////////////////////////////////////////////////
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        private String method;

        public NetworkTask(String url, ContentValues values, String method) {

            this.url = url;
            this.values = values;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            if (method == "GET") {
                result = requestHttpURLConnection.request_get(url, values); // 해당 URL로 부터 결과물을 얻어온다.
                System.out.println("enter 1");
            } else {
                result = requestHttpURLConnection.request_post(url, values); // 해당 URL로 POST 보내기.
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println(s);
            if (method == "GET") {
                try {
                    //Json parsing
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray boardsArray = jsonObject.getJSONArray("myCollection");


                    for (int i = 0; i < boardsArray.length(); i++) {
                        JSONObject getObject = boardsArray.getJSONObject(i);
                        BoardItem boardItem = new BoardItem();

                        boardItem.settitle(getObject.getString("title"));
                        boardItem.setcontent(getObject.getString("content"));
                        //contact.setMail(movieObject.getString("email"));
                        //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                        boardItems.add(boardItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("CHECK : " + contactItems.size());
                for (int i = 0; i < boardItems.size(); i++) {
                    //TODO: 글 게시자 프로필사진 띄우기
                    Bitmap sampleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person);
                    BoardItem bi = boardItems.get(i);
                    adapter.addItem(sampleBitmap, bi.gettitle(), bi.getcontent());
                }
                listview.setAdapter(adapter);
            } else if (method == "POST") {
                if (s == "fail") {
                    Log.e("fail", "fail....");
                } else {
                    Log.e("success", s);
                }
            }
        }
    }
}
    /////////////////////////////////////////////////////////////////////////////////////
/*
    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getContext().getAssets().open("contacts.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }
    //이걸 db로 받으면 됨.
    public void getContactList(){
        //contacts item에 해당 파일들이 json parsing 해서들어오면되는거.
    }
}
*/