package com.example.madcamp_proj2;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.madcamp_proj2.MainActivity.userID;
import static com.example.madcamp_proj2.MainActivity.context_main;
import static com.example.madcamp_proj2.Fragment1.adapter;
import static com.example.madcamp_proj2.Fragment1.listview;

import android.app.Activity;

//import static com.example.helloworld.MainActivity.contactItems;
//import static com.example.helloworld.Page1Fragment.adapter;
//import static com.example.helloworld.Page1Fragment.listview;
//import static com.example.helloworld.Page1Fragment.newcontact;

//import static com.example.helloworld.MainActivity.contactList;

public class ListViewAdapter extends BaseAdapter implements AsyncTaskCallback{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<ContactItem> contactItemList = new ArrayList<ContactItem>() ;

    // listview_item 내 view들
    public ImageView iconImageView;
    public TextView titleTextView, descTextView;
    public ContactItem contactItem;
    AsyncTaskCallback callback;



    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return contactItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        Context context = parent.getContext();
        callback = this;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        //hidden layout(button 3개) 를 숨기는 걸로 default.
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.hidden);
        layout.setVisibility(View.GONE);

        //View 내 데이터를 설정해주기.
        setViews(position , convertView);

        //button 설정 Call(dial로 이동), edit(dialog 띄우기), page(activity전환)
        Button dialbtn = (Button) convertView.findViewById(R.id.dialbtn);
        Button editbtn = (Button) convertView.findViewById(R.id.editbtn);
        Button mypagebtn = (Button) convertView.findViewById(R.id.mypagebtn);


        // item click
        convertView.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view){
                ListViewItemClickevent(layout);
            }
        });

        //dial button click
        dialbtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("dial","click");
                ContactItem now = contactItemList.get(pos);
                Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ now.getUser_phNumber()));
                context.startActivity(tt);
            }
        });

        //edit button click
        editbtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println( MainActivity.userID );

                ContactItem con = contactItemList.get(position);

                //주인하고 맞는지 체크
                if(con.getUser_name() == userID){
                    final LinearLayout linear = (LinearLayout) View.inflate(context, R.layout.contactdialog, null);
                    AlertDialog.Builder adb = new AlertDialog.Builder(context, R.style.MyDialogTheme);

                    EditText edt = linear.findViewById(R.id.et1);
                    adb.setView(linear);
                    edt.setText(con.getUser_name());

                    EditText edt2 = linear.findViewById(R.id.et2);
                    adb.setView(linear);
                    edt2.setText(con.getUser_phNumber());

                    EditText edt3 = linear.findViewById(R.id.et3);
                    adb.setView(linear);
                    edt3.setText(con.getMail());

                    EditText edt4 = linear.findViewById(R.id.et4);
                    adb.setView(linear);
                    edt4.setText(con.getAddress());

                    //ok는 수정했다는것.
                    adb.setTitle("Edit Contact")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String name = edt.getText().toString();
                                    String number = edt2.getText().toString();
                                    String mail = edt3.getText().toString();
                                    String add = edt4.getText().toString();



                                    //
                                    //DATABASE에 넘겨주는 작업이 필요함.
                                    // put
                                    String url = "http://"+context.getString(R.string.ip)+":8080/contacts";
                                    String method = "PUT";

                                    //build jsonObject
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.accumulate("name",con.getUser_name());
                                        jsonObject.accumulate("phone",con.getUser_phNumber());
                                        jsonObject.accumulate("email",con.getMail());
                                        jsonObject.accumulate("changename",name);
                                        jsonObject.accumulate("changephone",number);
                                        jsonObject.accumulate("changeemail",mail);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    ContactItem temp = new ContactItem();
                                    Bitmap sampleBitmap = BitmapFactory.decodeResource( context_main.getResources(), R.drawable.person);
                                    temp.setUser_profile(sampleBitmap);
                                    temp.setUser_name(name);
                                    temp.setUser_phNumber(number);
                                    temp.setMail(mail);
                                    temp.setAddress(add);
                                    contactItemList.set(position, temp);

                                    adapter.notifyDataSetChanged();
                                    listview.setAdapter(adapter);

                                    System.out.println("checkpoint");
                                    NetworkTask networkTask = new NetworkTask(url, null, method, jsonObject, callback  );
                                    networkTask.execute();



                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog finalDialog = adb.create();
                    finalDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            finalDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6E6557"));
                            finalDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#6E6557"));
                        }
                    });
                    finalDialog.show();
                }
                else{
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setMessage("Permission denied.");
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();

                    //Toast.makeText( MainActivity.context_main ,"permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //mypage button click
        mypagebtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("mypage","click");
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return contactItemList.get(position) ;
    }

    //view 내 데이터 설정.
    public void setViews(int position, View convertView){



        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        contactItem = contactItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageBitmap(contactItem.getUser_profile());
        titleTextView.setText(contactItem.getUser_name());
        descTextView.setText(contactItem.getUser_phNumber());



        //dialbtn.setOnClickListener(this);
        //editbtn.setOnClickListener(this);
        //pagebtn.setOnClickListener(this);

    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(ContactItem contactItem) {
        contactItemList.add(contactItem);
    }

    public void clearItem() {
        contactItemList.clear();
    }

    //item 클릭 시 hide/show 변환
    public void ListViewItemClickevent(LinearLayout layout){
        if(layout.isShown())
            layout.setVisibility(View.GONE);
        else
            layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void method3(String s) {
        System.out.println(s);
    }
}