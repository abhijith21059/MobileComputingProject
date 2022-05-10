package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity  implements ContactsAdaptor.OnNoteListener {
    private RecyclerView recyclerView;
    private ArrayList<String> dataHolders;
    private ArrayList<String> contactNumbers;
    private FloatingActionButton addContact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        dataHolders = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addContact = (FloatingActionButton)findViewById(R.id.add_contact);
        contactNumbers = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(true);
        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("emergency contacts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataHolders.clear();
                contactNumbers.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d("ContactsActivity.java", "Snapspt children: "+postSnapshot.child("name").getValue(String.class));
                    Log.d("ContactsActivity.java", "Snapspt children: "+postSnapshot.child("phoneNumber").getValue(String.class));
                   // ContactModel cm = new ContactModel(postSnapshot.child("name").getValue(String.class), postSnapshot.child("phoneNumber").getValue(String.class));
                    dataHolders.add(postSnapshot.child("name").getValue(String.class));
                    contactNumbers.add(postSnapshot.child("phoneNumber").getValue(String.class));
                }
                Log.d("ContactsActivity.class", "Size: "+dataHolders.size());

                recyclerView.setAdapter(new ContactsAdaptor(getApplicationContext(), dataHolders, ContactsActivity.this, contactNumbers));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactsActivity.this, "Failed to get data " + error, Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("ContactsActivity.class", "Size: "+dataHolders.size());
       // recyclerView.setAdapter(new ContactsAdaptor(this, dataHolders));

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, AddContact.class));
            }
        });
    }
    @Override
    public void onNoteClick(int position) {
        Bundle args = new Bundle();
        Intent i = new Intent(ContactsActivity.this, EditContacts.class);
        args.putString("name", dataHolders.get(position));
        args.putString("number", contactNumbers.get(position));
        i.putExtras(args);
        startActivity(i);
    }



}