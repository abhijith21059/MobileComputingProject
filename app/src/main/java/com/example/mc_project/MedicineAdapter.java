package com.example.mc_project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedHolder> {

    private final List<Medicine> mMedicine;

    public MedicineAdapter(List<Medicine> items) {
        mMedicine = items;
    }

    @NonNull
    @Override
    public MedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MedHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MedHolder holder, int position) {
        Medicine med= mMedicine.get(position);
        holder.bind(med);
    }

    @Override
    public int getItemCount() {
        Log.i("DEBUG","size="+mMedicine.size());
        return mMedicine.size();
    }
}
