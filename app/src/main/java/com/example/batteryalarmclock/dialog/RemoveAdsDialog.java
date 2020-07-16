package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.batteryalarmclock.R;

public class RemoveAdsDialog extends Dialog {
    Context context;
    Activity activity;

    public RemoveAdsDialog(@NonNull Context context, int themeResId, Activity activity) {
        super(context, themeResId);
        this.context = context;
        this.activity = activity;
    }

    public RemoveAdsDialog(Context context, Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remove_ads);

        Button btn_unlock = findViewById(R.id.btn_unlock);
        btn_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateUsDialog rateUsDialog = new RateUsDialog(context);
                rateUsDialog.show();
            }
        });

        ImageView img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
