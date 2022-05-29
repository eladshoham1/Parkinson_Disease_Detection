package com.example.parkinson_disease_detection.fragments.patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.activities.Activity_Result;
import com.example.parkinson_disease_detection.models.Classifier;
import com.example.parkinson_disease_detection.models.DrawingView;
import com.example.parkinson_disease_detection.models.Patient;
import com.example.parkinson_disease_detection.models.Point;
import com.example.parkinson_disease_detection.models.Result;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Patient_Test extends Fragment {
    private ImageView test_IMG_spiral;
    private DrawingView test_DRW_spiral;
    private Button test_BTN_classify;
    private ProgressBar test_PRB_loading;

    private List<Point> points;
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
        test_IMG_spiral = view.findViewById(R.id.test_IMG_spiral);
        test_DRW_spiral = view.findViewById(R.id.test_DRW_spiral);
        test_BTN_classify = view.findViewById(R.id.test_BTN_classify);
        test_PRB_loading = view.findViewById(R.id.test_PRB_loading);
    }

    private void initViews() {
        test_BTN_classify.setOnClickListener(v -> classify());

        points = new ArrayList<>();
        test_DRW_spiral.setOnTouchListener((v, event) -> {
            Point point = new Point(event.getX(), event.getY(), System.currentTimeMillis());
            points.add(point);
            return false;
        });
    }

    private void setPatient() {
        if (getArguments() != null) {
            String patientString = getArguments().getString(Constants.PATIENT, "");
            if (!patientString.isEmpty()) {
                patient = new Gson().fromJson(patientString, Patient.class);
            }
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void classify() {
        test_PRB_loading.setVisibility(View.VISIBLE);
        test_BTN_classify.setVisibility(View.GONE);
        long currentTime = System.currentTimeMillis();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB).child(patient.getUid()).child(String.valueOf(currentTime));
        Bitmap bitmap = Bitmap.createBitmap(test_DRW_spiral.getWidth(), test_DRW_spiral.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        test_DRW_spiral.draw(canvas);

        Classifier classifier = new Classifier(test_IMG_spiral, points);

        ref.putFile(getImageUri(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Result result = new Result(patient.getUid(), patient.getFullName(), currentTime , "OK", task.getResult().toString());
                            FirebaseDatabase.getInstance().getReference(Constants.RESULTS_DB).child(patient.getUid()).child(String.valueOf(result.getTime())).setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        moveToResult(result);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void moveToResult(Result result) {
        Intent intent = new Intent(getContext(), Activity_Result.class);
        intent.putExtra(Constants.RESULT, new Gson().toJson(result));
        startActivity(intent);
        getActivity().finish();
    }
}