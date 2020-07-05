package com.example.batteryalarmclock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.templates.Constant;

import java.util.ArrayList;
import java.util.List;

public class PerPickerCustomDialog extends Dialog {
    Context context;
    public static int selectdValue;
    public static boolean selectvaluedone = false ;
    NumberPicker numberPicker;

    public PerPickerCustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_per_picker);

        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        final List<String> displayedValues = new ArrayList<>();
        for (int i = 10; i  <  110; i += 10 ) {
            displayedValues.add(String.format("%02d", i));
        }
        numberPicker.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));

        setPreValueSelected(context , numberPicker);

        selectdValue = numberPicker.getValue() * 10;
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectdValue = numberPicker.getValue() * 10;
            }
        });

        TextView ok_btn = findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectvaluedone = true ;
                dismiss();
            }
        });

        TextView cancle_btn = findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectvaluedone = false ;
                dismiss();
            }
        });
    }

    public static void setPreValueSelected(Context context , NumberPicker numberPicker)
    {
        int current_per = Constant.getInstance().getCurrentBatteryStutus(context);
        if (current_per > 0  && current_per < 10 ) {
            numberPicker.setValue(1);
            selectdValue = 10;
        }else if (current_per >= 10 && current_per < 20) {
            numberPicker.setValue(2);
            selectdValue = 20;
        }else if (current_per >= 20 && current_per < 30) {
            numberPicker.setValue(3);
            selectdValue = 30;
        }else if (current_per >= 30 && current_per < 40) {
            numberPicker.setValue(4);
            selectdValue = 40;
        }else if (current_per >= 40 && current_per < 50) {
            numberPicker.setValue(5);
            selectdValue = 50;
        }else if (current_per >= 50 && current_per < 60) {
            numberPicker.setValue(6);
            selectdValue = 60;
        }else if (current_per >= 60 && current_per < 70) {
            numberPicker.setValue(7);
            selectdValue = 70;
        }else if (current_per >= 70 && current_per < 80) {
            numberPicker.setValue(8);
            selectdValue = 80;
        }else if (current_per >= 80 && current_per < 90) {
            numberPicker.setValue(9);
            selectdValue = 90;
        }else if (current_per >= 90 && current_per < 100) {
            numberPicker.setValue(10);
            selectdValue = 100;
        }else {
            numberPicker.setValue(10);
            selectdValue = 100;
        }
    }
}