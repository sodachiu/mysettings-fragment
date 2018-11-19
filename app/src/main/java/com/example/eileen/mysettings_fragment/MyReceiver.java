package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "qll_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive: hello");
        switch (action){
            case Intent.ACTION_TIME_TICK:
                Log.i(TAG, "onReceive: 时间增加一分钟");
                break;
        }
    }
}
