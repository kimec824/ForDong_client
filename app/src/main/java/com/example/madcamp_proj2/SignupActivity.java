package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements AsyncTaskCallback{

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

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        
        create = findViewById(R.id.buttonCreate);
        url_signup = "http://"+getString(R.string.ip)+":8080/login/signup";

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dfdsfsdf","dfsdfsd");

                if (name.getText().toString().matches("")) {
                    Toast.makeText(SignupActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ID.getText().toString().matches("")) {
                    Toast.makeText(SignupActivity.this, "You did not enter a ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.getText().toString().matches("")) {
                    Toast.makeText(SignupActivity.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.getText().toString().matches("")) {
                    Toast.makeText(SignupActivity.this, "You did not enter a phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.getText().toString().matches("")) {
                    Toast.makeText(SignupActivity.this, "You did not enter a email", Toast.LENGTH_SHORT).show();
                    return;
                }

                    NetworkTask networkTask = new NetworkTask(url_signup + "?ID=" + ID.getText().toString() + "&Password=" + Password.getText().toString(), null, "GET", null, SignupActivity.this);
                    networkTask.execute();

            }
        });
    }

    @Override
    public void method1(String s) {
        JsonParser jsonParser = new JsonParser();
        JsonObject collections = (JsonObject) jsonParser.parse(s);

        String verified = collections.get("Message").getAsString();
        if (verified.equals("Sign up")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", name.getText().toString());
                jsonObject.accumulate("ID", ID.getText().toString());
                jsonObject.accumulate("phone", phone.getText().toString());
                jsonObject.accumulate("email", email.getText().toString());
                jsonObject.accumulate("photo","null");
                NetworkTask networkTask = new NetworkTask(url_signup, null, "POST", jsonObject,  SignupActivity.this);
                networkTask.execute();
            }catch (JSONException e){
                Toast.makeText(SignupActivity.this,"error", Toast.LENGTH_SHORT);
            }
            Toast.makeText(getBaseContext(), "Sign up complete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void method2(String s) {
        finish();
    }
}