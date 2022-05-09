package com.example.mc_project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MedHolder extends RecyclerView.ViewHolder {

    private final TextView mMedicineName_Text;
    private final TextView mMedicine_days;
    private final TextView mMedicine_clockTime;
    private final TextView mMedicine_dosage;

    private Medicine mMed;
    String dosage_list="",time_list="";
    enum day_and_count {Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday};

    public MedHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.fragment_single_medicine, parent, false));

        mMedicineName_Text = (TextView) itemView.findViewById(R.id.medicine_name);
        mMedicine_days = (TextView) itemView.findViewById(R.id.medicine_time);
        mMedicine_clockTime = (TextView)itemView.findViewById(R.id.medicine_clocktime);
        mMedicine_dosage = (TextView)itemView.findViewById(R.id.medicine_dosage);
    }



    public void bind(Medicine med) {
        mMed = med;
        dosage_list="";
        time_list="";
        mMedicineName_Text.setText(mMed.getMedName());
//        mMedicine_dosage.setText("Dosage: "+Float.toString(mMed.getDosage()));
//        Log.i("Dosagecheck","prinitng:"+Float.toString(mMed.getDosage()));
//        mMedicine_clockTime.setText("Time: "+Integer.toString(mMed.getHr())+":"+Integer.toString(mMed.getMin()));
//        Log.i("Timecheck","prinitng:"+Integer.toString(mMed.getHr())+":"+Integer.toString(mMed.getMin()));

        int length=mMed.getDosage().size();
        System.out.println("Length="+length);
        for(int i=0; i<length-1; i++){
            dosage_list+=mMed.getDosage().get(i)+", ";
            time_list+=mMed.getTime().get(i)+", ";
        }
        dosage_list+=mMed.getDosage().get(length-1);
        System.out.println("Dosage List: "+dosage_list);
        System.out.println("Time List: "+time_list);
        time_list+=mMed.getTime().get(length-1);
        mMedicine_dosage.setText(dosage_list);
        mMedicine_clockTime.setText(time_list);


        List<Boolean> k = mMed.getDays();
        int i=0;

        String str="";
        for(Boolean x: k){
            if(x) {
                switch(i){
                    case 0:
                        str+=" sun";
                        break;
                    case 1:
                        str+=" mon";
                        break;
                    case 2:
                        str+=" tues";
                        break;
                    case 3:
                        str+=" wed";
                        break;
                    case 4:
                        str+=" thur";
                        break;
                    case 5:
                        str+=" fri";
                        break;
                    case 6:
                        str+=" sat";
                        break;
                }
                //str+=Integer.toString(i)+"day ";
            }
            i++;
        }
        mMedicine_days.setText(str);
    }
}