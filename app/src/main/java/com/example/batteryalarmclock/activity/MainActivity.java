package com.example.batteryalarmclock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

    public static final String APP_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Constant.getInstance().mContext = this;

        //MainActivity.this.startService(new Intent(MainActivity.this , BatteryAlarmService.class));

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
}
