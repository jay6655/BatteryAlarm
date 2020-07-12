package com.example.batteryalarmclock.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.activity.MainActivity;
import com.example.batteryalarmclock.model.IntruderData;
import com.example.batteryalarmclock.receiver.BatteryStatusReceiver;
import com.example.batteryalarmclock.receiver.PowerConnctedReceiver;

public class BatteryAlarmService extends Service {

    private static final String CHANNEL_ID = "ForegroundBatteryService";

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

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(this.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        //Power Connected or not receiver register
        PowerConnctedReceiver powerConnctedReceiver = new PowerConnctedReceiver();
        IntentFilter filter = new IntentFilter();
        for (String action: PowerConnctedReceiver.POWER_ACTIONS) {
            filter.addAction(action);
        }
        registerReceiver(powerConnctedReceiver , filter);

        //Battery status receiver register
        BatteryStatusReceiver batteryStatusReceiver = new BatteryStatusReceiver();
        IntentFilter filter_new = new IntentFilter();
        filter_new.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter_new.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryStatusReceiver , filter_new);

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
    }
}