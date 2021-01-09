package com.example.madcamp_proj2;

import android.content.ContentValues;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpURLConnection {
    public String request_get(String _url, ContentValues _params){
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //전송방식
            //connection.setDoOutput(true);       //데이터를 쓸 지 설정
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
    public String request_post(String _url, ContentValues _params){
        String result = null;
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); //전송방식
            connection.setDoOutput(true);       //데이터를 쓸 지 설정
            connection.setDoInput(true);        //데이터를 읽어올지 설정
            connection.setDefaultUseCaches(false);

            // Set some headers to inform server about the type of the content
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            String json= "";

            //build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name","testhihi!");
            jsonObject.accumulate("phoneNumber","010-6665-8728");
            jsonObject.accumulate("email","sampl@e.com");
            json = jsonObject.toString();

            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();

            // receive response as inputStream
            try{
                InputStream is = connection.getInputStream();
                // convert inputstream to string
                if(is != null){
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    while((result = br.readLine())!=null){
                        sb.append(result);
                    }
                    result = sb.toString();
                    return result;
                }
                else{
                    result = "fail";
                    return result;
                }

            }
            catch(IOException e){
                e.printStackTrace();
            }
            finally{
                connection.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}