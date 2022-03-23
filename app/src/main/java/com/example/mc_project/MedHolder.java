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
    enum day_and_count {Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday};

    public MedHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.fragment_single_medicine, parent, false));

        mMedicineName_Text = (TextView) itemView.findViewById(R.id.medicine_name);
        mMedicine_days = (TextView) itemView.findViewById(R.id.medicine_time);
        mMedicine_clockTime = (TextView)itemView.findViewById(R.id.medicine_clocktime);
        mMedicine_dosage = (TextView)itemView.findViewById(R.id.medicine_dosage);
    }


//    @Override
//    public void onClick(View v) {
//        Toast.makeText(getActivity(),mNews.getId()+" clicked!", Toast.LENGTH_SHORT).show();
//
//        DetailsFragment fragment = new DetailsFragment();
//        Bundle args=new Bundle();
//        args.putSerializable("News",mNews);
//        fragment.setArguments(args);
//
//        SingleFragmentActivity s=(SingleFragmentActivity)v.getContext();
//        FragmentManager fm =s.getSupportFragmentManager();
//
//        fm.beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null)
//                .commit();
//    }

    public void bind(Medicine med) {
        mMed = med;

        mMedicineName_Text.setText(mMed.getMedName());
        mMedicine_dosage.setText("Dosage: "+Float.toString(mMed.getDosage()));
        Log.i("Dosagecheck","prinitng:"+Float.toString(mMed.getDosage()));
        mMedicine_clockTime.setText("Time: "+Integer.toString(mMed.getHr())+":"+Integer.toString(mMed.getMin()));
        Log.i("Timecheck","prinitng:"+Integer.toString(mMed.getHr())+":"+Integer.toString(mMed.getMin()));

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