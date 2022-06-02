package com.example.parkinson_disease_detection.activities;

import static com.example.parkinson_disease_detection.utils.MyDate.makeDateString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Result;
import com.example.parkinson_disease_detection.utils.Constants;
import com.google.gson.Gson;

public class Activity_Result extends AppCompatActivity {
    private ImageView result_IMG_spiral;
    private TextView result_LBL_date;
    private TextView result_LBL_result;
    private Button result_BTN_toMenu;

    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        findViews();
        setResult();
        initViews();
    }

    private void findViews() {
        result_IMG_spiral = findViewById(R.id. result_IMG_spiral);
        result_LBL_date = findViewById(R.id.result_LBL_date);
        result_LBL_result = findViewById(R.id.result_LBL_result);
        result_BTN_toMenu = findViewById(R.id.result_BTN_toMenu);
    }

    private void setResult() {
        String resultString = getIntent().getStringExtra(Constants.RESULT);
        result = new Gson().fromJson(resultString, Result.class);
    }

    private void initViews() {
        result_LBL_date.setText(makeDateString(result.getTime()));
        result_LBL_result.setText(result.getResult());
        result_LBL_result.setTextColor(result.getResult().equals("Healthy") ? Color.GREEN : Color.RED);
        setSpiralImage();

        result_BTN_toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMenu();
            }
        });
    }

    private void setSpiralImage() {
        Glide.with(this)
                .load(result.getUrl())
                .centerCrop()
                .into(result_IMG_spiral);
    }

    private void moveToMenu() {
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
        finish();
    }
}