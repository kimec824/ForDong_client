package com.example.madcamp_proj2;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpURLConnection {
    public String request(String _url, ContentValues _params){
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //전송방식
            connection.setDoOutput(true);       //데이터를 쓸 지 설정
            connection.setDoInput(true);        //데이터를 읽어올지 설정

            InputStream is = connection.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String result;
            while((result = br.readLine())!=null){
                sb.append(result);
            }
            result = sb.toString();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
