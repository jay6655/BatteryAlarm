package com.example.batteryalarmclock.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.activity.ActivateAlarmActivity;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.receiver.AlarmReceiver;
import com.example.batteryalarmclock.service.WakeLocker;
import com.example.batteryalarmclock.templates.Constant;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

import java.util.Calendar;

public class BatteryAlarmFragment extends Fragment {
    View rootView ;
    SharedPreferencesApplication sh = new SharedPreferencesApplication();
    CameraManager cameraManager;
    String mCameraId;
    TextView enterPer , entertime  , current_per;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_battery_alarm , container , false);

        entertime = rootView.findViewById(R.id.entertime);
        enterPer = rootView.findViewById(R.id.enterPer);

        Constant constant = Constant.getInstance();

        current_per = rootView.findViewById(R.id.txt_per);
        current_per.setText(String.valueOf(constant.getCurrentBatteryStutus(requireContext())));

        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setProgress((int) constant.getCurrentBatteryStutus(requireContext()));

        Switch sw_slient = rootView.findViewById(R.id.sw_slient);
        sw_slient.setChecked(sh.getSlientSwich(requireContext()));
        sw_slient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sh.setSlientSwich(requireContext(), b);
            }
        });

        setFlashLight();

        Switch sw_flash = rootView.findViewById(R.id.sw_flash);
        sw_flash.setChecked(sh.getFlashLight(requireContext()));
        sw_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sh.setFlashLight(requireContext(), b);

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
        if (sh.getAlarmMethod(requireContext()).equalsIgnoreCase("SET_PERCNT")){
            RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.persantage);
            radioButton.setChecked(true);

            timer_view.setVisibility(View.INVISIBLE);
            persantage_view.setVisibility(View.VISIBLE);
            sh.setAlarmMethod(requireContext() , "SET_PERCNT");
        }else {
            RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.timer);
            radioButton.setChecked(true);

            timer_view.setVisibility(View.VISIBLE);
            persantage_view.setVisibility(View.INVISIBLE);
            sh.setAlarmMethod(requireContext() , "SET_TIMER");
        }
        alarm_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.persantage) {
                    timer_view.setVisibility(View.INVISIBLE);
                    persantage_view.setVisibility(View.VISIBLE);
                    sh.setAlarmMethod(requireContext() , "SET_PERCNT");
                }
                else if (i == R.id.timer) {
                    timer_view.setVisibility(View.VISIBLE);
                    persantage_view.setVisibility(View.INVISIBLE);
                    sh.setAlarmMethod(requireContext() , "SET_TIMER");
                }
            }
        });

        Button set_alarm = rootView.findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
        return rootView;
    }

    private void setAlarm() {
        if (sh.getAlarmMethod(requireContext()).equalsIgnoreCase("SET_PERCNT")) {
            Intent activateAlarm = new Intent(getContext(), ActivateAlarmActivity.class);
            activateAlarm.putExtra("SETVALUE" , enterPer.getText().toString().trim());
            activateAlarm.putExtra("ALARMTYPE" , "SET_PERCNT");
            startActivity(activateAlarm);
        }
        else {
            final Calendar calendar = Calendar.getInstance();
            AlarmData alarmData = new AlarmData();
            alarmData.setId((int) calendar.getTimeInMillis());
            alarmData.setType("time");
            calendar.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(entertime.getText())));
            alarmData.setTime(calendar.getTimeInMillis());
            AlarmReceiver.setAlarm(getContext(), alarmData);

            WakeLocker.acquire(requireContext());

            Intent activateAlarm = new Intent(getContext(), ActivateAlarmActivity.class);
            startActivity(activateAlarm);
        }
    }

    private void alertDialogShow(final String compare) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext() , AlertDialog.THEME_HOLO_DARK);
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
                       if (compare.equalsIgnoreCase("time")){
                           entertime.setText(userInput.getText().toString().trim());
                       }
                       else {
                           enterPer.setText(userInput.getText().toString().trim());
                       }
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
        boolean isFlashAvailable = requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        if (!isFlashAvailable) {
            rootView.findViewById(R.id.flash_view).setVisibility(View.GONE);
        }
    }
}

