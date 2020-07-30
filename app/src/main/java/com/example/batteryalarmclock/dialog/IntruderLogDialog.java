package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.adapter.IntruderAdapter;
import com.example.batteryalarmclock.model.IntruderData;
import com.example.batteryalarmclock.templates.Constant;
import com.example.batteryalarmclock.templates.DBHelper;
import com.example.batteryalarmclock.util.SharedPreferencesApplication;

import java.util.List;

public class IntruderLogDialog extends Dialog {
    private Context context;
    private Activity activity;

    public IntruderLogDialog(Context context, int themeResId, Activity activity) {
        super(context , themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_intruder_log);

        final ListView intruder_adapter = findViewById(R.id.intruder_adapter);
        List<IntruderData> intruderDataList = new DBHelper(context).getAllInruderdata();
        intruder_adapter.setAdapter(new IntruderAdapter(getContext(), this.activity, intruderDataList));

        RelativeLayout rel_back = findViewById(R.id.rel_back);
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView deleteall = findViewById(R.id.deleteall);
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DBHelper(context).deleteAllIntriderData();
                new DBHelper(context).closeDatabase();

                List<IntruderData> intruderDataList = new DBHelper(context).getAllInruderdata();
                intruder_adapter.setAdapter(new IntruderAdapter(getContext(), activity, intruderDataList));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        RelativeLayout rel_live_ad = findViewById(R.id.rel_live_ad);
        if (new SharedPreferencesApplication().getInAppDone(context)){
            rel_live_ad.setVisibility(View.INVISIBLE);
        }
        else {
            Constant.getInstance().loadBannerAd(rel_live_ad, context, activity);
        }
    }
}
