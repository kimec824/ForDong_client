package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements AsyncTaskCallback{

    TextView userID;
    TextView userName;
    TextView userPhone;
    TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = findViewById(R.id.idProfile);
        userName = findViewById(R.id.nameProfile);
        userPhone = findViewById(R.id.phoneProfile);
        userEmail = findViewById(R.id.emailProfile);

        Intent intent = getIntent();
        userID.setText(intent.getStringExtra("userID"));

        String url = "http://"+getString(R.string.ip)+":8080/contacts/"+userID.getText().toString();

        NetworkTask networkTask = new NetworkTask(url, null, "GET",null, this);
        networkTask.execute();
    }

    @Override
    public void method1(String s) {
        try{
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);

            JSONArray contactsArray = jsonObject.getJSONArray("Contact");
            JSONObject contact = contactsArray.getJSONObject(0);

            userName.setText(contact.getString("name"));
            userPhone.setText(contact.getString("phone"));
            userEmail.setText(contact.getString("email"));

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}