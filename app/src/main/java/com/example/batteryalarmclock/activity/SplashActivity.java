package com.example.batteryalarmclock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

public class SplashActivity extends AppCompatActivity {
    SharedPreferencesApplication sh = new SharedPreferencesApplication();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                }
            }
        } , 3000);

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
}
