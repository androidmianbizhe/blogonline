package com.example.lihao.blogeronline.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lihao on 17-11-27.
 */

public class BatteryBroadCast extends BroadcastReceiver {

    public static final String BATTERY_LOW = "battery_low";
    public static final String BATTERY_MIDDLE = "battery_middle";
    public static final String BATTERY_HIGH = "battery_high";

    public static String BATTERY_TAG = BATTERY_LOW;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 如果捕捉到action是ACRION_BATTERY_CHANGED
        // 就运行onBatteryInfoReveiver()
        if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
            int intLevel = intent.getIntExtra("level", 0);
            int intScale = intent.getIntExtra("scale", 100);
            int state = intLevel * 100 / intScale;
            if(state < 15){

                BATTERY_TAG = BATTERY_LOW;

            } else if(state >=15 && state <60){

                BATTERY_TAG = BATTERY_MIDDLE;
            } else {

                BATTERY_TAG = BATTERY_HIGH;
            }

        }
    }
}
