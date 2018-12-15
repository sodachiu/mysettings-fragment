package com.changhong.netchecking;

import android.app.Activity;
import android.content.Context;
import android.net.ethernet.EthernetManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "netcheck";
    private Context mContext = null;
    private TextView checkingTextV = null;
    private EthernetManager mEthernetManager = null;
    private int mTimeCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemProperties.set("sys.ch.boot.waiting", "true");
        mContext = this;
        initView();
        initTimeCount();
        initGloble();

        checkingHandler.sendEmptyMessage(10);
    }

    private void initView()
    {
        checkingTextV = (TextView) findViewById(R.id.chechingText);
    }

    private void initTimeCount()
    {
        mTimeCount = SystemProperties.getInt("persist.sys.ch.boot.wait.time",0);
        if(mTimeCount == 0)
        {
            exitApplication();
        }
    }

    private void initGloble()
    {
        mEthernetManager = (EthernetManager)getSystemService(Context.ETHERNET_SERVICE);
    }

    private void exitApplication()
    {
        SystemProperties.set("sys.ch.boot.waiting", "false");
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler checkingHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case 10://Count down
                {
                    Log.i(TAG, " case 10");
                    if(mTimeCount > 0)
                    {
                        mTimeCount --;
                        if(checkingTextV != null) {
                            checkingTextV.setText(getString(R.string.str_netchecking) + mTimeCount);
                        }
                        Log.i(TAG, " time wait:"+mTimeCount);
                        this.sendEmptyMessageDelayed(10, 1000);
                    }
                    else
                    {
                        if(mEthernetManager != null)
                        {
                            Log.i(TAG, " set ethernet disabled");
                            mEthernetManager.setEthernetEnabled(false);
                        }
                        Log.i(TAG, " delay 3000");
                        this.sendEmptyMessageDelayed(11, 3000);
                    }
                    break;
                }

                case 11://enable ethernet
                {
                    Log.i(TAG, " case 11");
                    if(mEthernetManager != null)
                    {
                        Log.i(TAG, " set ethernet enabled");
                        mEthernetManager.setEthernetEnabled(true);
                    }
                    Log.i(TAG, " delay 2000");
                    this.sendEmptyMessageDelayed(12, 5000);
                    break;
                }

                case 12://finish
                {
                    Log.i(TAG, " case 12,exit");
                    exitApplication();
                    break;
                }
            }


        }
    };
}
