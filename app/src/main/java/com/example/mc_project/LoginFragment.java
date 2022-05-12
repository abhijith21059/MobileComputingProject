package com.example.mc_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText email, password;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private TextView registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        email = v.findViewById(R.id.email1);
        password = v.findViewById(R.id.password1);
        loginButton = v.findViewById(R.id.loginButton);
        progressBar = v.findViewById(R.id.progressBar2);
        registerButton = v.findViewById(R.id.registerHere);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
//            startActivity();
//            Log.i("Hellooo----->",user.);

            Intent i = new Intent(getContext(), Dashborad.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        } else {
            // User is signed out
            Log.d("TAG", "onAuthStateChanged:signed_out");

        }

        return v;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                String myEmail = email.getText().toString().trim();
                String myPassword = password.getText().toString().trim();

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

                firebaseAuth.signInWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginFragment.class", "onComplete: LOGIN SUCCESSFUL");
                            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                            //START LOGIN DASHBOARD
//                            startActivity(new Intent(getContext(), MainActivity.class));
                            startActivity(new Intent(getContext(), Dashborad.class));
                            getActivity().finish();


                        } else {
                            Log.d("LoginFragment.class", "onComplete: LOGIN UNSUCCESSFUL");
                            Toast.makeText(getContext(), "Unsuccessful Login: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });

                break;

            case R.id.registerHere:
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RegistrationFragment()).commit();
                break;

        }
    }

    //todo--remove loader on back press from dashboard
}