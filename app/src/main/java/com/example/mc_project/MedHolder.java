package com.example.mc_project;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MedHolder extends RecyclerView.ViewHolder {

    private final TextView mMedicineName_Text;
    private Medicine mMed;

    public MedHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.fragment_single_medicine, parent, false));

        mMedicineName_Text = (TextView) itemView.findViewById(R.id.medicine_name);
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
        mMedicineName_Text.setText(mMed.MedName);
    }
}