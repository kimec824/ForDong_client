package com.example.madcamp_proj2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Comments extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_announce);
        EditText title=(EditText) findViewById(R.id.Add_title);
        EditText content=(EditText) findViewById(R.id.Add_content);
        //CheckBox check1=(CheckBox) findViewById(R.id.checkBox1);
        //CheckBox check2=(CheckBox) findViewById(R.id.checkBox2);
        //CheckBox check3=(CheckBox) findViewById(R.id.checkBox3);
        //Button addpost=(Button) findViewById(R.id.confirm);
    }
}
