package com.nilapps.batteryalarm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.nilapps.battery.alarm.clock.smart.R;

public class RateUsDialog extends Dialog {

    public RateUsDialog(Context context1, int dialogCustomTheme,  Activity context) {
        super(context1 , dialogCustomTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rateus);

        ImageView img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
