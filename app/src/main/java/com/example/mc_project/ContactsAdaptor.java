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

public class ContactsAdaptor extends RecyclerView.Adapter<ContactsAdaptor.ContactsViewHolder> {

    private Context myContext;
    private ArrayList<String> contactnames;
    private OnNoteListener monNoteListener;
    private ArrayList<String> contactNumbers;

    public ContactsAdaptor(Context myContext, ArrayList<String> contactsList, OnNoteListener onNoteListener, ArrayList<String> contactNumbers){
        this.contactnames = contactsList;
        this.myContext = myContext;
        this.monNoteListener = onNoteListener;
        this.contactNumbers = contactNumbers;
    }
    public ContactsAdaptor(ArrayList<String> contactsList, ArrayList<String> contactNumbers){
        this.contactnames = contactsList;
        this.contactNumbers = contactNumbers;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(myContext).inflate(R.layout.row_design_contact, parent, false);
        return new ContactsViewHolder(rootview, monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.txtView1.setText(""+contactnames.get(position));
        holder.txtView2.setText(""+contactNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return contactnames.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtView1, txtView2;
        OnNoteListener onNoteListener;
        public ContactsViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.onNoteListener = onNoteListener;
            txtView1 = itemView.findViewById(R.id.titleView1);
            txtView2 = itemView.findViewById(R.id.titleView2);
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
