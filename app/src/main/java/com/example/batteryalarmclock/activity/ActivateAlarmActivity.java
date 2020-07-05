package com.example.batteryalarmclock.activity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.templates.Constant;

public class ActivateAlarmActivity extends AppCompatActivity {

    TextView current_per ;
    ProgressBar progressBar;
    Constant constant = Constant.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_activate);

        current_per = findViewById(R.id.current_per);
        current_per.setText(constant.getCurrentBatteryStutus(ActivateAlarmActivity.this) + "%");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress((int) constant.getCurrentBatteryStutus(ActivateAlarmActivity.this));

    }
}