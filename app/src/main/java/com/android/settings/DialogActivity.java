package com.android.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DialogActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "qll_dialog_ac";

    private TextView tvTimerText;
    private Button btnContinueWatch;

    public static final int MILLIS_IN_FUTURE = 60000;
    public static final int COUNTDOWN_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle args) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(args);
        setContentView(R.layout.activity_dialog);

        initDialogSize();
        tvTimerText = (TextView) findViewById(R.id.advanced_tv_timer);
        btnContinueWatch = (Button) findViewById(R.id.advanced_btn_watch);

        btnContinueWatch.setOnClickListener(this);
        timer.start();

    }

    void initDialogSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels * 0.5);
        int height = (int) (dm.heightPixels * 0.5);
        getWindow().setLayout(width, height);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advanced_btn_watch:
                timer.cancel();
                finish();
                break;
            default:
                Log.i(TAG, "onClick: 点击了其它控件----" + v.getId());
        }
    }

    CountDownTimer timer = new CountDownTimer(MILLIS_IN_FUTURE, COUNTDOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            String timerText = getString(R.string.advanced_dialog_time_text);
            timerText = String.format(timerText, (millisUntilFinished / 1000));
            tvTimerText.setText(timerText);
        }

        @Override
        public void onFinish() {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            pm.goToSleep(SystemClock.uptimeMillis());
        }
    };

}
