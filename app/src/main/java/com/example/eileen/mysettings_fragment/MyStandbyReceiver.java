package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;
import com.example.eileen.mysettings_fragment.utils.MyHandler;
import com.example.eileen.mysettings_fragment.utils.ShowDialog;
import com.example.eileen.mysettings_fragment.utils.UniqueMark;
import com.google.android.collect.Sets;

import java.util.Arrays;
import java.util.Set;

public class MyStandbyReceiver extends BroadcastReceiver {

    private static final String TAG = "qll_standby";
    private static final String AUTO_SLEEP = "com.cbox.action.autosleep";
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action == null || action.equals("")) {
            Log.i(TAG, "onReceive: 没有接收到任何广播");
            return;
        }

        if (action.equals(AUTO_SLEEP) || action.equals(Intent.ACTION_SHUTDOWN)) {
            Log.i(TAG, "onReceive: 收到广播----" + action);
            // 弹框提醒
            Log.i(TAG, "onReceive: context----" + context);
            Intent callIntent = new Intent(context, DialogActivity.class);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
            Log.i(TAG, "onReceive: 弹框成功");

        }
    }
}
