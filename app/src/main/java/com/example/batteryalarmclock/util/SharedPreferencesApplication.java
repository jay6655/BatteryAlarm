package com.example.batteryalarmclock.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesApplication {
    public static final String MYPREFERENCES = "MyPreference" ;
    public String GETSTARTED = "GETSTARTED";
    public String SETSLIENT = "SETSLIENT";
    public String FLASHLIGHT = "FLASHLIGHT";


    //TODO Set and Get get Started  or not
    public boolean getGetStarted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        if (sharedPreferences.contains(GETSTARTED)) {
            return sharedPreferences.getBoolean(GETSTARTED, false);
        }
        return false;
    }

    public void setGetStarted(Context context , Boolean set) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GETSTARTED, set);
        editor.apply();
    }

    //Switch is slient
    public void setSlientSwich(Context context, boolean set) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SETSLIENT, set);
        editor.apply();
    }

    public boolean getSlientSwich(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        if (sharedPreferences.contains(SETSLIENT)) {
            return sharedPreferences.getBoolean(SETSLIENT, false);
        }
        return false;
    }

    //Flasjh Light
    public void setFlashLight(Context context, boolean set) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FLASHLIGHT, set);
        editor.apply();
    }

    public boolean getFlashLight(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
        if (sharedPreferences.contains(FLASHLIGHT)) {
            return sharedPreferences.getBoolean(FLASHLIGHT, false);
        }
        return false;
    }
}
