package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity {

    EditText name;
    EditText ID;
    EditText Password;
    EditText phone;
    EditText email;
    String url_signup;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.nameCreate);
        ID = findViewById(R.id.IDCreate);
        Password = findViewById(R.id.PasswordCreate);
        phone = findViewById(R.id.PhoneCreate);
        email = findViewById(R.id.EmailCreate);

        create = findViewById(R.id.buttonCreate);
        url_signup = "http://192.249.18.250:8080/login/signup";

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dfdsfsdf","dfsdfsd");
                NetworkTask networkTask = new NetworkTask(url_signup+"?ID="+ID.getText().toString()+"&Password="+Password.getText().toString(), null, null);
                networkTask.execute();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////
    /* Class for Network */
    ///////////////////////////////////////////////////////////////////////////////////
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        private JSONObject jsonObject;

        public NetworkTask(String url, ContentValues values, JSONObject jsonObject) {

            this.url = url;
            this.values = values;
            this.jsonObject = jsonObject;
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("Network","doInBackground");
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            if(jsonObject != null)
                result = requestHttpURLConnection.request_post(url, values, jsonObject);
            else
                result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Json parsing
            if(jsonObject == null) {
                JsonParser jsonParser = new JsonParser();
                JsonObject collections = (JsonObject) jsonParser.parse(s);

                String verified = collections.get("Message").getAsString();
                if (verified.equals("Sign up")) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("name", name.getText().toString());
                        jsonObject.accumulate("ID", ID.getText().toString());
                        jsonObject.accumulate("phone", phone.getText().toString());
                        jsonObject.accumulate("Email", email.getText().toString());
                        NetworkTask networkTask2 = new NetworkTask(url_signup, null, jsonObject);
                        networkTask2.execute();
                    }catch (JSONException e){
                        Toast.makeText(SignupActivity.this,"error", Toast.LENGTH_SHORT);
                    }
                    Toast.makeText(getBaseContext(), "Sign up complete", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                finish();
            }
            Log.d("Network","onPostExecute");

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////
}