package com.example.mc_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//for recycler view
public class MedicineFragment extends Fragment {

    private static final String DEBUG_TAG = "MedicineFragment";
    private RecyclerView mMedRecyclerView;
    private List medicineDataList=new ArrayList<>();
    private MedicineAdapter medicineAdapter;

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

        mMedRecyclerView = (RecyclerView)view.findViewById(R.id.med_recycler_view);
        mMedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        medicineDataPrepare();
        Log.i(DEBUG_TAG, "line 48" );
        medicineAdapter=new MedicineAdapter(medicineDataList);
        Log.i(DEBUG_TAG, "line 50" );
        mMedRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mMedRecyclerView.setAdapter(medicineAdapter);




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

    private void medicineDataPrepare() {
        Medicine data= new Medicine();
        medicineDataList.add(data);

        data= new Medicine();
        medicineDataList.add(data);

        data= new Medicine();
        medicineDataList.add(data);
    }





}