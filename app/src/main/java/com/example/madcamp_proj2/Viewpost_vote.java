package com.example.madcamp_proj2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Viewpost_vote extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpost_vote);
        TextView showtime=(TextView) findViewById(R.id.time_a);
        TextView writer=(TextView) findViewById(R.id.writer_a);
        TextView title=(TextView) findViewById(R.id.viewtitle_a);
        TextView content=(TextView) findViewById(R.id.viewcontent_a);
    }

}
