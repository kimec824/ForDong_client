package com.example.madcamp_proj2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.madcamp_proj2.MainActivity.context_main;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment implements AsyncTaskCallback {

    private ListView listview;
    public static ListViewAdapter_forBoard adapter = new ListViewAdapter_forBoard();
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
        adapter.clearItem();
        String url = "http://192.249.18.250:8080/board";

        //게시글 추가 버튼 클릭 이벤트
        Button addpostbutton = (Button) view.findViewById(R.id.addpost);
        addpostbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //글의 유형 선택하는 창 띄워주기
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("어떤 글을 작성하시겠습니까?");

                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        String[] items = getResources().getStringArray(R.array.LAN);
                        //Toast.makeText(getApplicationContext(),items[pos],Toast.LENGTH_LONG).show();
                        if (pos == 0) {
                            Intent intent = new Intent(getActivity(), Addpost_announce.class);
                            startActivity(intent);
                        } else if (pos == 1) {
                            Intent intent = new Intent(getActivity(), Addpost_vote.class);
                            startActivity(intent);
                        } else if (pos == 2) {
                            Intent intent = new Intent(getActivity(), Addpost_announce.class);
                            startActivity(intent);

                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //activity에서 선택한 것에 따라서 해당하는 게시글 추가 activity로 넘어간다.
                //게시글 추가할 때 type도 정해줘야한다!
            }
        });

        //게시글 전체보기 버튼 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //activity에서 선택한 position에 따라서 해당하는 게시글 view activity로 넘어간다
            }
        });


        NetworkTask networkTask = new NetworkTask(url, null, null, this);
        networkTask.execute();
        /*
        String method = "POST";
        Fragment3.NetworkTask networkPostTask = new Fragment3.NetworkTask(url, null, method);
        networkPostTask.execute();

        method = "GET";
        Fragment3.NetworkTask networkTask = new Fragment3.NetworkTask(url, null, method);
        networkTask.execute();
*/
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void method1(String s) {
        try {
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);

            JSONArray boardArray = jsonObject.getJSONArray("Board");

            for (int i = 0; i < boardArray.length(); i++) {
                JSONObject getObject = boardArray.getJSONObject(i);
                BoardItem post = new BoardItem();

                post.settitle(getObject.getString("title"));
                post.setwriter(getObject.getString("writer"));

                boardItems.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.clearItem();
        for (int i = 0; i < boardItems.size(); i++) {
            Bitmap sampleBitmap = BitmapFactory.decodeResource(context_main.getResources(), R.drawable.person);
            BoardItem bi = boardItems.get(i);
            adapter.addItem(sampleBitmap, bi.gettitle(), bi.getwriter());
        }
        listview.setAdapter(adapter);
    }
}