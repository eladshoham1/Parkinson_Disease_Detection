package com.example.parkinson_disease_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.User;
import com.example.parkinson_disease_detection.utils.Constants;
import com.example.parkinson_disease_detection.utils.MySP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Objects;

public class Activity_Sign_Up extends AppCompatActivity {
    private EditText signUp_EDT_fullName;
    private EditText signUp_EDT_email;
    private EditText signUp_EDT_password;
    private RadioGroup signUp_GRP_type;
    private RadioButton signUp_BTN_patient;
    private RadioButton signUp_BTN_doctor;
    private Button signUp_BTN_signUp;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }

    private void findViews() {
        signUp_EDT_fullName = findViewById(R.id.signUp_EDT_fullName);
        signUp_EDT_email = findViewById(R.id.signUp_EDT_email);
        signUp_EDT_password = findViewById(R.id.signUp_EDT_password);
        signUp_GRP_type = findViewById(R.id.signUp_GRP_type);
        signUp_BTN_patient = findViewById(R.id.signUp_BTN_patient);
        signUp_BTN_doctor = findViewById(R.id.signUp_BTN_doctor);
        signUp_BTN_signUp = findViewById(R.id.signUp_BTN_signUp);
    }

    private void initViews() {
        signUp_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String fullName = signUp_EDT_fullName.getText().toString();
        String email = signUp_EDT_email.getText().toString();
        String password = signUp_EDT_password.getText().toString();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty credentials!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password too short!", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(fullName, email, password);
        }
    }

    private void registerUser(String fullName, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    User user;

                    if (signUp_GRP_type.getCheckedRadioButtonId() == signUp_BTN_patient.getId()) {
                        user = new Patient(uid, fullName, email);
                    } else {
                        user = new Doctor(uid, fullName, email);
                    }

                    FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Activity_Sign_Up.this, "Registering user successful!", Toast.LENGTH_SHORT).show();
                                moveToMenu(user);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Activity_Sign_Up.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveToMenu(User user) {
        Intent intent = null;
        if (user instanceof Patient) {
            intent = new Intent(this, Activity_Patient_Menu.class);
            intent.putExtra(Constants.PATIENT, new Gson().toJson(user));
        } else if (user instanceof Doctor) {
            intent = new Intent(this, Activity_Doctor_Menu.class);
            intent.putExtra(Constants.DOCTOR, new Gson().toJson(user));
        }
        if (intent != null) {
            MySP.getInstance().putString(MySP.KEYS.USER_TYPE, user.getType().toString());
            startActivity(intent);
            finish();
        }
    }

    /*private void moveToMenu() {
        Intent intent = new Intent(this, Activity_Patient_Menu.class);
        startActivity(intent);
        finish();
    }*/
}