package com.example.parkinson_disease_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Doctor;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.User;
import com.example.parkinson_disease_detection.utils.Constants;
import com.example.parkinson_disease_detection.utils.MySP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Activity_Sign_In extends AppCompatActivity {
    private EditText signIn_EDT_email;
    private EditText signIn_EDT_password;
    private Button signIn_BTN_signIn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        findViews();
        initViews();
    }
    
    private void findViews() {
        signIn_EDT_email = findViewById(R.id.signIn_EDT_email);
        signIn_EDT_password = findViewById(R.id.signIn_EDT_password);
        signIn_BTN_signIn = findViewById(R.id.signIn_BTN_signIn);
    }

    private void initViews() {
        signIn_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = signIn_EDT_email.getText().toString();
        String password = signIn_EDT_password.getText().toString();
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Activity_Sign_In.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                getCurrentUser();
            }
        });
    }

    private String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            return firebaseUser.getUid();
        }

        return null;
    }

    private void getCurrentUser() {
        String uid = getCurrentUserId();
        if (uid == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference(Constants.USERS_DB).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = null;
                    String type = task.getResult().child("type").getValue(String.class);
                    if (type != null) {
                        if (type.equals(User.Type.PATIENT.toString())) {
                            user = task.getResult().getValue(Patient.class);
                        } else {
                            user = task.getResult().getValue(Doctor.class);
                        }
                        if (user != null) {
                            MySP.getInstance().putString(MySP.KEYS.USER_ID, uid);
                            MySP.getInstance().putString(MySP.KEYS.USER_TYPE, user.getType().toString());
                            moveToMenu(user);
                        }
                    }
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
            startActivity(intent);
            finish();
        }
    }
}