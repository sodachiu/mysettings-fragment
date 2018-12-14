package com.example.eileen.mysettings_fragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hisilicon.android.hidisplaymanager.HiDisplayManager;

import java.util.Timer;
import java.util.TimerTask;

public class MonitorTvStateService extends Service {

    private static final String TAG = "qll_service";
    private static final int MILLIS_IN_FUTURE = 300000;
    private static final int INTERVAL_TIME = 1000;
    private static final int DELAY_TIME = 0;

    private HiDisplayManager mHiDisplayManager;


    public MonitorTvStateService() {

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mHiDisplayManager = new HiDisplayManager();
        super.onCreate();
        Log.i(TAG, "onCreate: 服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        monitorThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 服务停止");
        super.onDestroy();
        
    }

    private Thread monitorThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: 线程开启");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    countDownTimer.cancel();
                    int tvStatus = mHiDisplayManager.getTVStatus();
                    Log.i(TAG, "run: 线程----" + Thread.currentThread().getId()
                            +"电视机状态----" + tvStatus);
                    if (tvStatus == 0 || tvStatus == -1) {
                        countDownTimer.start();
                        cancel();
                    }
                }
            }, DELAY_TIME, INTERVAL_TIME);
        }
    });

    private CountDownTimer countDownTimer = new CountDownTimer(MILLIS_IN_FUTURE, INTERVAL_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {

            int tvStatus = mHiDisplayManager.getTVStatus();
            Log.i(TAG, "onTick: 待机倒计时----" + millisUntilFinished/1000
                    + " && tvstatus----" + tvStatus);
            if (tvStatus != -1 && tvStatus != 0) {
                Log.i(TAG, "onTick: 重新开始计时");
                monitorThread.run();
            }
        }

        @Override
        public void onFinish() {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            pm.goToSleep(SystemClock.uptimeMillis());
        }
    };

}
