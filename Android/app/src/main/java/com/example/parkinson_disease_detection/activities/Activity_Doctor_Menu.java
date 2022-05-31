package com.example.parkinson_disease_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.fragments.doctor.Fragment_Doctor_Profile;
import com.example.parkinson_disease_detection.fragments.doctor.Fragment_Doctor_Results;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.utils.Constants;
import com.example.parkinson_disease_detection.utils.MySP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Activity_Doctor_Menu extends AppCompatActivity {
    private BottomNavigationView menu_doctor_NVG_bottomNavigation;
    private Fragment selectedFragment;

    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_menu);

        findViews();
        setDoctor();
    }

    private void findViews() {
        menu_doctor_NVG_bottomNavigation = findViewById(R.id.menu_doctor_NVG_bottomNavigation);
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
        replaceFragment(R.id.menu_doctor_LBL_profile);

        menu_doctor_NVG_bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                replaceFragment(item.getItemId());
                return true;
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

    private void replaceFragment(int id) {
        int itemId = 0;

        switch (id) {
            case R.id.menu_doctor_LBL_profile:
                itemId = R.id.menu_doctor_LBL_profile;
                selectedFragment = new Fragment_Doctor_Profile();
                break;
            case R.id.menu_doctor_LBL_results:
                itemId = R.id.menu_doctor_LBL_results;
                selectedFragment = new Fragment_Doctor_Results();
                break;
            case R.id.menu_doctor_LBL_signOut:
                signOut();
                break;
        }

        if (itemId != 0) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DOCTOR, new Gson().toJson(doctor));
            initFragments(selectedFragment, bundle);
        }
    }

    private void initFragments(Fragment selectedFragment, Bundle bundle) {
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_doctor_FRG_selectedFragment, selectedFragment)
                .commit();
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
