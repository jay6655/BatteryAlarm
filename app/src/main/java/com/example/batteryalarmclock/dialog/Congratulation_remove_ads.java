package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.batteryalarmclock.R;

public class Congratulation_remove_ads extends Dialog {
    Context context;
    Activity activity;

    public Congratulation_remove_ads(@NonNull Context context, int themeResId, Activity activity) {
        super(context, themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_congratulations);
    }
}
