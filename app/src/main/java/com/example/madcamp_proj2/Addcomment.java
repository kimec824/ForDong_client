package com.example.madcamp_proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Addcomment extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcomment);
        Button confirmButton = (Button) findViewById(R.id.addcomment_confirm);
        EditText commentcontent = (EditText) findViewById(R.id.comment_toadd);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버에 있는 글의 댓글 배열에 추가해야 함
                //그리고 다시 글 보는 화면으로 돌아가야 함
                //보고있던 글이 공지인 경우-->알기 위해서는 보고있던 글에 대한 정보를 받아와야 할듯!!!!
                Intent intent=new Intent(getApplicationContext(), Viewpost_announce.class);
                startActivity(intent);
                //보고있던 글이 투표인 경우-->아직 구현 안함
            }

        });
    }
}
