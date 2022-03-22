package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReminderActivity extends AppCompatActivity {

    FloatingActionButton mAddFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        //TextView hello = findViewById(R.id.tvReminder);
        //hello.setText("IN REMINDER");

        //AddMedFragment addMedicineFragment = (AddMedFragment) getSupportFragmentManager();

        FragmentManager fm1 = getSupportFragmentManager();
        Fragment MedicineFragment = fm1.findFragmentById(R.id.fragment_container);
        //findFragmentById(R.id.fragment_container);
        if (MedicineFragment == null) {
            MedicineFragment = new MedicineFragment();
            fm1.beginTransaction()
                    .replace(R.id.fragment_container, MedicineFragment)
                    .addToBackStack(null)
                    .commit();
        }




        mAddFab=findViewById(R.id.add_fab);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddFab.setVisibility(view.GONE);
                //mAddFab.setImageResource(R.drawable.ic_done);
                FragmentManager fm = getSupportFragmentManager();
                Fragment addMedicineFragment = fm.findFragmentById(R.id.fragment_container);
                //findFragmentById(R.id.fragment_container);
                if (addMedicineFragment == null) {
                    addMedicineFragment = new AddMedFragment();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, addMedicineFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

//        FragmentManager fm = getSupportFragmentManager();
//        Fragment addMedicineFragment = fm.findFragmentById(R.id.fragment_container);
//        //findFragmentById(R.id.fragment_container);
//        if (addMedicineFragment == null) {
//            addMedicineFragment = new AddMedFragment();
//            fm.beginTransaction()
//                    .add(R.id.fragment_container, addMedicineFragment)
//                    .commit();
//        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAddFab.setImageResource(R.drawable.ic_add);
//    }
}