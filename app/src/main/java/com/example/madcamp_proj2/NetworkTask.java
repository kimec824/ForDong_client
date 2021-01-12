package com.example.madcamp_proj2;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.madcamp_proj2.Fragment1.contactItems;
import static com.example.madcamp_proj2.Fragment1.adapter;
import static com.example.madcamp_proj2.Fragment1.listview;
import static com.example.madcamp_proj2.MainActivity.context_main;

///////////////////////////////////////////////////////////////////////////////////
/* Class for Network */
///////////////////////////////////////////////////////////////////////////////////
public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private String method;
    private JSONObject jsonObject;
    private AsyncTaskCallback mCallback;

    public NetworkTask(String url, ContentValues values, String method , JSONObject jsonObject, AsyncTaskCallback mCallback) {

        this.url = url;
        this.values = values;
        this.method = method;
        this.jsonObject = jsonObject;
        this.mCallback= mCallback;

    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        if(jsonObject == null){
            result = requestHttpURLConnection.request_get(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }
        else if(method == "POST"){
            result = requestHttpURLConnection.request_post(url, values, jsonObject); // 해당 URL로 POST 보내기.
            return result;
        }
        else{
            result = requestHttpURLConnection.request_post(url, values, jsonObject); // 해당 URL로 POST 보내기.
            return result;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //System.out.println(s);
        if(jsonObject == null)
            mCallback.method1(s);
        else if(method == "POST")
            mCallback.method2(s);
        else
            mCallback.method3(s);
    }
}
/////////////////////////////////////////////////////////////////////////////////////
