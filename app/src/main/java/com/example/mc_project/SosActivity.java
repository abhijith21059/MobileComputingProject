package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SosActivity extends AppCompatActivity {

    private SmsManager smsManager;
    private double latitude, longitude;
    private LocationListener mlocListener;
    private LocationManager locationManager;
    private static ArrayList<String> phoneNumbers;
    private ProgressBar pb;

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("SOSActivity.class", "OnLocationChanged called");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("SOS", "OnLocationChanged latitude "+ latitude);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("emergency contacts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    phoneNumbers.add(postSnapshot.child("phoneNumber").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        pb = findViewById(R.id.progressBarContacts);
        phoneNumbers = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                pb.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(SosActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SosActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SosActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocListener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                StringBuffer smsbody = new StringBuffer();
               // smsbody.append("http://maps.google.com?q=");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("SOS", "latitude" + latitude + "Longitude" + longitude);
                        String address = "http://maps.google.com/maps?saddr=" + latitude+","+longitude;
                        smsbody.append("Help me out. This is my current location : \n");
                        smsbody.append(Uri.parse(address));
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        if (!phoneNumbers.isEmpty()) {
                            callIntent.setData(Uri.parse("tel:" + phoneNumbers.get(0)));
                            ActivityCompat.requestPermissions(SosActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
                            startActivity(callIntent);
                            smsManager = SmsManager.getDefault();
                            for (int i = 1; i < phoneNumbers.size(); i++) {
                                smsManager.sendTextMessage(phoneNumbers.get(i), null, smsbody.toString(), null, null);
                            }
                            Log.d("SOS", "onClick: " + phoneNumbers.size());
                            Toast.makeText(getApplicationContext(), "Message sent to the emergency contacts", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "No emergency contacts available", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                }, 1000);
            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                // Do nothing
                dialog.dismiss();
            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }


}