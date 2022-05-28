package com.example.parkinson_disease_detection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.User;
import com.example.parkinson_disease_detection.utils.Constants;
import com.example.parkinson_disease_detection.utils.MySP;

public class Activity_Main extends AppCompatActivity {
    private Button main_BTN_signIn;
    private Button main_BTN_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = MySP.getInstance().getString(MySP.KEYS.USER_ID, "");
        if (!uid.isEmpty()) {
            String type = MySP.getInstance().getString(MySP.KEYS.USER_TYPE, "");
            if (!type.isEmpty()){
                moveToMenu(uid,type);
            }
        }
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }

    private void findViews() {
        main_BTN_signIn = findViewById(R.id.main_BTN_signIn);
        main_BTN_signUp = findViewById(R.id.main_BTN_signUp);
    }

    private void initViews() {
        main_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        main_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void moveToMenu(String uid,String type) {
        Intent intent = null;
        if (type.equals(User.Type.PATIENT.toString())) {
            intent = new Intent(this, Activity_Patient_Menu.class);
        } else if (type.equals(User.Type.DOCTOR.toString())) {
            intent = new Intent(this, Activity_Doctor_Menu.class);
        }
        if (intent != null) {
            intent.putExtra(Constants.USER_ID,uid);
            startActivity(intent);
            finish();
        }
    }


    private void signIn() {
        Intent intent = new Intent(this, Activity_Sign_In.class);
        startActivity(intent);
        finish();
    }

    private void signUp() {
        Intent intent = new Intent(this, Activity_Sign_Up.class);
        startActivity(intent);
        finish();
    }
}