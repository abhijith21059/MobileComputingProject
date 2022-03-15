package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
        /*Intent intent = new Intent(getApplicationContext(), LoginRegistrationActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment()).commit();
                break;
        }
    }
}