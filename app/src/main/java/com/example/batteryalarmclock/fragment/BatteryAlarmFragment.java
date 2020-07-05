package com.example.batteryalarmclock.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.example.batteryalarmclock.dialog.PerPickerCustomDialog;
import com.example.batteryalarmclock.dialog.SettingDialog;
import com.example.batteryalarmclock.dialog.TimePickerCustomDialog;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.receiver.AlarmReceiver;
import com.example.batteryalarmclock.service.WakeLocker;
import com.example.batteryalarmclock.templates.Constant;
import com.example.batteryalarmclock.templates.DBHelper;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.example.batteryalarmclock.dialog.PerPickerCustomDialog.selectdValue;
import static com.example.batteryalarmclock.dialog.PerPickerCustomDialog.selectvaluedone;
import static com.example.batteryalarmclock.dialog.TimePickerCustomDialog.selectedhour;
import static com.example.batteryalarmclock.dialog.TimePickerCustomDialog.selectedmin;
import static com.example.batteryalarmclock.dialog.TimePickerCustomDialog.selectvaluedonetime;

public class BatteryAlarmFragment extends Fragment implements View.OnClickListener {
    View rootView ;
    SharedPreferencesApplication sh = new SharedPreferencesApplication();
    TextView enterPer , entertime , current_per_frg;
    ProgressBar progressBar;
    Constant constant = Constant.getInstance();

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

        current_per_frg = rootView.findViewById(R.id.txt_per);
        current_per_frg.setText(String.valueOf(constant.getCurrentBatteryStutus(requireContext())));

        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setProgress(constant.getCurrentBatteryStutus(requireContext()));

        ImageView img_settings = rootView.findViewById(R.id.img_settings);
        img_settings.setOnClickListener(this);

        //TODO Alarm Slient or not
        Switch sw_slient = rootView.findViewById(R.id.sw_slient);
        sw_slient.setChecked(sh.getSlientSwich(requireContext()));
        sw_slient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sh.setSlientSwich(requireContext(), b);
            }
        });

        //TODO Device in flash light available or not
        setFlashLight();

        //TODO Alarm play Flash light on of off
        Switch sw_flash = rootView.findViewById(R.id.sw_flash);
        sw_flash.setChecked(sh.getFlashLight(requireContext()));
        sw_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sh.setFlashLight(requireContext(), b);

            }
        });

        //TODO Which type of alarm set for per or time
        final RelativeLayout timer_view = rootView.findViewById(R.id.timer_view);
        timer_view.setOnClickListener(this);

        final RelativeLayout persantage_view = rootView.findViewById(R.id.persantage_view);
        persantage_view.setOnClickListener(this);

        //TODO Which type of alarm set selection
        RadioGroup alarm_button_view = rootView.findViewById(R.id.alarm_button_view);
        if (sh.getAlarmMethod(requireContext()).equalsIgnoreCase("SET_PERCNT")){
            RadioButton radioButton = rootView.findViewById(R.id.persantage);
            radioButton.setChecked(true);
            timer_view.setVisibility(View.INVISIBLE);
            persantage_view.setVisibility(View.VISIBLE);
            sh.setAlarmMethod(requireContext() , "SET_PERCNT");
        }else {
            RadioButton radioButton = rootView.findViewById(R.id.timer);
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

        //TODO SET alarm function
        Button set_alarm = rootView.findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(this);

        NumberPicker numberPicker = new NumberPicker(requireContext());
        PerPickerCustomDialog.setPreValueSelected(requireContext() , numberPicker);

        entertime.setText(selectedmin * 5  + " min ");
        enterPer.setText(selectdValue + " %");

        new DBHelper(requireContext()).getAllAlarmData();

        return rootView;
    }

    private void setAlarm() {
        DBHelper dbHelper = new DBHelper(requireContext());
        if (sh.getAlarmAlwardySet(requireContext())) {
            Log.e("BATTERY constant.lastID" , Constant.lastID + " xx");
            dbHelper.updateAlarmStatus("DISCARD" , Constant.lastID);
            String set_alarm = dbHelper.getPerticularAlarmType(String.valueOf(Constant.lastID));
            Log.e("BATTERY" , set_alarm + " xx");
            if (set_alarm.equalsIgnoreCase("PERCENTAGE")) {
                sh.setAlarmPercentage(requireContext(), 0 );
            }
            else if (set_alarm.equalsIgnoreCase("TIME")){
                AlarmReceiver.cancelReminderAlarm(requireContext() , Constant.lastID);
            }
            setAlarmAllData();
        }
        else {
            setAlarmAllData();
        }
    }

    private void setAlarmAllData() {
        sh.setAlarmAlwardySet(requireContext() , true);

        AlarmData alarmData = new AlarmData();

        Random random = new Random();
        int unique_id = random.nextInt(900) + 100;
        alarmData.setUnique_id(unique_id);

        SimpleDateFormat sdf = new SimpleDateFormat(" MMM dd yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        alarmData.setCurrent_date_time(currentDateandTime);

        alarmData.setCurrent_percentage(constant.getCurrentBatteryStutus(requireContext()));
        alarmData.setAlarm_states("RUNNING");

        if (sh.getAlarmMethod(requireContext()).equalsIgnoreCase("SET_PERCNT")) {
            alarmData.setAlarm_type("PERCENTAGE");
            alarmData.setTarget_percentage(selectdValue);
            alarmData.setSelected_hour(0);
            alarmData.setSelected_minute(0);
            // TODO Alarm set for selected Percentage
            sh.setAlarmPercentage(requireContext(), selectdValue);

            WakeLocker.acquire(requireContext());

            Intent activateAlarm = new Intent(getContext(), ActivateAlarmActivity.class);
            startActivity(activateAlarm);
        } else {
            // TODO Alarm set for perticular time
            alarmData.setTarget_percentage(0);
            alarmData.setAlarm_type("TIME");
            alarmData.setSelected_hour(selectedhour);
            if (selectedmin == 1) {
                alarmData.setSelected_minute(selectedmin * 5);
            }
            else {
                alarmData.setSelected_minute(selectedmin);
            }

            AlarmReceiver.setAlarm(getContext(), alarmData);

            WakeLocker.acquire(requireContext());

            Intent activateAlarm = new Intent(getContext(), ActivateAlarmActivity.class);
            startActivity(activateAlarm);
        }
        DBHelper dbHelper = new DBHelper(requireContext());
        dbHelper.addAlarm(alarmData);
    }

    private void alertDialogShow(final String compare) {
        if (compare.equalsIgnoreCase("time")) {
            TimePickerCustomDialog timePickerCustomDialog = new TimePickerCustomDialog(requireContext() );
            timePickerCustomDialog.show();
            timePickerCustomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (selectvaluedonetime) {
                        if (selectedhour != 0 )
                            entertime.setText(selectedhour + " h " + selectedmin + " min ");
                        else if (selectedhour == 0 )
                            entertime.setText(selectedmin + " min ");
                    }
                }
            });

        }
        else {
            PerPickerCustomDialog perPickerCustomDialog = new PerPickerCustomDialog(requireContext());
            perPickerCustomDialog.setCanceledOnTouchOutside(false);
            perPickerCustomDialog.show();
            perPickerCustomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (selectvaluedone){
                        enterPer.setText(selectdValue + " %");
                    }
                    else {
                        selectdValue = 0 ;
                    }
                }
            });
        }
    }

    private void setFlashLight() {
        boolean isFlashAvailable = requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        if (!isFlashAvailable) {
            rootView.findViewById(R.id.flash_view).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_settings:
                SettingDialog settingDialog = new SettingDialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen ,getActivity());
                settingDialog.show();
                break;
            case R.id.set_alarm:
                setAlarm();
                break;
            case R.id.persantage_view:
                alertDialogShow("per");
                break;
            case R.id.timer_view:
                alertDialogShow("time");
                break;
        }
    }
}