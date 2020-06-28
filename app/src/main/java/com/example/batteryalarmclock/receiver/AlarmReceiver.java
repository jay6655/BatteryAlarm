package com.example.batteryalarmclock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.batteryalarmclock.activity.AlarmRingingActivity;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.service.AlarmService;
import com.example.batteryalarmclock.service.WakeLocker;
import com.example.batteryalarmclock.templates.Constant;

import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class AlarmReceiver  extends BroadcastReceiver {

    static Constant constant = Constant.getInstance();
    private AlarmData alarm;
    private Context context;
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        Log.e("AlarmReceiver", "intent not null");
        Bundle bundleintent = intent.getBundleExtra("BUNDLE_EXTRA");
        if (bundleintent != null) {
            Log.e("AlarmReceiver", "bundle not null");
            alarm = bundleintent.getParcelable("ALARM_KEY");
        }
        if (alarm == null) {
            Log.e("AlarmReceiver", "Alarm is null", new NullPointerException());
            return;
        } else {
            WakeLocker.acquire(context);
            if (constant.isAlarmActive) {
                Log.e("AlarmReceiver", "Alarm is Active onReceiver" + alarm.toString());
                constant.isAlarmActive = false;

                Intent intent1 = new Intent(context, AlarmRingingActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                final Bundle bundle = new Bundle();
                bundle.putParcelable("ALARM_KEY", alarm);
                intent1.putExtra("BUNDLE_EXTRA", bundle);
                intent1.putExtra("WHICH", "receiver");
                context.startActivity(intent1);

                constant.alarmDatabackup = alarm;
                
                Log.e("AlarmReceiver", "ALARM ID  IN RECEIVER : " + alarm.getId());

                Intent serviceIntent = new Intent(context, AlarmService.class);
                final Bundle bundle1 = new Bundle();
                bundle1.putParcelable("ALARM_KEY", alarm);
                serviceIntent.putExtra("BUNDLE_EXTRA", bundle1);
                ContextCompat.startForegroundService(context, serviceIntent);
            } 
        }
    }


    public static void setReminderAlarms(Context context, List<AlarmData> alarms) {
        for(AlarmData alarm : alarms) {
            setAlarm(context, alarm);
        }
    }

    private static PendingIntent launchAlarmLandingPage(Context ctx, AlarmData alarm) {
        return PendingIntent.getActivity(ctx, alarm.getId(), launchIntent(ctx), FLAG_UPDATE_CURRENT
        );
    }

    private static Intent launchIntent(Context ctx) {
        return new Intent(ctx , AlarmRingingActivity.class);
    }

    public static void  setAlarm(Context context, AlarmData alarm){
        final Intent intent ;
        if (alarm.getType().equals("time")) {
            intent = new Intent(context, AlarmReceiver.class);
            final Bundle bundle = new Bundle();
            bundle.putParcelable("ALARM_KEY", alarm);
            intent.putExtra("BUNDLE_EXTRA", bundle);
            intent.putExtra("WHEN" , "setReminderAlarm ");
            PendingIntent pIntent  = PendingIntent.getBroadcast(
                    context,
                    alarm.getId(),
                    intent,
                    FLAG_UPDATE_CURRENT
            );
            ScheduleAlarm.with(context).schedule(alarm, pIntent);
        }
        else {
            intent = new Intent(context, AlarmReceiver.class);

            final Bundle bundle = new Bundle();
            bundle.putParcelable("ALARM_KEY", alarm);
            intent.putExtra("BUNDLE_EXTRA", bundle);
            intent.putExtra("WHEN" , "setReminderAlarm ");
            PendingIntent pIntent  = PendingIntent.getBroadcast(
                    context,
                    alarm.getId(),
                    intent,
                    FLAG_UPDATE_CURRENT
            );
            ScheduleAlarm.with(context).schedule(alarm, pIntent);
        }
        /*else {
           *//* //Check whether the alarm is set to run on any days
            final Calendar nextAlarmTime = getTimeForNextAlarm(alarm);
            alarm.setTime(nextAlarmTime.getTimeInMillis());
            intent = new Intent(context, AlarmReceiver.class);
            final Bundle bundle = new Bundle();
            bundle.putParcelable("ALARM_KEY", alarm);
            intent.putExtra("BUNDLE_EXTRA", bundle);
            intent.putExtra("WHEN", "setReminderAlarm ");
            pIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.getId(),
                    intent,
                    FLAG_UPDATE_CURRENT
            );*//*
        }*/

    }

    public static void cancelReminderAlarm(Context context, AlarmData alarm) {
        Log.e("DATAAA12" , " cancelReminderAlarm  "+ alarm.getId() +  " cc");
        final Intent intent = new Intent(context, AlarmReceiver.class);

        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                alarm.getId(),
                intent,
                FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(pIntent);
        }
    }


    private static class ScheduleAlarm {

        private final Context ctx;
        private final AlarmManager am;
        private ScheduleAlarm( AlarmManager am,  Context ctx) {
            this.am = am;
            this.ctx = ctx;
        }
        static ScheduleAlarm with(Context context) {
            final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(am == null) {
                throw new IllegalStateException("AlarmManager is null");
            }
            return new ScheduleAlarm(am, context);
        }
        void schedule(AlarmData alarm, PendingIntent pi) {
            Log.e("ALARM RECEIVER " ,   " schedule  SS");
            if(SDK_INT > LOLLIPOP) {
                am.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.getTime(), launchAlarmLandingPage(ctx, alarm)), pi);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi);
            }
        }
    }

    /*private static Calendar getTimeForNextAlarm(AlarmData alarm) {
        Log.e("Receiver " , "getTimeForNextAlarm called : ");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());
        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);
        Log.e("Receiver " , "StartInder : " + startIndex);
        if (alarm.getDays().equalsIgnoreCase("00")) {
            if (calendar.getTimeInMillis() > currentTime){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            return  calendar;
        }
        else {
            int count = 0;
            boolean isAlarmSetForDay;
            final SparseBooleanArray daysArray = alarm.getDaysA();
            do {
                final int index = (startIndex + count) % 7;
                isAlarmSetForDay =
                        daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
                if (!isAlarmSetForDay) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    count++;
                }
            } while (!isAlarmSetForDay && count < 7);
            return calendar;
        }
    }*/

    /*private static int getStartIndexFromTime(Calendar c) {
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break; case Calendar.TUESDAY: startIndex = 1;
                break; case Calendar.WEDNESDAY: startIndex = 2;
                break; case Calendar.THURSDAY: startIndex = 3;
                break; case Calendar.FRIDAY: startIndex = 4;
                break; case Calendar.SATURDAY: startIndex = 5;
                break; case Calendar.SUNDAY: startIndex = 6;
                break;
        }
        return startIndex;
    }*/
}