package com.example.batteryalarmclock.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.batteryalarmclock.R;
import com.example.batteryalarmclock.model.IntruderData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class IntruderAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    List<IntruderData> intruderDataList;
    LayoutInflater inflater ;

    private Picasso mPicasso;

    public IntruderAdapter(Context context, Activity activity, List<IntruderData> intruderDataList) {
        this.context = context;
        this.activity = activity;
        this.intruderDataList = intruderDataList;
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (context != null) {
            Picasso.Builder builder = new Picasso.Builder(context);
            mPicasso = builder.build();
        }

    }

    @Override
    public int getCount() {
        return intruderDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        final ViewHolder holder;
        if(view == null){
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.intruder_log_layout, null);
            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.img_intruder_photo = vi.findViewById(R.id.img_photo);
            holder.txt_date = vi.findViewById(R.id.txt_date);
            holder.txt_time = vi.findViewById(R.id.txt_time);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else{
            holder =(ViewHolder)vi.getTag();
        }

        IntruderData intruderData = intruderDataList.get(i);

        Uri uri = Uri.fromFile(new File(intruderData.getIntruder_path()));
        Picasso.get().load(uri)
                .rotate(180)
                .resize(96, 96).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).
                into(holder.img_intruder_photo);

        holder.txt_date.setText(intruderData.getIntruder_date());
        holder.txt_time.setText(intruderData.getIntruder_time());


        return vi;
    }

    public static class ViewHolder{
        public TextView txt_app_name,txt_date,txt_time;
        public ImageView img_intruder_photo,app_icon;
    }
}
