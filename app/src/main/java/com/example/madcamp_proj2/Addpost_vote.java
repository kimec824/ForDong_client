package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Addpost_vote extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_vote);
        BoardItem newpost=new BoardItem();

        Button confirmbutton=(Button) findViewById(R.id.confirm_v);
        EditText title_input=(EditText) findViewById(R.id.Addtitle_v);
        EditText content_input=(EditText) findViewById(R.id.Addcontent_v);
        EditText candi1=(EditText) findViewById(R.id.candi1);
        EditText candi2=(EditText) findViewById(R.id.candi2);
        EditText candi3=(EditText) findViewById(R.id.candi3);
        EditText candi4=(EditText) findViewById(R.id.candi4);

        newpost.setcontent(content_input.getText().toString());
        newpost.settitle(title_input.getText().toString());
        newpost.settype(2);

        //추가 버튼 누르면 입력된 정보들(new post 객체) 서버에 post 해야됨
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 날짜, 시간을 boarditem의 date, time에 저장
                //현재 로그인 한 사람 id를 writer에 저장
                long now=System.currentTimeMillis();
                Date mDate=new Date(now);
                String temp=mDate.toString();
                newpost.settime(temp);
                //newpost.setwriter();
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
