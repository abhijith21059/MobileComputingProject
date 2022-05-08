package com.example.mc_project;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;

public class ContactsAdaptor extends RecyclerView.Adapter<ContactsAdaptor.ReportViewHolder> {

    private Context myContext;
    private ArrayList<String> contactsList;

    public ContactsAdaptor(Context myContext, ArrayList<String> contactsList){
        this.contactsList = contactsList;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(myContext).inflate(R.layout.row_design_contact, parent, false);
        return new ReportViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.txtView.setText(""+contactsList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder{
        TextView txtView;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.titleView1);
        }
    }
}
