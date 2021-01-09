package com.nilapps.batteryalarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.nilapps.battery.alarm.clock.smart.R;
import com.nilapps.batteryalarm.templates.BillingManager;
import com.nilapps.batteryalarm.templates.Constant;
import com.nilapps.batteryalarm.util.SharedPreferencesApplication;
import com.nilapps.battery.alarm.clock.smart.R;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    SharedPreferencesApplication sh = new SharedPreferencesApplication();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Constant.getInstance().billingManager = new BillingManager(this, bilingmanager);

        final RelativeLayout splash_view = findViewById(R.id.splash_view);
        final RelativeLayout mainView = findViewById(R.id.mainView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sh.getGetStarted(SplashActivity.this)) {
                    mainView.setVisibility(View.GONE);
                    splash_view.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    splash_view.setVisibility(View.GONE);
                    mainView.setVisibility(View.VISIBLE);
                    Constant.getInstance().billingManager = new BillingManager(SplashActivity.this, bilingmanager);
                }
            }
        } , 3000);

        TextView txt_privacy =  findViewById(R.id.term_service);
        txt_privacy.setVisibility(View.VISIBLE);
        txt_privacy.setMovementMethod(LinkMovementMethod.getInstance());

        Button get_started = findViewById(R.id.get_started);
        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh.setGetStarted(SplashActivity.this, true);
                Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    BillingManager.BillingUpdatesListener bilingmanager = new BillingManager.BillingUpdatesListener() {
        @Override
        public void onBillingClientSetupFinished() {
            Log.e("dailog", "onBillingClientSetupFinished");
            if(Constant.getInstance().billingManager != null){
                Constant.getInstance().billingManager.queryPurchases();
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
            for (Purchase p : purchases) {
                if (p.getSku().equals(Constant.getInstance().SKU_Removed_ads)) {
                    sh.setInAppDone(SplashActivity.this , true);
                }
            }
        }

        @Override
        public void onPurchasesFaild() {
        }
    };
}