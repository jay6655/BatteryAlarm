package com.example.batteryalarmclock.templates;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.util.Log;

import com.example.batteryalarmclock.model.AlarmData;

public class Constant {
    public static Constant constant;
    public boolean isCableConnected  = false;
    public AlarmData alarmDatabackup;
    public static int lastID = 0 ;
    public boolean isSetTherftAlarm = false;
    public MediaPlayer mp;

    public static Camera camera = null;

    public static  CameraManager manager;


    public static Constant getInstance() {
        if (constant == null) {
            return constant = new Constant();
        }
        return constant ;
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