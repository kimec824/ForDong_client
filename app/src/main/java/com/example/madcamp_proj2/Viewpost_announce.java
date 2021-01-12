package com.example.madcamp_proj2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;

import static com.example.madcamp_proj2.MainActivity.context_main;

public class Viewpost_announce extends AppCompatActivity implements AsyncTaskCallback {
    String chosenTitle;
    TextView showtime, writer, title, content;
    private ListView listview;
    public static ListViewAdapter_forComment adapter = new ListViewAdapter_forComment();
    ArrayList<ListViewItem_Comment> CommentItems = new ArrayList<ListViewItem_Comment>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String url="http://"+getString(R.string.ip)+":8080/board";
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            chosenTitle=extras.getString("title");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpost_announce);
         showtime=(TextView) findViewById(R.id.time_a);
         writer=(TextView) findViewById(R.id.writer_a);
         title=(TextView) findViewById(R.id.viewtitle_a);
         content=(TextView) findViewById(R.id.viewcontent_a);
         listview=(ListView) findViewById(R.id.commentlist_a);
        Button addcomment=(Button) findViewById(R.id.addcomment);
        //정보를 get해오고 싶은데 chosenTitle을 title로 가지는 정보를 가져오고싶음.
        //일단 다가져오고... 파싱하면서 고르는걸로
        NetworkTask networkTask = new NetworkTask(url,null,null,this);
        networkTask.execute();

        //댓글 추가
        Button addpostbutton=(Button) findViewById(R.id.addcomment);
        addpostbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Addcomment.class);
                intent.putExtra("titleofpost", chosenTitle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void method1(String s) {
        //post 정보 parsing 해야함
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray boardArray = jsonObject.getJSONArray("Board");
            //BoardItem post = new BoardItem();
            for (int i = 0; i < boardArray.length(); i++) {
                JSONObject getObject = boardArray.getJSONObject(i);
                if(getObject.getString("title").equals(chosenTitle)) {
                    //getObject는 선택된 글
                    title.setText(getObject.getString("title"));
                    content.setText(getObject.getString("content"));
                    writer.setText(getObject.getString("writer"));
                    showtime.setText(getObject.getString("time"));


//////////////////////////////////댓글 출력 관련 코드///////////////////////////
                    CommentItems.clear();
                    JSONArray commentArray = getObject.getJSONArray("comment");
                    System.out.println(commentArray.length());
                    for(int j=0;j<commentArray.length();j++){
                        //댓글 하나하나 new comment object에 저장해서 listview에 저장
                        JSONObject getObject1 = commentArray.getJSONObject(j);
                        ListViewItem_Comment com = new ListViewItem_Comment();

                        com.setTime(getObject1.getString("time"));
                        com.setWriter(getObject1.getString("writer"));
                        com.setDesc(getObject1.getString("content"));

                        CommentItems.add(com);
                    }
                    adapter.clearItem();
                    for (int k = 0; k < CommentItems.size(); k++) {
                        Bitmap sampleBitmap = BitmapFactory.decodeResource(context_main.getResources(), R.drawable.person);
                        ListViewItem_Comment comi = CommentItems.get(k);
                        System.out.println(comi.getDesc());
                        adapter.addItem(sampleBitmap, comi.getDesc(), comi.getWriter(), comi.getTime());
                    }
                    listview.setAdapter(adapter);
////////////////////////////////////////////////////////////////////////////////////////
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    //서버에서 정보 받아와서 textview에 넣어주기


