package com.nilapps.batteryalarm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.nilapps.battery.alarm.clock.smart.R;
import com.nilapps.batteryalarm.templates.BillingManager;
import com.nilapps.batteryalarm.templates.Constant;
import com.nilapps.batteryalarm.util.SharedPreferencesApplication;

import java.util.ArrayList;
import java.util.List;

public class RemoveAdsDialog extends Dialog {
    Context context;
    Activity activity;
    Constant constant = Constant.getInstance();

    public RemoveAdsDialog(@NonNull Context context, int themeResId, Activity activity) {
        super(context, themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remove_ads);
        constant.billingManager = new BillingManager(activity,bilingmanager);

        Button btn_unlock = findViewById(R.id.btn_unlock);
        btn_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (constant.checkInternetConnection(context)) {
                            List<String> skulist = new ArrayList<>();
                            skulist.add(constant.SKU_Removed_ads);
                            constant.billingManager.initiatePurchaseFlow(skulist, BillingClient.SkuType.INAPP);
                            close();
                        }else{
                            showalert(context.getString(R.string.alert) , context.getString(R.string.no_internet));
                        }
                    }
                }, 300);
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

    BillingManager.BillingUpdatesListener bilingmanager = new BillingManager.BillingUpdatesListener() {
        @Override
        public void onBillingClientSetupFinished() {
            Log.e("dailog", "onBillingClientSetupFinished");
            if(constant.billingManager != null){
                constant.billingManager.queryPurchases();
            }
        }

        @Override
        public void onConsumeFinished(String token, int result) {

            if (result == BillingClient.BillingResponseCode.OK) {
                Log.e("dailog", "onConsumeFinished");
            }
        }


        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {
            //update ui
            for (Purchase p : purchases) {
                if (p.getSku().equals(constant.SKU_Removed_ads)) {
                    purchaseMethod();
                }
            }
        }

        @Override
        public void onPurchasesFaild() {
            showalert(context.getString(R.string.txt_failed) , context.getString(R.string.fail_msg));
        }
    };

    private void showalert(String title , String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.alert_dialog);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Creating dialog box
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        Button btn1 = dialog.findViewById(android.R.id.button1);
        btn1.setTextColor(context.getResources().getColor(R.color.colorPrimary));

    }

    public void purchaseMethod() {
        new SharedPreferencesApplication().setInAppDone(context , true);
        Congratulation_remove_ads congratulation_remove_ads = new Congratulation_remove_ads(context, R.style.DialogCustomTheme , activity);
        congratulation_remove_ads.show();
    }


    private void close() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 300);
    }
}