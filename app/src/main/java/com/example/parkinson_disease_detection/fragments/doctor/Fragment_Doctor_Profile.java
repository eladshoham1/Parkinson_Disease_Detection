package com.example.parkinson_disease_detection.fragments.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.gson.Gson;

public class Fragment_Doctor_Profile extends Fragment {
    private TextView profile_doctor_LBL_fullName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        profile_doctor_LBL_fullName = view.findViewById(R.id.profile_doctor_LBL_fullName);
    }

    private void initViews() {
        if (getArguments() != null) {
            String doctorString = getArguments().getString(Constants.DOCTOR, "");
            if (!doctorString.isEmpty()) {
                Doctor doctor = new Gson().fromJson(doctorString, Doctor.class);
                profile_doctor_LBL_fullName.setText("Welcome, " + doctor.getFullName());
            }
        }
    }
}
