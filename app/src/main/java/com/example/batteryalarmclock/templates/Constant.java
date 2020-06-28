package com.example.batteryalarmclock.templates;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.batteryalarmclock.model.AlarmData;

public class Constant {
    public static Constant constant;
    public boolean isAlarmActive = true;
    public AlarmData alarmDatabackup;
    public Context mContext;


    public static Constant getInstance() {
        if (constant == null) {
            return constant = new Constant();
        }
        return new Constant();
    }

    public int getCurrentBatteryStutus(Context context) {
         Log.e("servi" , "getCurrentBatteryStutus");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = 0 , scale = 0;
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        return  (int) (level * 100 / (float)scale);
    }

}
