package com.example.batteryalarmclock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmData implements Parcelable {

    private int id  ;
    private String type;
    private long time;

    public AlarmData() {
    }


    /*public AlarmData(Parcel in) {
        id = in.readInt();
        type  = in.readString();
        time = in.readLong();
    }*/

    public static final Creator<AlarmData> CREATOR = new Creator<AlarmData>() {
        @Override
        public AlarmData createFromParcel(Parcel in) {
            return new AlarmData();
        }

        @Override
        public AlarmData[] newArray(int size) {
            return new AlarmData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(type);
        parcel.writeLong(time);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AlarmData{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
