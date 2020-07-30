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
import com.example.batteryalarmclock.util.SharedPreferencesApplication;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        if(!isMyServiceRunning(BatteryAlarmService.class)) {
            MainActivity.this.startService(new Intent(MainActivity.this, BatteryAlarmService.class));
        }

        //Changes 

        Log.e("AVT" , "Main isSetTherftAlarm : " + Constant.getInstance().isSetTherftAlarm );
        if (Constant.getInstance().isSetTherftAlarm) {
            Log.e("AVT" , "Main If called isSetTherftAlarm : " + Constant.getInstance().isSetTherftAlarm );
            Constant.getInstance().isSetTherftAlarm = false;
            new SharedPreferencesApplication().setTheftAlarmOcured(MainActivity.this , false);
            Log.e("AVT" , "Main If called isSetTherftAlarm : " + Constant.getInstance().isSetTherftAlarm );
            if (Constant.getInstance().mp != null) {
                Log.e("AVT" , "Main MP NOT null ");
                if (Constant.getInstance().mp.isPlaying()){
                    Constant.getInstance().mp.stop();
                    Constant.getInstance().mp.release();
                    Constant.getInstance().mp = null ;
                }
                else {
                    Log.e("AVT", "Main MP  null ");
                }
            }
            if (getIntent() != null) {
                Log.e("AVT" , "Main  getIntent NOt null" );
                String done = getIntent().getStringExtra("DONE");
                assert done != null;
                if (done.equalsIgnoreCase("DONEPWD")) {
                    navView.getMenu().findItem(R.id.navigation_theft).setChecked(true);
                    Fragment fragment1  = new TheftAlarmFragment();
                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    ft1.add(R.id.container_fram, fragment1 ,"TheftAlarm");
                    ft1.commit();
                }
            }
        }
        else {
            defualFragmentLoad();
        }

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mMenuId = item.getItemId();
                for (int i = 0; i < navView.getMenu().size(); i++) {
                    MenuItem menuItem = navView.getMenu().getItem(i);
                    boolean isChecked = menuItem.getItemId() == item.getItemId();
                    menuItem.setChecked(isChecked);
                }

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