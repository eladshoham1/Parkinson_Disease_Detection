package com.example.parkinson_disease_detection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.fragments.patient.Fragment_Patient_Profile;
import com.example.parkinson_disease_detection.fragments.patient.Fragment_Patient_Results;
import com.example.parkinson_disease_detection.fragments.patient.Fragment_Patient_Test;
import com.example.parkinson_disease_detection.models.Patient;
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

public class Activity_Patient_Menu extends AppCompatActivity {
    private BottomNavigationView menu_patient_NVG_bottomNavigation;
    private Fragment selectedFragment;

    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_menu);
        findViews();
        initViews();
    }

    private void findViews() {
        menu_patient_NVG_bottomNavigation = findViewById(R.id.menu_patient_NVG_bottomNavigation);
    }

    private void initViews() {
        String patientString = getIntent().getStringExtra(Constants.PATIENT);
        String uid = getIntent().getStringExtra(Constants.USER_ID);
        if (patientString != null && !patientString.isEmpty()) {
            patient = new Gson().fromJson(patientString, Patient.class);
            replaceFragment(R.id.menu_patient_LBL_profile);
        } else if (uid != null && !uid.isEmpty()){
            getPatientFromDB(uid);
        }

        menu_patient_NVG_bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                replaceFragment(item.getItemId());
                return true;
            }
        });
    }

    private void getPatientFromDB(String uid) {
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    patient = task.getResult().getValue(Patient.class);
                    replaceFragment(R.id.menu_patient_LBL_profile);
                }
            }
        });
    }

    private void replaceFragment(int id) {
        int itemId = 0;

        switch (id) {
            case R.id.menu_patient_LBL_profile:
                itemId = R.id.menu_patient_LBL_profile;
                selectedFragment = new Fragment_Patient_Profile();
                break;
            case R.id.menu_patient_LBL_test:
                itemId = R.id.menu_patient_LBL_test;
                selectedFragment = new Fragment_Patient_Test();
                break;
            case R.id.menu_patient_LBL_results:
                itemId = R.id.menu_patient_LBL_results;
                selectedFragment = new Fragment_Patient_Results();
                break;
            case R.id.menu_patient_LBL_signOut:
                signOut();
                break;
        }

        if (itemId != 0) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PATIENT, new Gson().toJson(patient));
            initFragments(selectedFragment, bundle);
        }
    }

    private void initFragments(Fragment selectedFragment, Bundle bundle) {
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_patient_FRG_selectedFragment, selectedFragment)
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
