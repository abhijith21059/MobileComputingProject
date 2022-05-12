package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {


    private RecyclerView recyclerViewReport;
    private ReportAdapter adapter;
    private DatabaseReference databaseReference;

    private ArrayList<String> listImg;


    @Override
    protected void onStart() {
        super.onStart();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(user).child("reports");
        listImg = new ArrayList<>();
        adapter = new ReportAdapter(this, listImg);
        recyclerViewReport.setAdapter(adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    listImg.add(snap.getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        recyclerViewReport = findViewById(R.id.recViewReports);
        recyclerViewReport.setHasFixedSize(true);
        recyclerViewReport.setLayoutManager(new LinearLayoutManager(this));

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