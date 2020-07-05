package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.batteryalarmclock.R;

public class HistoryLogDialog extends Dialog {
    public HistoryLogDialog(Context context, int themeResId, Activity activity) {
        super(context ,themeResId );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_history_log);
    }
}
