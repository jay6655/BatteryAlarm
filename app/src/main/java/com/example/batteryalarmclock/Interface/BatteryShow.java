package com.example.batteryalarmclock.Interface;

public class BatteryShow {

    GetbatteryPesebtage getbatteryPesebtage = new GetbatteryPesebtage() {
        @Override
        public void batteryPersentage(float current_bettery) {
        }
    };

    public interface GetbatteryPesebtage{
        void batteryPersentage(float current_bettery);
    }

    public void myRecord(int set) {
        getbatteryPesebtage.batteryPersentage(set);  //now this works.
    }
}
