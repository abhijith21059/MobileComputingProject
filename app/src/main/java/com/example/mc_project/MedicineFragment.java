package com.example.mc_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//for recycler view
public class MedicineFragment extends Fragment {

    FloatingActionButton mAddFab;

    public MedicineFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_medicine, container, false);

        mAddFab=view.findViewById(R.id.add_fab);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddFab.setVisibility(view.GONE);
                //mAddFab.setImageResource(R.drawable.ic_done);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment addMedicineFragment = fm.findFragmentById(R.id.fragment_container_addmed);
                //findFragmentById(R.id.fragment_container);
                if (addMedicineFragment == null) {
                    addMedicineFragment = new AddMedFragment();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, addMedicineFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }
}