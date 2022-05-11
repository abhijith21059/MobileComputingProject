package com.example.mc_project;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddContact extends AppCompatActivity {
    private EditText name, contact;
    private Button submit;
    int size;
    private List<ContactModel> emergencyContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        name = findViewById(R.id.fullname);
        contact = findViewById(R.id.contactNumber);
        submit = findViewById(R.id.submitButton);
        emergencyContacts = new ArrayList<>();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO database storage
                ContactModel contactModel = new ContactModel(name.getText().toString(), contact.getText().toString());
               // emergencyContacts.add(contactModel);
                String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("emergency contacts");
                //DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("contacts count");


                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Log.d("AddContact.java", "Snapspt children: "+postSnapshot);
                            ContactModel cm = new ContactModel(postSnapshot.child("name").getValue(String.class), postSnapshot.child("phoneNumber").getValue(String.class));
                            emergencyContacts.add(cm);
                        }
                        emergencyContacts.add(contactModel);
                        ref1.setValue(emergencyContacts);

                        Toast.makeText(AddContact.this, "Successfully added emergency contact ", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddContact.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}