package com.example.mc_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegistrationFragment extends Fragment {
    private EditText name, email, password, phone, address, city;
    private Button registerButton;
    private TextView loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_registration, container, false);
        name  = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        phone = v.findViewById(R.id.phoneNumber);
        address = v.findViewById(R.id.address);
        city = v.findViewById(R.id.city);
        loginButton = v.findViewById(R.id.loginHere);
        registerButton = v.findViewById(R.id.registerButton);
        progressBar = v.findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null) {
//            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment()).commit();
//            requireActivity().finish();
//        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myEmail = email.getText().toString().trim();
                String myPassword = password.getText().toString().trim();
                String myFullName = name.getText().toString().trim();
                String myPhone = phone.getText().toString().trim();
                String myAddress = address.getText().toString().trim();
                String myCity = city.getText().toString().trim();



                if (TextUtils.isEmpty(myEmail)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(myPassword)) {
                    password.setError("Password is required");
                    return;
                }

                if (myPassword.length() < 6) {
                    password.setError("Password must be greater than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Patients patient = new Patients(myFullName, myEmail, myPassword, myPhone, myAddress, myCity);
                            FirebaseDatabase.getInstance().getReference("Patients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(patient);

                            Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment()).commit();
                        } else {
                            Toast.makeText(getActivity(), "Unsuccessful: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment()).commit();
            }
        });

        return v;
    }
}