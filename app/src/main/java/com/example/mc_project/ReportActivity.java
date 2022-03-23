package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
//        TextView hello = findViewById(R.id.tvReport);
//        hello.setText("IN REPORTS");
//        getSupportFragmentManager().beginTransaction().replace(R.id.ReportFragment, new ListReportFragment()).commit();
        Button add = findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, CaptureActivity.class);
                startActivity(intent);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ListReportFragment, new CaptureImageFragment(), "NewFragmentTag").addToBackStack(null).commit();

            }
        });


    }
}