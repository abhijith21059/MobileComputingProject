package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

public class ReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        //TextView hello = findViewById(R.id.tvReminder);
        //hello.setText("IN REMINDER");

        //AddMedFragment addMedicineFragment = (AddMedFragment) getSupportFragmentManager();
        FragmentManager fm = getSupportFragmentManager();
        Fragment addMedicineFragment = fm.findFragmentById(R.id.fragment_container);
        //findFragmentById(R.id.fragment_container);
        if (addMedicineFragment == null) {
            addMedicineFragment = new AddMedFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, addMedicineFragment)
                    .commit();
        }

    }
}