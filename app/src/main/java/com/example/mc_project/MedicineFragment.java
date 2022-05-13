package com.example.mc_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

//for recycler view
public class MedicineFragment extends Fragment {

    private static final String DEBUG_TAG = "MedicineFragment";
    private RecyclerView mMedRecyclerView;
    DatabaseReference ref;
    private List medicineDataList=new ArrayList<>();
    private MedicineAdapter medicineAdapter;

    Boolean undo=false;
    FloatingActionButton mAddFab;
    TextView mssg;

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

        mssg = view.findViewById(R.id.TVmssg);

//        Toolbar myToolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        mMedRecyclerView = (RecyclerView)view.findViewById(R.id.med_recycler_view);
        mMedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ref=FirebaseDatabase.getInstance().getReference();

        medicineDataPrepare();

        Log.i(DEBUG_TAG, "line 48" );
        medicineAdapter=new MedicineAdapter(medicineDataList);
        messageDisplay();

        Log.i(DEBUG_TAG, "line 50" );
        mMedRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mMedRecyclerView.setAdapter(medicineAdapter);




        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Medicine toDelete = (Medicine) medicineDataList.get(viewHolder.getAdapterPosition());

                int position = viewHolder.getAdapterPosition();
                System.out.println("Line 97:"+position);
                medicineDataList.remove(viewHolder.getAdapterPosition());
                medicineAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
//                Snackbar.make(mMedRecyclerView,toDelete.getMedName(),Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        undo=true;
//                        medicineDataList.add(position,toDelete);
//                        medicineAdapter.notifyItemInserted(position);
//                    }
//                }).show();

                Snackbar.make(mMedRecyclerView,toDelete.getMedName(),Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
//                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION){
                            // Snackbar closed on its own
                            System.out.println("Here line111");
                            String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference deleteEntry = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("medicines").child("medicine_"+toDelete.getMedName());
        //                    System.out.println("Here at line 114.."+deleteEntry.toString());
                            deleteEntry.removeValue();
                            messageDisplay();
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                }).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        undo=true;
                        medicineDataList.add(position,toDelete);
                        medicineAdapter.notifyItemInserted(position);
                    }
                }).show();

//                if (!undo) {
//                    System.out.println("Here line111");
//                    String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    DatabaseReference deleteEntry = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("medicines").child("medicine_"+toDelete.getMedName());
////                    System.out.println("Here at line 114.."+deleteEntry.toString());
//                    deleteEntry.removeValue();
////                    medicineAdapter.notifyDataSetChanged();
//
//                }
                medicineAdapter.notifyDataSetChanged();
                messageDisplay();

            }
        }).attachToRecyclerView(mMedRecyclerView);
        System.out.println("Here at line 120");

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

    private void messageDisplay(){
        if(medicineAdapter.getItemCount()==0){
            mssg.setVisibility(View.VISIBLE);
        }
        else{
            mssg.setVisibility(View.GONE);
        }
    }

    private void medicineDataPrepare() {

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("medicine");
        String User = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(User).child("medicines");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i("DataSnapshot","exists");
                }
                else{
                    Log.i("DataSnapshot","does not exists");
                }
                medicineDataList.clear();


                for(DataSnapshot snapshot1:dataSnapshot.getChildren()){                     // for every med

                    String x=snapshot1.child("medName").getValue().toString();
                    List y= (List) snapshot1.child("days").getValue();
                    List dose = (List) snapshot1.child("dosage").getValue();
                    List time = (List) snapshot1.child("time").getValue();
                    Log.i("Debug","testing"+x);
                    Log.i("Debug","testing"+y.toString());

                    Medicine m=new Medicine(x.toString());
                    m.setDays(y);
                    m.setDosage(dose);
                    m.setTime(time);

                    medicineDataList.add(m);
                }
                medicineAdapter.notifyDataSetChanged();
                messageDisplay();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DBERROR","printing error"+error.getMessage());
            }
        });
    }
}