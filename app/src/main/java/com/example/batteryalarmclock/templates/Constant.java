package com.example.batteryalarmclock.templates;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.batteryalarmclock.model.AlarmData;

public class Constant {
    public static Constant constant;
    public boolean isCableConnected  = false;
    public AlarmData alarmDatabackup;
    public static int lastID = 0 ;
    public boolean isSetTherftAlarm = false;
    public MediaPlayer mp;
    public int adcountfailbanner = 0 ;
    public boolean ismopubshow;
    public Admobe_Banner_controller ads_controller = new Admobe_Banner_controller();
    public BillingManager billingManager;
    public String SKU_Removed_ads ="";

    public static Constant getInstance() {
        if (constant == null) {
            return constant = new Constant();
        }
        return constant ;
    }

    public int getCurrentBatteryStutus(Context context) {
        Log.e("servi" , "getCurrentBatteryStutus");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = 0 , scale = 0;
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        return  (int) (level * 100 / (float)scale);
    }

    public void loadBannerAd(final RelativeLayout livead , Context context , Activity activity) {
        if (this.ads_controller.checkAddviewNull() && checkInternetConnection(context) && !constant.ismopubshow) {
            this.ads_controller.add_init(activity);
            livead.removeAllViews();
            livead.addView(this.ads_controller.mAdView);
            this.ads_controller.setOnAdsShowingListner(new Admobe_Banner_controller.AdmobeAdsListner(){

                @Override
                public void onAdFailedToLoad(int n) {
                    livead.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAdLoaded() {
                    livead.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        if (checkInternetConnection(context)) {
            if (this.ads_controller.isAdsshowingornot() && constant.ismopubshow) {
                livead.setVisibility(View.VISIBLE);
                livead.removeAllViews();
                if (this.ads_controller.mAdView.getParent() != null) {
                    ((ViewGroup)this.ads_controller.mAdView.getParent()).removeView(this.ads_controller.mAdView);
                }
                livead.removeAllViews();
                livead.addView(this.ads_controller.mAdView);
                return;
            }
            livead.setVisibility(View.VISIBLE);
            this.ads_controller.add_init(activity);
            livead.removeAllViews();
            livead.addView(this.ads_controller.mAdView);
            this.ads_controller.setOnAdsShowingListner(new Admobe_Banner_controller.AdmobeAdsListner(){
                @Override
                public void onAdFailedToLoad(int n) {
                    Log.e("Load" , "Failed ");
                    livead.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAdLoaded() {
                    livead.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public boolean checkInternetConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /*public void showDialogInternet(boolean allowBackButton, String title,
                                   String message,Context mContext) {
        Dialog dialogCommonSimple = new Dialog(mContext, R.style.DialogCustomTheme);
        dialogCommonSimple.setContentView(R.layout.dialog_internet);
        dialogCommonSimple.setCancelable(allowBackButton);
        setLayout_DialogInternet(dialogCommonSimple, title, message,mContext);
        dialogCommonSimple.setCanceledOnTouchOutside(false);
        dialogCommonSimple.show();
    }*/
}