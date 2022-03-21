package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TrackerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        TextView hello = findViewById(R.id.tvTracker);
        hello.setText("IN TRACKER");

        //this is a comment
    }
}