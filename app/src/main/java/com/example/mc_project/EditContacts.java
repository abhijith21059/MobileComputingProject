package com.example.mc_project;

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
import java.util.Objects;

public class EditContacts extends AppCompatActivity {
    private EditText editName, editNumber;
    private Button updateButton, delButton;
    String name;
    ArrayList<ContactModel> emergencyContacts;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);
        editName = findViewById(R.id.contactName);
        editNumber = findViewById(R.id.number);
        updateButton = findViewById(R.id.button);
        delButton = findViewById(R.id.delButton);
        emergencyContacts = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            number = bundle.getString("number");
            Log.d("EditContacts.class", "Name: " +name+ "Number: " +number);
        }
        editName.setText(name);
        editNumber.setText(number);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("emergency contacts");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            if (Objects.requireNonNull(postSnapshot.child("name").getValue(String.class)).equalsIgnoreCase(name)) {
                                ContactModel cm = new ContactModel(editName.getText().toString(), editNumber.getText().toString());
                                emergencyContacts.add(cm);
                            } else {
                                ContactModel cm = new ContactModel(postSnapshot.child("name").getValue(String.class), postSnapshot.child("phoneNumber").getValue(String.class));

                                emergencyContacts.add(cm);
                            }
                        }

                        ref.setValue(emergencyContacts);

                        Toast.makeText(EditContacts.this, "Successfully updated emergency contact ", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditContacts.this, "Fail to update data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("emergency contacts");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            if (Objects.requireNonNull(postSnapshot.child("name").getValue(String.class)).equalsIgnoreCase(name)) {
                                postSnapshot.getRef().removeValue();
                            } else {
                                ContactModel cm = new ContactModel(postSnapshot.child("name").getValue(String.class), postSnapshot.child("phoneNumber").getValue(String.class));
                                emergencyContacts.add(cm);
                            }
                        }

                        ref.setValue(emergencyContacts);

                        Toast.makeText(EditContacts.this, "Successfully updated emergency contact ", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditContacts.this, "Fail to update data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}