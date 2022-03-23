package com.example.mc_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        medicineDataList.clear();
        Medicine data= new Medicine();
        data.MedName+="1";

        medicineDataList.add(data);

        data= new Medicine();
        data.MedName+="2";
        medicineDataList.add(data);

        data= new Medicine();
        data.MedName+="3";
        medicineDataList.add(data);



//        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("medicine");
//        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(User).child("medicine");
//        Toast.makeText(getContext(), "printing:"+ref.child("med1"), Toast.LENGTH_SHORT).show();
////        uid=User.getUid()
//        Toast.makeText(getContext(), "printing:"+User, Toast.LENGTH_SHORT).show();
//        //FirebaseAuth.getInstance().getCurrentUser().getUid()
//
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    Log.i("DataSnapshot","exists");
//                }
//                else{
//                    Log.i("DataSnapshot","does not exists");
//                }
//                medicineDataList.clear();
//
//                for(DataSnapshot snapshot1:dataSnapshot.getChildren()){
////                    Log.i("children","med available"+snapshot1.getValue().toString());
//                    medicineDataList.add(snapshot1.getValue().toString());
//                }
//                medicineAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("DBERROR","printing error"+error.getMessage());
//            }
//        });
    }





}