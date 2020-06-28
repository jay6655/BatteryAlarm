package com.example.batteryalarmclock.service;

import android.content.Context;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager powerManager = (PowerManager) ctx.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Battery:MyWakelockTag");
        wakeLock.acquire();

        /*PowerManager pm = (PowerManager) ctx.getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, MainActivity.APP_TAG);*/

    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}
