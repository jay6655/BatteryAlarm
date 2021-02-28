package com.nilapps.batteryalarm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;

import com.nilapps.battery.alarm.clock.smart.BuildConfig;
import com.nilapps.battery.alarm.clock.smart.R;
import com.nilapps.batteryalarm.templates.Constant;
import com.nilapps.batteryalarm.util.SharedPreferencesApplication;
import com.nilapps.lockscreen.EnterPinActivity;

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


        RelativeLayout rel_removeAds = findViewById(R.id.rel_removeAds);
        rel_removeAds.setOnClickListener(this);

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

        RelativeLayout rel_back = findViewById(R.id.rel_back);
        rel_back.setOnClickListener(this);

        checkFingurePrint();

        SwitchCompat switch_fingure = findViewById(R.id.switch_fingure);
        switch_fingure.setChecked(sh.getUserFingurePrint(context));
        switch_fingure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sh.setUserFingurePrint(context , b);
                if (b) {
                    checkFingurePrint();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView txt_version_val = findViewById(R.id.txt_version_val);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            txt_version_val.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        RelativeLayout rel_live_ad = findViewById(R.id.rel_live_ad);
        RelativeLayout rel_inapp = findViewById(R.id.rel_inapp);
        if (sh.getInAppDone(context)){
//            rel_live_ad.setVisibility(View.GONE);
            rel_inapp.setVisibility(View.GONE);
        }
        else {
//            Constant.getInstance().loadBannerAd(rel_live_ad, context, activity);
            rel_inapp.setVisibility(View.VISIBLE);
        }
    }


    private void checkFingurePrint() {
        RelativeLayout rel_touchid = findViewById(R.id.rel_touchid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    rel_touchid.setVisibility(View.GONE);
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    rel_touchid.setVisibility(View.VISIBLE);
                    if (sh.getUserFingurePrint(context)) {
                        showFingureNotRegister();
                    }
                } else {
                    // Everything is ready for fingerprint authentication
                    rel_touchid.setVisibility(View.VISIBLE);
                }
            }
            else {
                rel_touchid.setVisibility(View.GONE);
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
            case  R.id.rel_removeAds:
                showRemoveAdsDialog();
                break;
            case  R.id.rel_rate:
                rateUsCode();
                break;
            case  R.id.rel_shareapp:
                shareAppCode();
                break;
            case  R.id.rel_setalarm:
                break;
            case  R.id.rel_feedback:
                break;
            case  R.id.rel_termspolicy:
                break;
            case  R.id.rel_appversion:
                break;
            case  R.id.rel_back:
                dismiss();
                break;

        }
    }

    private void rateUsCode() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void shareAppCode() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.nilapps.battery.alarm.clock.smart" +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            e.printStackTrace();
            //e.toString();
        }
    }

    private void showLockScreen() {
        sh.setUserPin(context, "");
        Intent intent = new Intent(getContext(), EnterPinActivity.class);
        intent.putExtra("WHEN_CALL", "Setting");
        context.startActivity(intent);
    }

    private void showRemoveAdsDialog() {
        RemoveAdsDialog removeAdsDialog = new RemoveAdsDialog(context  ,R.style.AppTheme ,activity);
        removeAdsDialog.show();
    }

    private void showIntruderLog() {
        IntruderLogDialog intruderLogDialog = new IntruderLogDialog(context , R.style.AppTheme , activity);
        intruderLogDialog.show();
}

    private void showHistoryPage() {
        HistoryLogDialog historyLogDialog = new HistoryLogDialog(context , R.style.AppTheme , activity);
        historyLogDialog.show();
    }
}
