package com.example.mc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context myContext;
    private ArrayList<String> imgList;

    public ReportAdapter(Context myContext, ArrayList<String> imgList){
        this.imgList = imgList;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(myContext).inflate(R.layout.report_items, parent, false);
        return new ReportViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Glide.with(myContext).load(imgList.get(position)).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgViewRep);
        }
    }
}
