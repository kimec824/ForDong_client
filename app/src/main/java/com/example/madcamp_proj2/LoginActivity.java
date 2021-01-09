package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText password;
    Button Login;
    Button SignUp;
    private LoginButton btn_facebook_login;
    private LoginCallback mLoginCallback;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();
        mLoginCallback = new LoginCallback();

        btn_facebook_login = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"));
        btn_facebook_login.registerCallback(mCallbackManager, mLoginCallback);

        getHashKey();
        id = findViewById(R.id.loginId);
        password = findViewById(R.id.loginPassword);
        Login = findViewById(R.id.buttonLogin);
        SignUp = findViewById(R.id.buttonSignUp);

        String url = "http://192.249.18.250:8080/login/?";

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask2 networkTask = new NetworkTask2(url+"ID="+id.getText().toString()+"&Password="+password.getText().toString(), null);
                networkTask.execute();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////
    /* Class for Network */
    ///////////////////////////////////////////////////////////////////////////////////
    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask2(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_get(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Json parsing
            JsonParser jsonParser = new JsonParser();
            JsonObject collections = (JsonObject) jsonParser.parse(s);

            String verified = collections.get("Message").getAsString();

            if(verified.equals("verified")){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else if(verified.equals("fail")){
                Toast.makeText(getBaseContext(),"Wrong ID/Password", Toast.LENGTH_SHORT).show();
            }

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(mLoginCallback.success) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}