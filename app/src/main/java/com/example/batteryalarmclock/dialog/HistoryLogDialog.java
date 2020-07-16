package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.adapter.LogHistoryAdapter;
import com.example.batteryalarmclock.model.AlarmData;
import com.example.batteryalarmclock.templates.DBHelper;

import java.util.List;

public class HistoryLogDialog extends Dialog {

    List<AlarmData> alarmDataArrayList;
    Context context;

    public HistoryLogDialog(Context context, int themeResId, Activity activity) {
        super(context ,themeResId );
        this.context = context ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_history_log);

        Log.e("hfkj" , "HistoryLogDialog  ");

        RelativeLayout rel_back = findViewById(R.id.rel_back);
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        alarmDataArrayList = new DBHelper(context).getAllAlarmData();

        RecyclerView recycle_history = findViewById(R.id.recycle_history);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycle_history.setLayoutManager(mLayoutManager);
        recycle_history.setItemAnimator(new DefaultItemAnimator());
        LogHistoryAdapter logHistoryAdapter = new  LogHistoryAdapter(context , alarmDataArrayList);
        recycle_history.setAdapter(logHistoryAdapter);

        logHistoryAdapter.notifyDataSetChanged();

        Log.e("hfkj" , "HistoryLogDialogs ffsgfsg   ");

    }

}
