package com.nilapps.batteryalarm.templates;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;

import com.nilapps.batteryalarm.model.AlarmData;

public class Constant {
    public static Constant constant;
    public boolean isCableConnected  = false  ,isSetTherftAlarm = false , isNotificationShow = true;
    public AlarmData alarmDatabackup;
    public static int lastID = 0 ;
    public BillingManager billingManager;
    public String SKU_Removed_ads ="com.nilapps.battery.alarm.removead";

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

    public boolean checkInternetConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}