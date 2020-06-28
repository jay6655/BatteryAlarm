package com.example.batteryalarmclock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.receiver.AlarmReceiver;
import com.example.batteryalarmclock.service.AlarmService;
import com.example.batteryalarmclock.service.BatteryAlarmService;

public class AlarmRingingActivity extends AppCompatActivity {
    AlarmData alarm ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_ringing);

        Bundle bundleintent = getIntent().getBundleExtra("BUNDLE_EXTRA");
        if (bundleintent != null) {
            Log.e("AlarmReceiver", "bundle not null");
            alarm = bundleintent.getParcelable("ALARM_KEY");
        }

        Button stop_alarm = findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(AlarmRingingActivity.this , BatteryAlarmService.class));
                AlarmReceiver.cancelReminderAlarm(AlarmRingingActivity.this , alarm);
                stopService(new Intent(AlarmRingingActivity.this , AlarmService.class));
                finish();
            }
        });
    }

}
