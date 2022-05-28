package com.example.parkinson_disease_detection.fragments.patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.User;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Patient_Profile extends Fragment {
    private TextView profile_patient_LBL_fullName;
    private TextView profile_patient_LBL_doctor;
    private Spinner profile_patient_SPN_doctors;
    private Button profile_patient_BTN_setDoctor;

    private Patient patient;
    private List<User> doctors;
    private List<String> doctorsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        profile_patient_LBL_fullName = view.findViewById(R.id.profile_patient_LBL_fullName);
        profile_patient_LBL_doctor = view.findViewById(R.id.profile_patient_LBL_doctor);
        profile_patient_SPN_doctors = view.findViewById(R.id.profile_patient_SPN_doctors);
        profile_patient_BTN_setDoctor = view.findViewById(R.id.profile_patient_BTN_setDoctor);
    }

    private void initViews() {
        if (getArguments() != null) {
            String patientString = getArguments().getString(Constants.PATIENT, "");
            if (!patientString.isEmpty()) {
                patient = new Gson().fromJson(patientString, Patient.class);
                profile_patient_LBL_fullName.setText("Welcome, " + patient.getFullName());
                setDoctorNameById(patient.getDoctor());
                initDoctors();
                profile_patient_BTN_setDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDoctor();
                    }
                });
            }
        }
    }

    private void setDoctorNameById(String uid) {
        if (uid == null) {
            return;
        }

        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Doctor doctor = task.getResult().getValue(Doctor.class);
                    profile_patient_LBL_doctor.setText("Your doctor is " + doctor.getFullName());
                }
            }
        });
    }

    private void initDoctors() {
        doctors = new ArrayList<>();
        doctorsNames = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    doctorsNames.add(0, "");
                    for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user.getType() == User.Type.DOCTOR) {
                            doctors.add(user);
                            doctorsNames.add(user.getFullName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, doctorsNames);
                    profile_patient_SPN_doctors.setAdapter(adapter);
                }
            }
        });
    }

    private void setDoctor() {
        int position = profile_patient_SPN_doctors.getSelectedItemPosition() - 1;
        if (position == -1) {
            return;
        }
        String doctorId = doctors.get(position).getUid();
        patient.setDoctor(doctorId);
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(patient.getUid()).setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    addPatientToDoctor(doctorId);
                }
            }
        });
    }

    private void addPatientToDoctor(String doctorId) {
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(doctorId).child(Constants.PATIENTS_DB).child(patient.getUid()).setValue(patient.getFullName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "The doctor has been successfully updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
