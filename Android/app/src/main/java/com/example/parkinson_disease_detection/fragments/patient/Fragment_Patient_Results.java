package com.example.parkinson_disease_detection.fragments.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.activities.Activity_Result;
import com.example.parkinson_disease_detection.adapter.Adapter_Result;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.Result;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Patient_Results extends Fragment {
    private RecyclerView fragment_patient_LST_allResults;
    private Adapter_Result adapter_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_results, container, false);
        findViews(view);
        getUser();

        return view;
    }

    private void findViews(View view) {
        fragment_patient_LST_allResults = view.findViewById(R.id.fragment_patient_LST_allResults);
    }

    private void getUser() {
        if (getArguments() != null) {
            String patientString = getArguments().getString(Constants.PATIENT, "");
            String uid = getArguments().getString(Constants.USER_ID, "");
            if (!patientString.isEmpty()) {
                Patient patient = new Gson().fromJson(patientString, Patient.class);
                getAllResults(patient.getUid());
            } else if (!uid.isEmpty()) {
                getAllResults(uid);
            }
        }
    }

    private void getAllResults(String uid) {
        FirebaseDatabase.getInstance().getReference(Constants.RESULTS_DB).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    List<Result> allResults = new ArrayList<>();
                    for (DataSnapshot resultsSnapshot : task.getResult().getChildren()) {
                        allResults.add(0, resultsSnapshot.getValue(Result.class));
                    }
                    initViews(allResults);
                }
            }
        });
    }

    private void initViews(List<Result> allResults) {
        fragment_patient_LST_allResults.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_result = new Adapter_Result(getContext(), allResults);
        adapter_result.setClickListener(new Adapter_Result.ItemClickListener() {
            @Override
            public void onItemClickResults(View view, int position) {
                openResult(allResults.get(position));
            }
        });
        fragment_patient_LST_allResults.setAdapter(adapter_result);
    }

    private void openResult(Result result) {
        Intent intent = new Intent(getContext(), Activity_Result.class);
        intent.putExtra(Constants.RESULT, new Gson().toJson(result));
        startActivity(intent);
    }
}