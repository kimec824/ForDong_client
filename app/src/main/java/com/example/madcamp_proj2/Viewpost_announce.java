package com.example.madcamp_proj2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.madcamp_proj2.MainActivity.context_main;

public class Viewpost_announce extends AppCompatActivity implements AsyncTaskCallback {
    String chosenTitle;
    TextView showtime, writer, title, content;
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
            BoardItem post = new BoardItem();
            for (int i = 0; i < boardArray.length(); i++) {
                JSONObject getObject = boardArray.getJSONObject(i);
                if(getObject.getString("title")==chosenTitle) {
                    //getObject는 선택된 글
                    title.setText(getObject.getString("title"));
                    content.setText(getObject.getString("content"));
                    writer.setText(getObject.getString("writer"));
                    //post.settype(getObject.getInt("type"));-->어차피 announce layout이므로 여기서는 아무 의미 없음
                    showtime.setText(getObject.getString("time"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    //서버에서 정보 받아와서 textview에 넣어주기


