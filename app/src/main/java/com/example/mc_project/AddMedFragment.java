package com.example.mc_project;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddMedFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton mAddFab1,mAddFab,mAddDosageFab;

    LinearLayout linearLayout_list;

    private EditText editMedName;
    private CheckBox allDayCheckBox;
    private CheckBox sunCheckBox;
    private CheckBox monCheckBox;
    private CheckBox tueCheckBox;
    private CheckBox wedCheckBox;
    private CheckBox thurCheckBox;
    private CheckBox friCheckBox;
    private CheckBox satCheckBox;

    private Button timeBtn;
    int hour, minute;

    private EditText dosageText;

    ImageView delete;

    Medicine med;
    Boolean[] days=new Boolean[7];
    List<String> times=new ArrayList<String>();
    List<Float> dosages=new ArrayList<Float>();
    int nchild,medCount=0;

    private String DEBUG_TAG="AddMedFragment";
    private View view;
    private Activity mActivity;


    public AddMedFragment() {
        // Required empty public constructor

    }


    public static AddMedFragment newInstance(String param1, String param2) {
        AddMedFragment fragment = new AddMedFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mAddFab1=mActivity.findViewById(R.id.add_fab);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddFab1.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_med, container, false);

        med = new Medicine();

        editMedName = (EditText)view.findViewById(R.id.edit_med_name);
        allDayCheckBox = (CheckBox)view.findViewById(R.id.all_day);
        sunCheckBox = (CheckBox)view.findViewById(R.id.dv_sunday);
        monCheckBox = (CheckBox)view.findViewById(R.id.dv_monday);
        tueCheckBox = (CheckBox)view.findViewById(R.id.dv_tuesday);
        wedCheckBox = (CheckBox)view.findViewById(R.id.dv_wednesday);
        thurCheckBox = (CheckBox)view.findViewById(R.id.dv_thursday);
        friCheckBox = (CheckBox)view.findViewById(R.id.dv_friday);
        satCheckBox = (CheckBox)view.findViewById(R.id.dv_saturday);

        linearLayout_list = view.findViewById(R.id.list);

        mAddDosageFab = view.findViewById(R.id.add_dosage_fab);
        mAddDosageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });

        mAddFab=view.findViewById(R.id.done_fab);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedata();

                Toast.makeText(mActivity, "Medicine saved", Toast.LENGTH_SHORT).show();

                //set alarm--to be done

                mActivity.onBackPressed();
            }
        });


        Log.e(DEBUG_TAG,"line 75");


        allDayCheckBox.setOnClickListener(this);
        Log.e(DEBUG_TAG,"line 79");
        sunCheckBox.setOnClickListener(this);
        monCheckBox.setOnClickListener(this);
        tueCheckBox.setOnClickListener(this);
        wedCheckBox.setOnClickListener(this);
        thurCheckBox.setOnClickListener(this);
        friCheckBox.setOnClickListener(this);
        satCheckBox.setOnClickListener(this);


        return view;
    }

    private void addView() {

        final View dosage = getLayoutInflater().inflate(R.layout.add_time_dosage,null,false);

        timeBtn = (Button)dosage.findViewById(R.id.timeButton);
        dosageText = (EditText)dosage.findViewById(R.id.tv_dose_quantity);
        delete = (ImageView)dosage.findViewById(R.id.delete_img);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = dosageText.getText().toString();
                if(!txt.isEmpty())
                    dosages.add(Float.parseFloat(txt));
                else
                    dosages.add(0.0f);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        dosageText.addTextChangedListener(textWatcher);


        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("LIne 222: "+linearLayout_list.indexOfChild(dosage));
                int pos=linearLayout_list.indexOfChild(dosage);

                int len=Math.max(dosages.size(),times.size());
                if(len>pos) {
                    if (dosages.size() == times.size() && dosages.size() != 0) {
                        dosages.remove(pos);
                        times.remove(pos);
                    } else if (dosages.size() > times.size())
                        dosages.remove(pos);
                    else if (dosages.size() < times.size())
                        times.remove(pos);
                }
                removeView(dosage);

            }
        });
//        System.out.println("Time: "+timeBtn.getText().toString());
//        System.out.println("Dosage: "+dosageText.getText().toString());
        linearLayout_list.addView(dosage);

    }

    private void removeView(View dosage) {
        linearLayout_list.removeView(dosage);
    }

    private void savedata() {

        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("medicine");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("medicines").exists()){
//                if(!dataSnapshot.exists()){
                    Log.i("DataSnapshot", "does not exists");
                    med.setMedName(editMedName.getText().toString().trim());
                    DatabaseReference ref3 = ref.child("medicines").child("medicine_"+med.getMedName());
                    medCount++;
//                    med.setMedName(editMedName.getText().toString().trim());
                    med.setDosage(dosages);
                    med.setTime(times);

                    List daylist = new ArrayList<Boolean>(Arrays.asList(days));
                    med.setDays(daylist);
                    ref3.setValue(med);

                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    List<Float> d = med.getDosage();
                    List<String> t = med.getTime();
                    List<Boolean> days_set = med.getDays();
                    ArrayList<Integer> alarm_days = new ArrayList<Integer>();

                    for(int i=0;i<7;i++){
                        if(days_set.get(i)){
                            switch(i){
                                case 0: alarm_days.add(Calendar.SUNDAY);
                                    break;
                                case 1: alarm_days.add(Calendar.MONDAY);
                                    break;
                                case 2: alarm_days.add(Calendar.TUESDAY);
                                    break;
                                case 3: alarm_days.add(Calendar.WEDNESDAY);
                                    break;
                                case 4: alarm_days.add(Calendar.THURSDAY);
                                    break;
                                case 5: alarm_days.add(Calendar.FRIDAY);
                                    break;
                                case 6: alarm_days.add(Calendar.SATURDAY);
                                    break;

                            }
                        }

                    }
                    String hr_min[] = t.get(0).split(":");
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Take medicine "+med.getMedName());
                    intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(hr_min[0]));
                    intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(hr_min[1]));
                    intent.putExtra(AlarmClock.EXTRA_DAYS, alarm_days);
                    if(intent.resolveActivity(mActivity.getPackageManager())!=null) {
                        mActivity.startActivity(intent);
                    }
                }
                else{
                    Log.i("DataSnapshot", "exists");
                    medCount = (int) dataSnapshot.child("medicines").getChildrenCount();
                    med.setMedName(editMedName.getText().toString().trim());
                    DatabaseReference ref3 = ref.child("medicines").child("medicine_"+med.getMedName());
                    medCount++;

//                    med.setMedName(editMedName.getText().toString().trim());
                    med.setDosage(dosages);
                    med.setTime(times);

                    List daylist = new ArrayList<Boolean>(Arrays.asList(days));
                    med.setDays(daylist);
                    ref3.setValue(med);

                    System.out.println(mActivity);
                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    List<Float> d = med.getDosage();
                    List<String> t = med.getTime();
                    List<Boolean> days_set = med.getDays();
                    ArrayList<Integer> alarm_days = new ArrayList<Integer>();

                    for(int i=0;i<7;i++){
                        if(days_set.get(i)){
                            switch(i){
                                case 0: alarm_days.add(Calendar.SUNDAY);
                                        break;
                                case 1: alarm_days.add(Calendar.MONDAY);
                                        break;
                                case 2: alarm_days.add(Calendar.TUESDAY);
                                        break;
                                case 3: alarm_days.add(Calendar.WEDNESDAY);
                                        break;
                                case 4: alarm_days.add(Calendar.THURSDAY);
                                        break;
                                case 5: alarm_days.add(Calendar.FRIDAY);
                                        break;
                                case 6: alarm_days.add(Calendar.SATURDAY);
                                        break;

                            }
                        }

                    }
                    String hr_min[] = t.get(0).split(":");
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Take medicine "+med.getMedName());
                    intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(hr_min[0]));
                    intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(hr_min[1]));
                    intent.putExtra(AlarmClock.EXTRA_DAYS, alarm_days);
                    if(intent.resolveActivity(mActivity.getPackageManager())!=null) {
                        mActivity.startActivity(intent);
                    }


//                    nchild = (int) dataSnapshot.getChildrenCount()+1;
//                    DatabaseReference ref3 = ref.child("medicines").child("medicine_"+nchild);
//                    med.setMedName(editMedName.getText().toString().trim());
//                    med.setDosage(dosages);
//                    med.setTime(times);
//
//                    List daylist = new ArrayList<Boolean>(Arrays.asList(days));
//                    med.setDays(daylist);
//                    ref3.setValue(med);
                }
//                else {
//                    Log.i("DataSnapshot", "exists");
//                    nchild = (int) dataSnapshot.getChildrenCount()+1;
//
//                    DatabaseReference ref2 = ref.child("med" + Integer.toString(nchild));
//
//                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Log.i("DataSnapshot", "checked till here 166");
//
//                            med.setMedName(editMedName.getText().toString().trim());
//                            med.setDosage(Float.parseFloat(dosageText.getText().toString()));
//
//
//                            List daylist = new ArrayList<Boolean>(Arrays.asList(days));
//                            med.setDays(daylist);
//
//                            ref2.setValue(med);
//                            Log.i("DataSnapshot", "checked till here 169");
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DBERROR","printing error"+error.getMessage());
            }
        });
    }

    public void onClick(View view1) {
        boolean checked = ((CheckBox) view1).isChecked();
        switch (view1.getId()) {
            case R.id.dv_sunday:
                if (checked) {
                    days[0] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[0] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_monday:
                if (checked) {
                    days[1] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[1] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    days[2] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[2] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    days[3] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[3] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    days[4] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[4] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    days[5] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[5] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    days[6] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF338CDC"));
                } else {
                    days[6] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.all_day:
                Log.e(DEBUG_TAG, "all_day switch case");
                LinearLayout ll = (LinearLayout)view.findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((CheckBox) v).setChecked(checked);
                    onClick(v);
                }
                Log.e(DEBUG_TAG, "all_day switch case passed");
                break;
        }
    }

    public void pickTime(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                Log.e(DEBUG_TAG, "on time set");
                hour = selectedHour;
                minute = selectedMinute;
                timeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
//                hrs.add(hour);
//                mins.add(minute);
                String s = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                times.add(s);
//                med.setHr(hour);
//                med.setMin(minute);
            }
        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }




}