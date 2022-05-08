package com.example.mc_project;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;

public class ContactsAdaptor extends RecyclerView.Adapter<ContactsAdaptor.ViewHolder> {
    ArrayList<String> data;
    private OnNoteListener monNoteListener;

    public ContactsAdaptor(ArrayList<String> data, OnNoteListener onNoteListener) {
        this.data = data;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_design_contact, parent, false);

        return new ViewHolder(view, monNoteListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView1;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.onNoteListener = onNoteListener;
            Resources res = itemView.getResources();
            Drawable shape = res.getDrawable(R.drawable.gradient_box);
            textView1 = itemView.findViewById(R.id.titleView1);
            textView1.setBackground(shape);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}

