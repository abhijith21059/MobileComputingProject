package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackerActivity extends AppCompatActivity implements OnSuccessListener{

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 2;
    private static final String TAG = "TrackerActivity";
    private FitnessOptions fitnessOptions;
    private FitnessData fitnessData;
    private TextView txt, calTxt, distTxt;
    OnDataPointListener listener;
    CircularProgressBar circularProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        txt = findViewById(R.id.activityTracker);
        calTxt = findViewById(R.id.calTracker);
        distTxt = findViewById(R.id.distTracker);
        txt.setText("IN TRACKER");

        fitnessData = new FitnessData();
        checkPermissions();

    }

    private void checkPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.ACTIVITY_RECOGNITION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
        }
        else{
            checkGoogleFitPermission();
        }
    }

    private void checkGoogleFitPermission() {
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = getGoogleAccount();

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    TrackerActivity.this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        }
        else{
            startDataReading();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            startDataReading();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    //Permission was granted. Now you can call your method to open camera, fetch contact or whatever
                    Toast.makeText(this, "Activity Recognition Permission Granted ", Toast.LENGTH_SHORT).show();
                    checkGoogleFitPermission();
                } else {
                    // Permission was denied.......
                    // You can again ask for permission from
                    Toast.makeText(this, "Activity Recognition or Location Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void startDataReading() {
        Toast.makeText(this, "inside start data reading", Toast.LENGTH_SHORT).show();
        getTodayData();
        subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA);
    }

    /*
     * You can subscribe specific data types
     */
    private void subscribeAndGetRealTimeData(DataType dataType) {
        Fitness.getRecordingClient(this, getGoogleAccount())
                .subscribe(dataType)
                .addOnSuccessListener(aVoid -> {
                    Log.e(TAG, "Subscribed");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failure " + e.getLocalizedMessage());
                });

        getDataUsingSensor(dataType);
    }

    /*
     * Register Sensor Client to get Real Time Data
     */
    private void getDataUsingSensor(DataType dataType) {
        Fitness.getSensorsClient(this, getGoogleAccount())
                .add(new SensorRequest.Builder()
                                .setDataType(dataType)
                                .setSamplingRate(10, TimeUnit.SECONDS)
                                .build(),
                        new OnDataPointListener() {
                            @Override
                            public void onDataPoint(@NonNull DataPoint dataPoint) {
                                float value = Float.parseFloat(dataPoint.getValue(Field.FIELD_STEPS).toString());
//                                float cal = Float.parseFloat(dataPoint.getValue(Field.FIELD_CALORIES).toString());
//                                float dist = Float.parseFloat(dataPoint.getValue(Field.FIELD_DISTANCE).toString());
//                                float activity = Float.parseFloat(dataPoint.getValue(Field.FIELD_ACTIVITY).toString());
                                fitnessData.steps = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.steps));
//                                step_count
                                txt.setText("Steps: "+fitnessData.steps);
                            }
                        }
                );

        Fitness.getSensorsClient(this, getGoogleAccount())
                .add(new SensorRequest.Builder()
                                .setDataType(dataType)
                                .setSamplingRate(10, TimeUnit.SECONDS)
                                .build(),
                        new OnDataPointListener() {
                            @Override
                            public void onDataPoint(@NonNull DataPoint dataPoint) {
                                float cal = Float.parseFloat(dataPoint.getValue(Field.FIELD_CALORIES).toString());
                                fitnessData.calories = Float.parseFloat(new DecimalFormat("#.##").format(cal + fitnessData.calories));
                                calTxt.setText("Steps: "+fitnessData.calories);
                            }
                        }
                );

        Fitness.getSensorsClient(this, getGoogleAccount())
                .add(new SensorRequest.Builder()
                                .setDataType(dataType)
                                .setSamplingRate(10, TimeUnit.SECONDS)
                                .build(),
                        new OnDataPointListener() {
                            @Override
                            public void onDataPoint(@NonNull DataPoint dataPoint) {
                                float dist = Float.parseFloat(dataPoint.getValue(Field.FIELD_DISTANCE).toString());
                                fitnessData.distance = Float.parseFloat(new DecimalFormat("#.##").format(dist + fitnessData.distance));
                                distTxt.setText("Steps: "+fitnessData.distance);
                            }
                        }
                );

    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(getApplicationContext(), fitnessOptions);
    }

    private void getTodayData() {
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(this);
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(this);
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(this);
    }


    @Override
    public void onSuccess(Object o) {

        if (o instanceof DataSet) {
            DataSet dataSet = (DataSet) o;
            if (dataSet != null) {
                getDataFromDataSet(dataSet);
            }
        } else if (o instanceof DataReadResponse) {
            fitnessData.steps = 0f;
            fitnessData.calories = 0f;
            fitnessData.distance = 0f;
            DataReadResponse dataReadResponse = (DataReadResponse) o;
            if (dataReadResponse.getBuckets() != null && !dataReadResponse.getBuckets().isEmpty()) {
                List<Bucket> bucketList = dataReadResponse.getBuckets();

                if (bucketList != null && !bucketList.isEmpty()) {
                    for (Bucket bucket : bucketList) {
                        DataSet stepsDataSet = bucket.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                        getDataFromDataReadResponse(stepsDataSet);
                        DataSet caloriesDataSet = bucket.getDataSet(DataType.TYPE_CALORIES_EXPENDED);
                        getDataFromDataReadResponse(caloriesDataSet);
                        DataSet distanceDataSet = bucket.getDataSet(DataType.TYPE_DISTANCE_DELTA);
                        getDataFromDataReadResponse(distanceDataSet);
                    }
                }
            }
        }
    }

    private void getDataFromDataSet(DataSet dataSet) {
        List<DataPoint> dataPoints = dataSet.getDataPoints();
        for (DataPoint dataPoint : dataPoints) {

            Log.e(TAG, " data manual : " + dataPoint.getOriginalDataSource().getStreamName());

            for (Field field : dataPoint.getDataType().getFields()) {
                float value = Float.parseFloat(dataPoint.getValue(field).toString());
                Log.e(TAG, " data : " + value);

                if (field.getName().equals(Field.FIELD_STEPS.getName())) {
                    fitnessData.steps = Float.parseFloat(new DecimalFormat("#.##").format(value));
                    txt.setText("Steps: "+fitnessData.steps);
                } else if (field.getName().equals(Field.FIELD_CALORIES.getName())) {
                    fitnessData.calories = Float.parseFloat(new DecimalFormat("#.##").format(value));
                    calTxt.setText("Calories: "+fitnessData.calories);
                } else if (field.getName().equals(Field.FIELD_DISTANCE.getName())) {
                    fitnessData.distance = Float.parseFloat(new DecimalFormat("#.##").format(value/1000));
                    distTxt.setText("Distance: "+fitnessData.distance+" km");
                }

            }
        }
//        activityMainBinding.setFitnessData(fitnessDataResponseModel);

    }

    private void getDataFromDataReadResponse(DataSet dataSet) {

        List<DataPoint> dataPoints = dataSet.getDataPoints();
        for (DataPoint dataPoint : dataPoints) {
            for (Field field : dataPoint.getDataType().getFields()) {

                float value = Float.parseFloat(dataPoint.getValue(field).toString());
                Log.e(TAG, " data : " + value);

                if (field.getName().equals(Field.FIELD_STEPS.getName())) {
                    fitnessData.steps = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.steps));
                } else if (field.getName().equals(Field.FIELD_CALORIES.getName())) {
                    fitnessData.calories = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.calories));
                } else if (field.getName().equals(Field.FIELD_DISTANCE.getName())) {
                    fitnessData.distance = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.distance));
                }
            }
        }
//        activityMainBinding.setFitnessData(fitnessDataResponseModel);
        txt.setText("Steps: "+fitnessData.steps);
    }
}