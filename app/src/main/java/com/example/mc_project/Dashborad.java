package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class Dashborad extends AppCompatActivity  {
    private Button logoutButton;
    private CardView cardCalendar;
    private CardView cardReports;
    private CardView cardReminder;
    private CardView cardSos;
    private CardView cardTracker;
    private CardView cardContacts;
    private FirebaseAuth mfirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashborad);

        cardCalendar = findViewById(R.id.cardCalendar);
        cardReports= findViewById(R.id.cardReports);
        cardReminder= findViewById(R.id.cardReminder);
        cardSos = findViewById(R.id.cardSos);
        cardTracker = findViewById(R.id.cardTracker);
        cardContacts = findViewById(R.id.cardContacts);
        logoutButton = findViewById(R.id.logoutButton);
//        logoutButton.setOnClickListener(this);
        mfirebaseAuth = FirebaseAuth.getInstance();

        cardCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashborad.this, CalendarActivity.class));

                }
            }
        );

        cardReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashborad.this, ReportActivity.class));

                }
            }
        );
        cardReminder.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(Dashborad.this, ReminderActivity.class));

               }
           }
        );

        cardSos.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(Dashborad.this, SosActivity.class));

               }
           }
        );
        cardTracker.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(Dashborad.this, TrackerActivity.class));

               }
           }
        );
        cardContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashborad.this, ContactsActivity.class));
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirebaseAuth.signOut();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut();
                googleSignInClient.revokeAccess();


                startActivity(new Intent(Dashborad.this,LoginRegistrationActivity.class));

            }
        });

        }
//    @Override
//    public void onClick(View view) {
//        switch(view.getId()) {
//            case R.id.logout:
//                startActivity(new Intent(Dashborad.this,LoginRegistrationActivity.class));
//
////                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment()).commit();
//                break;
//        }
//    }

}