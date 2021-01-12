package com.example.madcamp_proj2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.madcamp_proj2.Fragment3.boardItems;
import static com.example.madcamp_proj2.MainActivity.context_main;
import static com.example.madcamp_proj2.MainActivity.userID;
import static com.example.madcamp_proj2.Viewpost_announce.CommentItems;
import static com.example.madcamp_proj2.Viewpost_announce.post_type;
import static com.example.madcamp_proj2.Viewpost_vote.CommentItems1;
import static com.example.madcamp_proj2.Viewpost_vote.adapter1;
import static com.example.madcamp_proj2.Viewpost_announce.adapter;


public class Addcomment extends AppCompatActivity implements AsyncTaskCallback {
    String titleofpost;
    //public static ListViewAdapter_forComment adapter = new ListViewAdapter_forComment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            titleofpost=extras.getString("titleofpost");
        }
        //String url="http://"+getString(R.string.ip)+":8080/board"+titleofpost;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcomment);
        Button confirmButton = (Button) findViewById(R.id.addcomment_confirm);
        EditText commentcontent = (EditText) findViewById(R.id.comment_toadd);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버에 있는 글의 댓글 배열에 추가해야 함
                JSONObject jsonObject = new JSONObject();
                try {
                    System.out.println("send post name : " + titleofpost);
                    String str = titleofpost;
                    jsonObject.accumulate("title",str);
                    jsonObject.accumulate("writer",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.accumulate("content",commentcontent.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                long now=System.currentTimeMillis();
                Date mDate=new Date(now);
                SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime=simpleDate.format(mDate);
                try {
                    jsonObject.accumulate("time",getTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                ////Debug////////
                try {
                    System.out.println(jsonObject.getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                ListViewItem_Comment new_comment = new ListViewItem_Comment();
                new_comment.setWriter(userID);
                new_comment.setDesc(commentcontent.getText().toString());


                if(post_type == 1){
                    CommentItems.add(new_comment);
                    adapter.addItem(commentcontent.getText().toString(),userID);
                    adapter.notifyDataSetChanged();
                }
                else{
                    CommentItems1.add(new_comment);
                    adapter1.addItem(commentcontent.getText().toString(),userID);
                    adapter1.notifyDataSetChanged();
                }


                String url="http://"+getString(R.string.ip)+":8080/board/comment";
                try {
                    gotoPost(jsonObject,url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void gotoPost (JSONObject jsonObject, String url) throws JSONException {
        NetworkTask networkTask = new NetworkTask(url,null, "POST", jsonObject,this);
        //Bitmap sampleBitmap = BitmapFactory.decodeResource(context_main.getResources(), R.drawable.person);
        //adapter.addItem(jsonObject.getString("content"), jsonObject.getString("writer"));
        //listview.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        networkTask.execute();
    }
    @Override
    public void method2(String s) {

        finish();
    }
}
