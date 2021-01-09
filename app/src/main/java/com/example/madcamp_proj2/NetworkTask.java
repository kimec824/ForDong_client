package com.example.madcamp_proj2;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.madcamp_proj2.Fragment1.contactItems;
import static com.example.madcamp_proj2.Fragment1.adapter;
import static com.example.madcamp_proj2.Fragment1.listview;
import static com.example.madcamp_proj2.Fragment2.gridViewAdapter;
import static com.example.madcamp_proj2.Fragment2.gridview;
import static com.example.madcamp_proj2.MainActivity.context_main;

import static com.example.madcamp_proj2.Fragment2.feedItems;

///////////////////////////////////////////////////////////////////////////////////
/* Class for Network */
///////////////////////////////////////////////////////////////////////////////////
public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private String method;
    private int wantContent = 0; // 이변수가 1이면 contact 주고받기. 2면 photo주고받기.

    public NetworkTask(String url, ContentValues values, String method, int wantContent) {

        this.url = url;
        this.values = values;
        this.method = method;
        this.wantContent = wantContent;
    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        if(method == "GET" && wantContent == 1){
            result = requestHttpURLConnection.request_get(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }
        else if(method == "POST" && wantContent == 1){
            result = requestHttpURLConnection.request_post(url, values); // 해당 URL로 POST 보내기.
            return result;
        }
        else if(method == "GET" && wantContent ==2){
            result = requestHttpURLConnection.request_photoarray_get(url, values); // 해당 URL로 POST 보내기.
            return result;
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //System.out.println(s);
        if (method == "GET" && wantContent == 1){
            try{
                //Json parsing
                JSONObject jsonObject = new JSONObject(s);

                JSONArray contactsArray = jsonObject.getJSONArray("Contacts");


                for(int i=0; i<contactsArray.length(); i++)
                {
                    JSONObject movieObject = contactsArray.getJSONObject(i);
                    ContactItem contact = new ContactItem();

                    contact.setUser_name(movieObject.getString("name"));
                    contact.setUser_phNumber(movieObject.getString("phoneNumber"));
                    contact.setMail(movieObject.getString("email"));
                    //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                    contactItems.add(contact);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("CHECK : " + contactItems.size());
            for(int i = 0; i< contactItems.size() ; i++){
                Bitmap sampleBitmap = BitmapFactory.decodeResource( context_main.getResources(), R.drawable.person);
                ContactItem ci = contactItems.get(i);
                adapter.addItem(sampleBitmap, ci.getUser_name(), ci.getUser_phNumber(),
                        ci.getMail(), "sample address");
            }
            listview.setAdapter(adapter);
        }
        else if(method == "POST" && wantContent == 1){
            if(s == "fail"){
                //Log.e("fail","fail....");
            }
            else{
                //Log.e("success",s);
            }
        }
        else if(method == "GET" && wantContent == 2){
            try{
                //Json parsing
                JSONObject jsonObject = new JSONObject(s);

                JSONArray photosArray = jsonObject.getJSONArray("Photos");

                for(int i=0; i<photosArray.length(); i++)
                {
                    JSONObject photoObject = photosArray.getJSONObject(i);
                    GridViewItem feed = new GridViewItem();

                    feed.setName(photoObject.getString("name"));
                    feed.setImagePath(photoObject.getString("file_path"));
                    feed.setPhotoContext(photoObject.getString("context"));
                    //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                    //현재 url ~~/photo  내가 들어가야하는 곳 ~~/uploads/file_path
                    String path_url = "http://192.249.18.232:8080/uploads/" + feed.getImagePath();
                    ImageLoadTask task = new ImageLoadTask(path_url , feed);
                    task.execute();

                }
            }catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
/////////////////////////////////////////////////////////////////////////////////////
