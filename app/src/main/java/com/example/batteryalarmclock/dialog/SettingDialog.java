package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.biometrics.BiometricManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;


public class SettingDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Activity activity;
    SharedPreferencesApplication sh = new SharedPreferencesApplication();

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

        checkFingurePrint();

        SwitchCompat switch_fingure = findViewById(R.id.switch_fingure);
        switch_fingure.setChecked(sh.getUserFingurePrint(context));
        switch_fingure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkFingurePrint();
                }
                sh.setUserFingurePrint(context , b);
            }
        });
    }

    private void checkFingurePrint() {
        RelativeLayout rel_touchid = findViewById(R.id.rel_touchid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                rel_touchid.setVisibility(View.GONE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                rel_touchid.setVisibility(View.VISIBLE);
                showFingureNotRegister();
            } else {
                // Everything is ready for fingerprint authentication
                rel_touchid.setVisibility(View.VISIBLE);
            }
        }
        else {
            rel_touchid.setVisibility(View.GONE);
        }
    }

    public void showFingureNotRegister(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context , R.style.alert_dialog);
        builder.setTitle("Alert ");
        builder.setMessage("Please Device in Register in Fingure Print ");
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Creating dialog box
        android.app.AlertDialog dialog  = builder.create();
        dialog.show();

        Button btn1 =  dialog.findViewById(android.R.id.button1);
        btn1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rel_log_history:
                showHistoryPage();
                break;
            case R.id.rel_password:
                showLockScreen();
                break;
            case R.id.rel_interuder:
                showIntruderLog();
                 break;
            case R.id.rel_Vibration:
                break;
            case  R.id.rel_repeat:
                break;
            case  R.id.rel_removeAds:
                showRemoveAdsDialog();
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

    private void showLockScreen() {
        sh.setUserPin(context, "");
        Intent intent = new Intent(getContext(), com.example.lockscreen.EnterPinActivity.class);
        intent.putExtra("WHEN_CALL", "Setting");
        context.startActivity(intent);
    }

    private void showRemoveAdsDialog() {
        RemoveAdsDialog removeAdsDialog = new RemoveAdsDialog(context , android.R.style.Theme_Black_NoTitleBar_Fullscreen , activity);
        removeAdsDialog.show();
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
