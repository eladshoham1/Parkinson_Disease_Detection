package com.example.parkinson_disease_detection.fragments.patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.activities.Activity_Result;
import com.example.parkinson_disease_detection.models.DrawingView;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.Result;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class Fragment_Patient_Test extends Fragment {
    private DrawingView test_DRW_spiral;
    private Button test_BTN_classify;
    private ProgressBar test_PRB_loading;

    private Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_test, container, false);
        findViews(view);
        initViews();
        setPatient();

        return view;
    }

    private void findViews(View view) {
        test_DRW_spiral = view.findViewById(R.id.test_DRW_spiral);
        test_BTN_classify = view.findViewById(R.id.test_BTN_classify);
        test_PRB_loading = view.findViewById(R.id.test_PRB_loading);
    }

    private void initViews() {
        test_BTN_classify.setOnClickListener(v -> classify());
    }

    private void setPatient() {
        if (getArguments() != null) {
            String patientString = getArguments().getString(Constants.PATIENT, "");
            if (!patientString.isEmpty()) {
                patient = new Gson().fromJson(patientString, Patient.class);
            }
        }
    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String convertUriToUrl(String uri) {
        String[] firstParts = uri.split("/");
        String[] secondParts = firstParts[7].split("%");
        String[] thirdParts = secondParts[2].split("alt=");
        String[] fourthParts = thirdParts[1].split("=");
        String id = secondParts[1];
        String name = thirdParts[0].substring(0, thirdParts[0].length() - 1);
        String token = fourthParts[1];
        return Constants.CONVOLUTIONAL_NEURAL_NETWORK_URL + "/?id=" + id + "&name=" + name + "&token=" + token;
    }

    private void classify() {
        test_PRB_loading.setVisibility(View.VISIBLE);
        test_BTN_classify.setVisibility(View.GONE);
        Bitmap bitmap = Bitmap.createBitmap(test_DRW_spiral.getWidth(), test_DRW_spiral.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        test_DRW_spiral.draw(canvas);
        saveSpiralImageInDB(getBytes(bitmap));
    }

    private void saveSpiralImageInDB(byte[] data) {
        long currentTime = System.currentTimeMillis();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB).child(patient.getUid()).child(String.valueOf(currentTime));
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            setResult(task.getResult().toString(), currentTime);
                        }
                    }
                });
            }
        });
    }

    private void setResult(String uri, long currentTime) {
        String url = convertUriToUrl(uri);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Result result = new Result(patient.getUid(), patient.getFullName(), currentTime, response.substring(1, response.length() - 2), uri);
                FirebaseDatabase.getInstance().getReference(Constants.RESULTS_DB).child(patient.getUid()).child(String.valueOf(result.getTime())).setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            moveToResult(result);
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(stringRequest);
    }

    private void moveToResult(Result result) {
        Intent intent = new Intent(getContext(), Activity_Result.class);
        intent.putExtra(Constants.RESULT, new Gson().toJson(result));
        startActivity(intent);
        getActivity().finish();
    }
}