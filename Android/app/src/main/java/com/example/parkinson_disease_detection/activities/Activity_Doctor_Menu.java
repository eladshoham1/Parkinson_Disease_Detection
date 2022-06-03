package com.example.parkinson_disease_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.fragments.doctor.Fragment_Doctor_Results;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.utils.Constants;
import com.example.parkinson_disease_detection.utils.MySP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Activity_Doctor_Menu extends AppCompatActivity {
    private TextView doctor_menu_LBL_doctorName;
    private ImageView doctor_menu_IMG_signOut;
    private Fragment_Doctor_Results fragment_doctor_results;

    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_menu);

        findViews();
        setDoctor();
    }

    private void findViews() {
        doctor_menu_LBL_doctorName = findViewById(R.id.doctor_menu_LBL_doctorName);
        doctor_menu_IMG_signOut = findViewById(R.id.doctor_menu_IMG_signOut);
    }

    private void setDoctor() {
        String doctorString = getIntent().getStringExtra(Constants.DOCTOR);
        String uid = getIntent().getStringExtra(Constants.USER_ID);
        if (doctorString != null && !doctorString.isEmpty()) {
            doctor = new Gson().fromJson(doctorString, Doctor.class);
            setPatients();
        } else if (uid != null && !uid.isEmpty()) {
            getDoctorFromDB(uid);
        }
    }

    private void setPatients(){
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(doctor.getUid()).child(Constants.PATIENTS_DB).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot patientSnapshot : task.getResult().getChildren()) {
                        doctor.addPatient(patientSnapshot.getKey(), patientSnapshot.getValue(String.class));
                    }
                    initViews();
                }

            }
        });
    }

    private void initViews() {
        doctor_menu_LBL_doctorName.setText("Welcome, " + doctor.getFullName());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DOCTOR, new Gson().toJson(doctor));
        fragment_doctor_results = new Fragment_Doctor_Results();
        fragment_doctor_results.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.doctor_menu_FRG_results, fragment_doctor_results)
                .commit();
        doctor_menu_IMG_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void getDoctorFromDB(String uid) {
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    doctor = task.getResult().getValue(Doctor.class);
                    setPatients();
                }
            }
        });
    }

    private void signOut() {
        MySP.getInstance().removeKey(MySP.KEYS.USER_ID);
        MySP.getInstance().removeKey(MySP.KEYS.USER_TYPE);
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
        moveToMain();
    }

    private void moveToMain() {
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
        finish();
    }
}
