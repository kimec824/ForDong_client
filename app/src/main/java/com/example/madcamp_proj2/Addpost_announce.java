package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import static com.example.madcamp_proj2.MainActivity.userID;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Addpost_announce extends AppCompatActivity implements AsyncTaskCallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_announce);
        String url="http://"+getString(R.string.ip)+":8080/board";
        //새로운 글 객체 생성
        BoardItem newpost=new BoardItem();
        //boarditem 정보: 제목, 내용, 글쓴이, 시간, 유형
        Button confirmbutton=(Button) findViewById(R.id.confirm_a);
        EditText title_input=(EditText) findViewById(R.id.Addtitle_a);
        EditText content_input=(EditText) findViewById(R.id.Addcontent_a);

        newpost.setcontent(content_input.getText().toString());
        newpost.settitle(title_input.getText().toString());
        newpost.settype(1);

        //추가 버튼 누르면 입력된 정보들(new post 객체) 서버에 post 해야됨
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 날짜, 시간을 boarditem의 date, time에 저장
                //현재 로그인 한 사람 id를 writer에 저장-->networktask jsonObject없이 불러서 그 객체 여기로 받아온 다음에?
                long now=System.currentTimeMillis();
                Date mDate=new Date(now);
                SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime=simpleDate.format(mDate);
                newpost.settime(getTime);
                //newpost를 json object로 바꿈
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("title", title_input.getText().toString());
                    System.out.println(newpost.gettitle());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("content",content_input.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("writer",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("type",1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("time",getTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gotoMain(jsonObject);
            }
        });
    }
    public void gotoMain (JSONObject jsonObject){

        NetworkTask networkTask = new NetworkTask("http://"+getString(R.string.ip)+":8080/board",null, jsonObject,this);
        networkTask.execute();
        finish();
        //Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }

    @Override
    public void method2(String s) {

    }
}
