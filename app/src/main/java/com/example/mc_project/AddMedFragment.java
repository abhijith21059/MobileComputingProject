package com.example.mc_project;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.List;
import java.util.Locale;


public class AddMedFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton mAddFab1,mAddFab;

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

    Medicine med;
    Boolean[] days=new Boolean[7];
    int nchild;

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
        //mAddFab1.setImageResource(R.drawable.ic_done);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddFab1.setVisibility(View.VISIBLE);
        //mAddFab1.setImageResource(R.drawable.ic_add);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_med, container, false);

        view= inflater.inflate(R.layout.fragment_add_med, container, false);
        editMedName = (EditText)view.findViewById(R.id.edit_med_name);
        allDayCheckBox = (CheckBox)view.findViewById(R.id.all_day);
        sunCheckBox = (CheckBox)view.findViewById(R.id.dv_sunday);
        monCheckBox = (CheckBox)view.findViewById(R.id.dv_monday);
        tueCheckBox = (CheckBox)view.findViewById(R.id.dv_tuesday);
        wedCheckBox = (CheckBox)view.findViewById(R.id.dv_wednesday);
        thurCheckBox = (CheckBox)view.findViewById(R.id.dv_thursday);
        friCheckBox = (CheckBox)view.findViewById(R.id.dv_friday);
        satCheckBox = (CheckBox)view.findViewById(R.id.dv_saturday);

        mAddFab=view.findViewById(R.id.done_fab);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                save to database----to be done0
                savedata();
                Toast.makeText(mActivity, "Medicine saved", Toast.LENGTH_SHORT).show();
                mActivity.onBackPressed();
            }
        });

        timeBtn = (Button)view.findViewById(R.id.timeButton);
        dosageText = (EditText)view.findViewById(R.id.tv_dose_quantity);

        Log.e(DEBUG_TAG,"line 75");

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view);
            }
        });

//        editMedName.addTextChangedListener(this);
        allDayCheckBox.setOnClickListener(this);
        Log.e(DEBUG_TAG,"line 79");
//        allDayCheckBox.setOnCheckedChangeListener(this);
        sunCheckBox.setOnClickListener(this);
        monCheckBox.setOnClickListener(this);
        tueCheckBox.setOnClickListener(this);
        wedCheckBox.setOnClickListener(this);
        thurCheckBox.setOnClickListener(this);
        friCheckBox.setOnClickListener(this);
        satCheckBox.setOnClickListener(this);

        med = new Medicine();

        return view;
    }

    private void savedata() {

        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("medicine");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Log.i("DataSnapshot", "does not exists");
                }
                else {
                    Log.i("DataSnapshot", "exists");
                    nchild = (int) dataSnapshot.getChildrenCount()+1;

                    DatabaseReference ref2 = ref.child("med" + Integer.toString(nchild));

                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("DataSnapshot", "checked till here 166");

                            med.setMedName(editMedName.getText().toString().trim());
//                            Log.i("Dosagecheck","prinitng:"+dosageText.getText().toString());
                            med.setDosage(Float.parseFloat(dosageText.getText().toString()));

//                med.setDays(new boolean[]{true,false,true,false,true,false,false});

                            List daylist = new ArrayList<Boolean>(Arrays.asList(days));
                            med.setDays(daylist);

                            ref2.setValue(med);
                            Log.i("DataSnapshot", "checked till here 169");
//                medicineDataList.clear();

//                for(DataSnapshot snapshot1:dataSnapshot.getChildren()){
//                    Log.i("children","med available"+snapshot1.getValue().toString());
//                    Medicine m=new Medicine(snapshot1.getValue().toString());
//                    //medicineDataList.add(m);
//                }
//                medicineAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
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
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[0] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_monday:
                if (checked) {
                    days[1] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[1] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    days[2] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[2] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    days[3] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[3] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    days[4] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[4] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    days[5] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                } else {
                    days[5] = false;
                    allDayCheckBox.setChecked(false);
                    view1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    days[6] = true;
                    view1.setBackgroundColor(Color.parseColor("#FF03DAC5"));
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
                med.setHr(hour);
                med.setMin(minute);
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }




}