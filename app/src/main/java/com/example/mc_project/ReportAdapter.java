package com.example.mc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context myContext;
    private ArrayList<String> imgList;
    TextView tvreport;

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
//        tvreport.setText("Report: "+position+1);

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView tvreport;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgViewRep);
            tvreport = itemView.findViewById(R.id.reportId);
            tvreport.setText("Report");

        }
    }
}
