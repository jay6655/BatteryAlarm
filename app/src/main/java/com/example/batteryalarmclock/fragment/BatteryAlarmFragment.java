package com.example.batteryalarmclock.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

public class BatteryAlarmFragment extends Fragment {
    View rootView ;
    SharedPreferencesApplication sh = new SharedPreferencesApplication();
    CameraManager cameraManager;
    String mCameraId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_battery_alarm , container , false);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().registerReceiver(null, ifilter);
        int level = 0 , scale = 0;
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        int batteryPct = (int) (level * 100 / (float)scale);

        TextView txt_per = rootView.findViewById(R.id.txt_per);
        txt_per.setText(String.valueOf(batteryPct));

        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setProgress((int) batteryPct);

        Switch sw_slient = rootView.findViewById(R.id.sw_slient);
        sw_slient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sh.setSlientSwich(getContext() , b);
                }
                else {
                    sh.setSlientSwich(getContext() , b);
                }
            }
        });
        setFlashLight();

        Switch sw_flash = rootView.findViewById(R.id.sw_flash);
        sw_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sh.setFlashLight(getContext() , b);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager.setTorchMode(mCameraId, b);
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager.setTorchMode(mCameraId, b);
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    sh.setSlientSwich(getContext() , b);
                }
            }
        });

        final RelativeLayout timer_view = rootView.findViewById(R.id.timer_view);
        final RelativeLayout persantage_view = rootView.findViewById(R.id.persantage_view);
        persantage_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogShow("per");
            }
        });

        timer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogShow("time");
            }
        });

        RadioGroup alarm_button_view = rootView.findViewById(R.id.alarm_button_view);
        alarm_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.persantage) {
                    timer_view.setVisibility(View.INVISIBLE);
                    persantage_view.setVisibility(View.VISIBLE);
                }
                else if (i == R.id.timer) {
                    timer_view.setVisibility(View.VISIBLE);
                    persantage_view.setVisibility(View.INVISIBLE);
                }
            }
        });


        return rootView;
    }

    private void alertDialogShow(String compare) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext() , AlertDialog.THEME_HOLO_DARK);
        // set alert_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput =  promptsView.findViewById(R.id.etUserInput);
        final TextView textView1 = promptsView.findViewById(R.id.textView1);
        if (compare.equalsIgnoreCase("time"))
        {
            textView1.setText("Enter Minute : ");
        }
        else {
            textView1.setText("Enter persentage : ");
        }
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        Toast.makeText(getContext(), "Entered: "+userInput.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void setFlashLight() {

        boolean isFlashAvailable = getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        /*if (!isFlashAvailable) {
            showNoFlashError();
        }*/

        cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
}

