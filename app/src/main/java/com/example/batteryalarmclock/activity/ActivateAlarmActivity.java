package com.example.batteryalarmclock.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.service.BatteryAlarmService;
import com.example.batteryalarmclock.templates.Constant;

public class ActivateAlarmActivity extends AppCompatActivity {

    TextView current_per ;
    ProgressBar progressBar;
    Constant constant = Constant.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_activate);

        String setValue = getIntent().getStringExtra("SETVALUE");
        String setType = getIntent().getStringExtra("ALARMTYPE");

        current_per = findViewById(R.id.current_per);
        current_per.setText(String.valueOf(constant.getCurrentBatteryStutus(ActivateAlarmActivity.this)));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress((int) constant.getCurrentBatteryStutus(ActivateAlarmActivity.this));


        if (setType != null && setType.equalsIgnoreCase("SET_PERCNT")) {
            Log.e("ActivateAlarmActivity", " SET PERNCESGE ");

            Intent checkBattery = new Intent(ActivateAlarmActivity.this, BatteryAlarmService.class);
            checkBattery.putExtra("SETVALUE" , setValue);
            checkBattery.putExtra("ALARMTYPE" , setType);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(checkBattery);
            } else {
                startService(checkBattery);
            }
        }




    }

}
