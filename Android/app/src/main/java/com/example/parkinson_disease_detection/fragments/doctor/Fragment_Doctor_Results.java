package com.example.parkinson_disease_detection.fragments.doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.activities.Activity_Patients_Results;
import com.example.parkinson_disease_detection.adapter.Adapter_Patient;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Doctor_Results extends Fragment {
    private RecyclerView fragment_doctor_LST_allPatients;
    private Adapter_Patient adapter_patient;

    private Doctor doctor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_results, container, false);
        findViews(view);
        setUid();

        return view;
    }

    private void findViews(View view) {
        fragment_doctor_LST_allPatients = view.findViewById(R.id.fragment_doctor_LST_allPatients);
    }

    private void setUid() {
        if (getArguments() != null) {
            String doctorString = getArguments().getString(Constants.DOCTOR, "");
            if (!doctorString.isEmpty()) {
                doctor = new Gson().fromJson(doctorString, Doctor.class);
                List<String> patientsIds = new ArrayList<>(doctor.getPatients().keySet());
                List<String> patientsName = new ArrayList<>(doctor.getPatients().values());
                displayPatients(patientsIds, patientsName);
            }
        }
    }

    private void displayPatients(List<String> patientsIds, List<String> patientsName) {
        fragment_doctor_LST_allPatients.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_patient = new Adapter_Patient(getContext(), patientsName);
        adapter_patient.setClickListener(new Adapter_Patient.ItemClickListener() {
            @Override
            public void onItemClickResults(View view, int position) {
                openPatientResults(patientsIds.get(position));
            }
        });
        fragment_doctor_LST_allPatients.setAdapter(adapter_patient);
    }

    private void openPatientResults(String patientId) {
        Intent intent = new Intent(getContext(), Activity_Patients_Results.class);
        intent.putExtra(Constants.USER_ID, patientId);
        startActivity(intent);
    }
}
