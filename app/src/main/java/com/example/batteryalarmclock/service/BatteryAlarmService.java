package com.example.batteryalarmclock.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.activity.ActivateAlarmActivity;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.receiver.AlarmReceiver;
import com.example.batteryalarmclock.templates.Constant;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class BatteryAlarmService extends Service {

    private static final String CHANNEL_ID = "ForegroundBatteryService";
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String setValue = intent.getStringExtra("SETVALUE");
        String setType = intent.getStringExtra("ALARMTYPE");

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, ActivateAlarmActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        notificationIntent.putExtra("SETVALUE" , setValue);
        notificationIntent.putExtra("ALARMTYPE" , setType);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(this.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        countDownTimer = new CountDownTimer(1000000000 , 7000) {
            @Override
            public void onTick(long l) {
                Log.e("Coundtime tmerunninf " , TimeUnit.MILLISECONDS.toMinutes(l) + " ");
                int value = Constant.getInstance().getCurrentBatteryStutus(getApplicationContext());

                if (setValue != null && value == Integer.parseInt(setValue)) {
                    Log.e("Conditiuon true" , value + " ");
                    onFinish();

                    Calendar calendar = Calendar.getInstance();
                    AlarmData alarmData = new AlarmData();
                    alarmData.setId((int) calendar.getTimeInMillis() + 1 );
                    alarmData.setType("percentage");
                    alarmData.setTime(calendar.getTimeInMillis());
                    AlarmReceiver.setAlarm(getApplicationContext(), alarmData);
                }
            }

            @Override
            public void onFinish() { }
        };

        countDownTimer.start();

        return START_STICKY;
    }

    public boolean isConnected() {
        Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = 0;
        if (intent != null) {
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        }
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Alarm is Playing",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
