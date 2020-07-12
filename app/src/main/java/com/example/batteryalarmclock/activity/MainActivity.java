package com.example.batteryalarmclock.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.fragment.BatteryAlarmFragment;
import com.example.batteryalarmclock.fragment.TheftAlarmFragment;
import com.example.batteryalarmclock.service.BatteryAlarmService;
import com.example.batteryalarmclock.templates.Constant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if(!isMyServiceRunning(BatteryAlarmService.class)) {
            MainActivity.this.startService(new Intent(MainActivity.this, BatteryAlarmService.class));
        }

        if (Constant.getInstance().isSetTherftAlarm) {
            Constant.getInstance().isSetTherftAlarm = false;
            if (getIntent() != null) {
                String done = getIntent().getStringExtra("DONE");
                assert done != null;
                if (done.equalsIgnoreCase("DONEPWD")) {
                    Constant.getInstance().mp.release();
                    Constant.getInstance().mp = null;
                }
            }
        }

        defualFragmentLoad();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_alarm:
                        Fragment fragment = new BatteryAlarmFragment();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.container_fram ,fragment,"BatteryAlarm");
                        ft.commit();
                        break;
                    case R.id.navigation_theft:
                        Fragment fragment1  = new TheftAlarmFragment();
                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        ft1.add(R.id.container_fram, fragment1 ,"TheftAlarm");
                        ft1.commit();
                        break;
                }
                return true;
            }
        });
    }


    private void defualFragmentLoad() {
        Fragment fragment = new BatteryAlarmFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container_fram ,fragment,"BatteryAlarm");
        ft.commit();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ){

            }else{
                Toast.makeText(MainActivity.this, "Access Denied ! Cannot proceed further ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}