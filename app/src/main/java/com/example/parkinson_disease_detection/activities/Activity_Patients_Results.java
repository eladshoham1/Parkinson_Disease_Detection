package com.example.parkinson_disease_detection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.fragments.patient.Fragment_Patient_Results;
import com.example.parkinson_disease_detection.utils.Constants;

public class Activity_Patients_Results extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_results);
        initViews();
    }

    private void initViews() {
        Fragment_Patient_Results fragment = new Fragment_Patient_Results();
        Bundle bundle = new Bundle();
        String uid = getIntent().getStringExtra(Constants.USER_ID);
        if (uid != null) {
            bundle.putString(Constants.USER_ID, uid);
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.patients_results_FRG_results, fragment)
                    .commit();
        }
    }
}
