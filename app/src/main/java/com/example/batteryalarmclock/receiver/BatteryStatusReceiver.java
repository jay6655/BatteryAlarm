package com.example.batteryalarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.templates.Constant;
import com.example.batteryalarmclock.templates.DBHelper;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BatteryStatusReceiver extends BroadcastReceiver {
    SharedPreferencesApplication sh = new SharedPreferencesApplication();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (Objects.equals(intent.getAction(), Intent.ACTION_BATTERY_OKAY)){
                Log.e("RECEIVER " , "PRE  OKAY  %");
            }
            if (Objects.equals(intent.getAction(), Intent.ACTION_BATTERY_CHANGED)){
                int level , scale ;
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int battery_level = (int) (level * 100 / (float)scale) ;
                Log.e("RECEIVER " , "PRE " + battery_level + " %" );

                if (sh.getAlarmPercentage(context) == battery_level ){
                    sh.setAlarmPercentage(context , 0 );
                    AlarmData alarmData = new DBHelper(context).getPerticularAlarmData(String.valueOf(Constant.lastID));
                    AlarmReceiver.setAlarm(context , alarmData);
                    //AlarmReceiver.setReminderAlarms(context , alarmData);
                }
            }

        }
    }
}