package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.example.batteryalarmclock.R;

public class SettingDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Activity activity;

    public SettingDialog(Context context, int themeResId, FragmentActivity activity) {
        super(context, themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);

        RelativeLayout rel_password = findViewById(R.id.rel_password);
        rel_password.setOnClickListener(this);

        RelativeLayout rel_touchid = findViewById(R.id.rel_touchid);
        rel_touchid.setOnClickListener(this);

        RelativeLayout rel_interuder = findViewById(R.id.rel_interuder);
        rel_interuder.setOnClickListener(this);

        RelativeLayout rel_log_history = findViewById(R.id.rel_log_history);
        rel_log_history.setOnClickListener(this);

        RelativeLayout rel_setalarm = findViewById(R.id.rel_setalarm);
        rel_setalarm.setOnClickListener(this);

        RelativeLayout rel_Vibration = findViewById(R.id.rel_Vibration);
        rel_Vibration.setOnClickListener(this);

        RelativeLayout rel_repeat = findViewById(R.id.rel_repeat);
        rel_repeat.setOnClickListener(this);

        RelativeLayout rel_removeAds = findViewById(R.id.rel_removeAds);
        rel_removeAds.setOnClickListener(this);

        RelativeLayout rel_purchase = findViewById(R.id.rel_purchase);
        rel_purchase.setOnClickListener(this);

        RelativeLayout rel_rate = findViewById(R.id.rel_rate);
        rel_rate.setOnClickListener(this);

        RelativeLayout rel_shareapp = findViewById(R.id.rel_shareapp);
        rel_shareapp.setOnClickListener(this);

        RelativeLayout rel_feedback = findViewById(R.id.rel_feedback);
        rel_feedback.setOnClickListener(this);

        RelativeLayout rel_termspolicy = findViewById(R.id.rel_termspolicy);
        rel_termspolicy.setOnClickListener(this);

        RelativeLayout rel_appversion = findViewById(R.id.rel_appversion);
        rel_appversion.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rel_log_history:
                showHistoryPage();
                break;
            case R.id.rel_password:
                break;
            case R.id.rel_touchid:
                break;
            case R.id.rel_interuder:
                showIntruderLog();
                 break;
            case R.id.rel_Vibration:
                break;
            case  R.id.rel_repeat:
                break;
            case  R.id.rel_removeAds:
                break;
            case  R.id.rel_purchase:
                break;
            case  R.id.rel_rate:
                break;
            case  R.id.rel_shareapp:
                break;
            case  R.id.rel_setalarm:
                break;
            case  R.id.rel_feedback:
                break;
            case  R.id.rel_termspolicy:
                break;
            case  R.id.rel_appversion:
                break;

        }
    }

    private void showIntruderLog() {
        IntruderLogDialog intruderLogDialog = new IntruderLogDialog(context , android.R.style.Theme_Black_NoTitleBar_Fullscreen , activity);
        intruderLogDialog.show();
    }

    private void showHistoryPage() {
        HistoryLogDialog historyLogDialog = new HistoryLogDialog(context , android.R.style.Theme_Black_NoTitleBar_Fullscreen , activity);
        historyLogDialog.show();
    }
}
