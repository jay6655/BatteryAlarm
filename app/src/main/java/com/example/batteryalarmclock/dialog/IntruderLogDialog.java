package com.example.batteryalarmclock.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.adapter.IntruderAdapter;
import com.example.batteryalarmclock.model.IntruderData;
import com.example.batteryalarmclock.templates.DBHelper;

import java.util.List;

public class IntruderLogDialog extends Dialog {
    private Context context;
    private Activity activity;
    private List<IntruderData> intruderData;

    public IntruderLogDialog(Context context, int themeResId, Activity activity) {
        super(context , themeResId);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_intruder_log);

        ListView intruder_adapter = findViewById(R.id.intruder_adapter);
        intruderData = new DBHelper(context).getAllInruderdata();
        intruder_adapter.setAdapter(new IntruderAdapter(getContext(), this.activity, intruderData));

        RelativeLayout rel_back = findViewById(R.id.rel_back);
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
