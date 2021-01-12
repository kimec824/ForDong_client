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

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.madcamp_proj2.MainActivity.userID;

public class Addpost_vote extends AppCompatActivity implements AsyncTaskCallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_vote);
        //BoardItem newpost=new BoardItem();

        Button confirmbutton=(Button) findViewById(R.id.confirm_v);
        EditText title_input=(EditText) findViewById(R.id.Addtitle_v);
        EditText content_input=(EditText) findViewById(R.id.Addcontent_v);
        EditText candi1=(EditText) findViewById(R.id.candi1);
        EditText candi2=(EditText) findViewById(R.id.candi2);
        EditText candi3=(EditText) findViewById(R.id.candi3);
        EditText candi4=(EditText) findViewById(R.id.candi4);

        //newpost.setcontent(content_input.getText().toString());
        //newpost.settitle(title_input.getText().toString());
        //newpost.settype(2);
        //newpost.setwriter(userID);
        //추가 버튼 누르면 입력된 정보들(new post 객체) 서버에 post 해야됨
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 날짜, 시간을 boarditem의 date, time에 저장
                //현재 로그인 한 사람 id를 writer에 저장
                long now=System.currentTimeMillis();
                Date mDate=new Date(now);
                SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime=simpleDate.format(mDate);
                //newpost.settime(getTime);
                //newpost를 json object로 바꿈
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("title", title_input.getText().toString());
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
                    jsonObject.accumulate("type",2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("time",getTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi1", candi1.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi2", candi2.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi3", candi3.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi4", candi4.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi1result",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi2result",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi3result",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("candi4result",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                gotoMain(jsonObject);
            }
        });
    }
    public void gotoMain (JSONObject jsonObject){

        NetworkTask networkTask = new NetworkTask("http://"+getString(R.string.ip)+":8080/board",null,"POST", jsonObject,this);
        networkTask.execute();
        System.out.println("end of add vote");
        finish();
    }

    @Override
    public void method2(String s) {

    }
}
