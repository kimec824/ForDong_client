package com.example.madcamp_proj2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.madcamp_proj2.MainActivity.context_main;
import static com.example.madcamp_proj2.MainActivity.userID;

public class Viewpost_vote extends AppCompatActivity implements AsyncTaskCallback{
    String chosenTitle;
    TextView showtime, writer, title, content, res1, res2, res3, res4;
    int restemp1, restemp2, restemp3, restemp4;
    CheckBox check1, check2, check3, check4;
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
        setContentView(R.layout.viewpost_vote);
        showtime=(TextView) findViewById(R.id.time_a);
        writer=(TextView) findViewById(R.id.writer_a);
        title=(TextView) findViewById(R.id.viewtitle_a);
        content=(TextView) findViewById(R.id.viewcontent_a);
        check1=(CheckBox) findViewById(R.id.choose1);
        check2=(CheckBox) findViewById(R.id.choose2);
        check3=(CheckBox) findViewById(R.id.choose3);
        check4=(CheckBox) findViewById(R.id.choose4);
        res1=(TextView) findViewById(R.id.result1);
        res2=(TextView) findViewById(R.id.result2);
        res3=(TextView) findViewById(R.id.result3);
        res4=(TextView) findViewById(R.id.result4);
        Button addcomment=(Button) findViewById(R.id.addcomment_v);
        //정보를 get해오고 싶은데 chosenTitle을 title로 가지는 정보를 가져오고싶음.
        //일단 다가져오고... 파싱하면서 고르는걸로
        NetworkTask networkTask = new NetworkTask(url,null,null,this);
        networkTask.execute();

        //댓글 추가 관련 코드
        Button addpostbutton=(Button) findViewById(R.id.addcomment);
        addpostbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Addcomment.class);
                intent.putExtra("titleofpost", chosenTitle);
                startActivity(intent);
            }
        });

        //투표 선택 관련 코드
        //'투표 확정' 버튼 누르면 현재 check box 체크 상태 확인해서 4자리 숫자로 파싱-->서버에 post(댓글 하듯이)-->서버도 수정해야함
        Button confirmvoteButton=(Button) findViewById(R.id.confirm_vote);
        confirmvoteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String url="http://"+getString(R.string.ip)+":8080/board/result"+chosenTitle;
                String sUrl="";
                String eUrl="";
                sUrl=url.substring(0,   url.lastIndexOf("/")+1);
                eUrl=url.substring(url.lastIndexOf("/")+1,url.length());
                try {
                    eUrl= URLEncoder.encode(eUrl,"EUC-KR").replace("+","%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url=sUrl+eUrl;
                int voteResult=0;
                if(check1.isChecked())//1 선택했으면
                    voteResult=voteResult+1000;
                if(check2.isChecked())
                    voteResult=voteResult+100;
                if(check3.isChecked())
                    voteResult=voteResult+10;
                if(check4.isChecked())
                    voteResult=voteResult+1;
                //서버 vote result에 추가
                JSONObject jsonObject = new JSONObject();
                try {jsonObject.accumulate("ID",userID);} catch (JSONException e) {
                    e.printStackTrace();
                }
                try {jsonObject.accumulate("vote",voteResult);} catch (JSONException e) {
                    e.printStackTrace();
                }
                try {addVote(jsonObject,url);} catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //get 이후 실행 함수
    @Override
    public void method1(String s) {
        //vote 정보 parsing 해야함
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray boardArray = jsonObject.getJSONArray("Board");
            for (int i = 0; i < boardArray.length(); i++) {
                JSONObject getObject = boardArray.getJSONObject(i);
                if(getObject.getString("title").equals(chosenTitle)) {
                    //getObject는 선택된 글
                    title.setText(getObject.getString("title"));
                    content.setText(getObject.getString("content"));
                    writer.setText(getObject.getString("writer"));
                    showtime.setText(getObject.getString("time"));
                    check1.setText(getObject.getString("candi1"));
                    check2.setText(getObject.getString("candi2"));
                    check3.setText("hihihihihihih");
                    //check3.setText(getObject.getString("candi3"));
                    check4.setText(getObject.getString("candi4"));
                    restemp1=getObject.getInt("candi1result");
                    restemp2=getObject.getInt("candi2result");
                    restemp3=getObject.getInt("candi3result");
                    restemp4=getObject.getInt("candi4result");
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

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////
    }

    public void addVote (JSONObject jsonObject, String url) throws JSONException {

        NetworkTask networkTask = new NetworkTask(url,null, jsonObject,this);
        networkTask.execute();
        finish();
    }
    //post 이후 실행 함수
    @Override
    public void method2(String s) {
        //새로운 투표 결과 보여주기
        if(check1.isChecked()){res1.setText(restemp1+1);}
        else {res1.setText(restemp1);}
        if(check2.isChecked()){res2.setText(restemp2+1);}
        else {res2.setText(restemp2);}
        if(check3.isChecked()){res3.setText(restemp3+1);}
        else {res3.setText(restemp3);}
        if(check4.isChecked()){res4.setText(restemp4+1);}
        else {res4.setText(restemp4);}
    }
}
