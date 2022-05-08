package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    EditText docNameEV, decsEV, locationEV;
    CalendarView calendarView;
    int month;
    int day;
    int year;
    int hour;
    int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        docNameEV = findViewById(R.id.docNameEV);
        decsEV = findViewById(R.id.descEV);
        locationEV = findViewById(R.id.locationEV);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                year = i;
                month = i1+1;
                day = i2;
            }
        });
    }

    public void onSaveEvent(View view) throws ParseException {
        if(!docNameEV.getText().toString().isEmpty() && !decsEV.getText().toString().isEmpty() && !locationEV.getText().toString().isEmpty()){
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, decsEV.getText().toString());

            String dateSel = year+"/"+month+"/"+day+" "+hour+":"+min+":00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sdf.parse(dateSel);
            long millis = date.getTime();

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millis);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millis+3600000);

            intent.putExtra(CalendarContract.Events.DESCRIPTION, docNameEV.getText().toString());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, locationEV.getText().toString());
            intent.putExtra(Intent.EXTRA_EMAIL, "aman.srivastava1118@gmail.com");



            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }
            else{
                Toast.makeText(CalendarActivity.this, "There is no app that supports calendar in your phone", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(CalendarActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void pickTime(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                min = selectedMinute;
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this, /*style,*/ onTimeSetListener, hour, min, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}