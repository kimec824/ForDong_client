package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Viewpost_announce extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpost_announce);
        TextView showtime=(TextView) findViewById(R.id.time_a);
        TextView writer=(TextView) findViewById(R.id.writer_a);
        TextView title=(TextView) findViewById(R.id.viewtitle_a);
        TextView content=(TextView) findViewById(R.id.viewcontent_a);
        Button addcomment=(Button) findViewById(R.id.addcomment);

        //댓글 추가
        Button addpostbutton=(Button) findViewById(R.id.addcomment);
        addpostbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Addcomment.class);
                startActivity(intent);
            }
        });
    }
    }
    //서버에서 정보 받아와서 textview에 넣어주기


