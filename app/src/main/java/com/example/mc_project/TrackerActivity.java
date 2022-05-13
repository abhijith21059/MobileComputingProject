package com.example.mc_project;

import static com.google.android.gms.fitness.data.Field.FIELD_STEPS;
import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackerActivity extends AppCompatActivity implements OnSuccessListener{

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 2;
    private static final String TAG = "TrackerActivity";
    private FitnessOptions fitnessOptions;
    private FitnessData fitnessData;
    private TextView txt, calTxt, distTxt, historyTxt;
    OnDataPointListener listener;
    CircularProgressBar circularProgressBar;
    private ToggleButton btnWalk;
    private boolean permissions;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        txt = findViewById(R.id.activityTracker);
        calTxt = findViewById(R.id.calTracker);
        distTxt = findViewById(R.id.distTracker);
        historyTxt = findViewById(R.id.txtHistory);
        txt.setText("IN TRACKER");

        barChart = (BarChart) findViewById(R.id.bargraph);

        btnWalk = (ToggleButton)findViewById(R.id.btnWalk);

        fitnessData = new FitnessData();
        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.RIGHT_TO_LEFT);
        circularProgressBar.setProgressMax(1000f);

        checkPermissions();

        btnWalk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    startDataReading();
                }
                else{
                    unsubscribe(DataType.TYPE_STEP_COUNT_DELTA);
                    unsubscribe(DataType.TYPE_CALORIES_EXPENDED);
                    unsubscribe(DataType.TYPE_DISTANCE_DELTA);
                }
            }
        });

        ArrayList<BarEntry> barEntries=new ArrayList<>();
        ArrayList<String> dates=new ArrayList<>();;
        barEntries.add(new BarEntry(0,0));

        dates.add("today");
        barDataSet = new BarDataSet(barEntries,"Dates");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.setVisibleXRangeMaximum(7);
        barChart.getDescription().setText("Weekly step count");
    }

    private void unsubscribe(DataType dataType) {
        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                // This example shows unsubscribing from a DataType. A DataSource
                // should be used where the subscription was to a DataSource.
                // Alternatively, if the client doesn’t maintain its subscription
                // information, they can use an element from the return value of
                // listSubscriptions(), which is of type Subscription.
                .unsubscribe(dataType)
                .addOnSuccessListener(unused ->
                        Log.i(TAG,"Successfully unsubscribed."))
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Failed to unsubscribe.");
                    // Retry the unsubscribe request.
                });
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
            btnWalk.setEnabled(true);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            btnWalk.setEnabled(true);
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
                    Toast.makeText(this, "Activity Recognition Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void startDataReading() {
        Toast.makeText(this, "inside start data reading", Toast.LENGTH_SHORT).show();
        getTodayData();
        requestForHistory();
        subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA);
        subscribeAndGetRealTimeData(DataType.TYPE_CALORIES_EXPENDED);
        subscribeAndGetRealTimeData(DataType.TYPE_DISTANCE_DELTA);
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
        if(dataType==DataType.TYPE_STEP_COUNT_DELTA) {
            Fitness.getSensorsClient(this, getGoogleAccount())
                    .add(new SensorRequest.Builder()
                                    .setDataType(dataType)
                                    .setSamplingRate(10, SECONDS)
                                    .build(),
                            new OnDataPointListener() {
                                @Override
                                public void onDataPoint(@NonNull DataPoint dataPoint) {
                                    float value = Float.parseFloat(dataPoint.getValue(FIELD_STEPS).toString());
//                                float activity = Float.parseFloat(dataPoint.getValue(Field.FIELD_ACTIVITY).toString());
                                    fitnessData.steps = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.steps));
                                    circularProgressBar.setProgress(fitnessData.steps);
                                    txt.setText("Steps: " + fitnessData.steps);
                                }
                            }
                    );
        }
        else if(dataType == DataType.TYPE_CALORIES_EXPENDED) {
            Fitness.getSensorsClient(this, getGoogleAccount())
                    .add(new SensorRequest.Builder()
                                    .setDataType(dataType)
                                    .setSamplingRate(10, SECONDS)
                                    .build(),
                            new OnDataPointListener() {
                                @Override
                                public void onDataPoint(@NonNull DataPoint dataPoint) {
                                    float cal = Float.parseFloat(dataPoint.getValue(Field.FIELD_CALORIES).toString());
                                    fitnessData.calories = Float.parseFloat(new DecimalFormat("#.##").format(cal));
                                    calTxt.setText("Steps: " + fitnessData.calories);
                                }
                            }
                    );
        }
        else if(dataType == DataType.TYPE_DISTANCE_DELTA) {
            Fitness.getSensorsClient(this, getGoogleAccount())
                    .add(new SensorRequest.Builder()
                                    .setDataType(dataType)
                                    .setSamplingRate(10, SECONDS)
                                    .build(),
                            new OnDataPointListener() {
                                @Override
                                public void onDataPoint(@NonNull DataPoint dataPoint) {
                                    float dist = Float.parseFloat(dataPoint.getValue(Field.FIELD_DISTANCE).toString());
                                    fitnessData.distance = Float.parseFloat(new DecimalFormat("#.##").format(dist + fitnessData.distance));
                                    distTxt.setText("Steps: " + fitnessData.distance);
                                }
                            }
                    );
        }
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
            Toast.makeText(this, "datareadresponse", Toast.LENGTH_SHORT).show();
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

                if (field.getName().equals(FIELD_STEPS.getName())) {
                    fitnessData.steps = Float.parseFloat(new DecimalFormat("#.##").format(value));
                    circularProgressBar.setProgress(fitnessData.steps);
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

    }

    private void getDataFromDataReadResponse(DataSet dataSet) {
        List<DataPoint> dataPoints = dataSet.getDataPoints();
        int i=0;
        for (DataPoint dataPoint : dataPoints) {
            Log.e(TAG, dataPoint.toString());
            for (Field field : dataPoint.getDataType().getFields()) {
                float value = Float.parseFloat(dataPoint.getValue(field).toString());
                Log.e(TAG, " data : " + value);

                if (field.getName().equals(FIELD_STEPS.getName())) {
                    float steps = Float.parseFloat(new DecimalFormat("#.##").format(value + fitnessData.steps));
//                    historyTxt.setText(historyTxt.getText() + " Steps" + ":" + steps + ":");
//                    Log.e(TAG, historyTxt.getText()+" Steps"+i+":"+steps+":");;
                    barData.addEntry(new BarEntry(barDataSet.getEntryCount(), steps), 0);

                    barData.notifyDataChanged();
                    barChart.notifyDataSetChanged();
                    barChart.moveViewToX(barData.getEntryCount());

                }
            }
            i+=1;
        }

    }


    private void requestForHistory() {
        Calendar cal;

        cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime=cal.getTimeInMillis();

        cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long endTime=cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Log.e(TAG, "line 418:"+readRequest.toString());
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readData(readRequest)
                .addOnSuccessListener(this);


    }
}