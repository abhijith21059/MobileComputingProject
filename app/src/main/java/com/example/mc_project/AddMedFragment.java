package com.example.mc_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddMedFragment extends Fragment {



    public AddMedFragment() {
        // Required empty public constructor
    }


    public static AddMedFragment newInstance(String param1, String param2) {
        AddMedFragment fragment = new AddMedFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_med, container, false);
    }
}