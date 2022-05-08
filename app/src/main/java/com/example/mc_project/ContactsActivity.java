package com.example.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements ContactsAdaptor.OnNoteListener {
    private RecyclerView recyclerView;
    private ArrayList<String> dataHolders;
    private FloatingActionButton addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addContact = (FloatingActionButton)findViewById(R.id.add_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(new ContactsAdaptor(dataHolders, this));

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, AddContact.class));
            }
        });
    }

    @Override
    public void onNoteClick(int position) {

    }
}