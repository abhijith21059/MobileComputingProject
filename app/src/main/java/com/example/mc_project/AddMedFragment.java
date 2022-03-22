package com.example.mc_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AddMedFragment extends Fragment implements View.OnClickListener {


    private EditText editMedName;
    private CheckBox allDayCheckBox;
    private CheckBox sunCheckBox;
    private CheckBox monCheckBox;
    private CheckBox tueCheckBox;
    private CheckBox wedCheckBox;
    private CheckBox thurCheckBox;
    private CheckBox friCheckBox;
    private CheckBox satCheckBox;
    private TextView timeText;
    private EditText dosageText;

    Medicine med;


    public AddMedFragment() {
        // Required empty public constructor

    }


    public static AddMedFragment newInstance(String param1, String param2) {
        AddMedFragment fragment = new AddMedFragment();
        Bundle args = new Bundle();


        return fragment;
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

        View view= inflater.inflate(R.layout.fragment_add_med, container, false);
        editMedName = (EditText)view.findViewById(R.id.edit_med_name);
        allDayCheckBox = (CheckBox)view.findViewById(R.id.all_day);
        sunCheckBox = (CheckBox)view.findViewById(R.id.dv_sunday);
        monCheckBox = (CheckBox)view.findViewById(R.id.dv_monday);
        tueCheckBox = (CheckBox)view.findViewById(R.id.dv_tuesday);
        wedCheckBox = (CheckBox)view.findViewById(R.id.dv_wednesday);
        thurCheckBox = (CheckBox)view.findViewById(R.id.dv_thursday);
        friCheckBox = (CheckBox)view.findViewById(R.id.dv_friday);
        satCheckBox = (CheckBox)view.findViewById(R.id.dv_saturday);

        timeText = (TextView)view.findViewById(R.id.tv_medicine_time);
        dosageText = (EditText)view.findViewById(R.id.tv_dose_quantity);


//        editMedName.addTextChangedListener(this);
        allDayCheckBox.setOnClickListener(this);
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

    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.dv_monday:
                if (checked) {
                    med.days[1] = true;
                } else {
                    med.days[1] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    med.days[2] = true;
                } else {
                    med.days[2] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    med.days[3] = true;
                } else {
                    med.days[3] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    med.days[4] = true;
                } else {
                    med.days[4] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    med.days[5] = true;
                } else {
                    med.days[5] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    med.days[6] = true;
                } else {
                    med.days[6] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.dv_sunday:
                if (checked) {
                    med.days[0] = true;
                } else {
                    med.days[0] = false;
                    allDayCheckBox.setChecked(false);
                }
                break;
            case R.id.all_day:
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((CheckBox) v).setChecked(checked);
                    onClick(v);
                }
                break;
        }
    }

}