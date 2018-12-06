package com.example.eileen.mysettings_fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.hisilicon.android.hidisplaymanager.HiDisplayManager;

public class ScaleActivity extends Activity {

    private static final String TAG = "qll_scale_activity";
    private static final int MAX_HORIZONTAL = 150;
    private static final int MAX_VERTICAL = 75;
    private static final int EVERY_STEP = 1;
    private static final int MIN_HORIZONTAL = 0;
    private static final int MIN_VERTICAL = 0;

    private HiDisplayManager mHiDisplayManager;
    private int marginStart, marginEnd, marginTop, marginBottom;

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_scale);

        mHiDisplayManager = new HiDisplayManager();
        Rect outRangeRect = mHiDisplayManager.getGraphicOutRange();
        marginStart = outRangeRect.left;
        marginEnd = outRangeRect.right;
        marginTop = outRangeRect.top;
        marginBottom = outRangeRect.bottom;

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
        mHiDisplayManager.saveParam();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 左/上键增大边距，右/下键缩小边距
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                handleDown();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                handleUp();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                handleLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                handleRight();
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
        }

        return false;
    }

    void handleRight() {
        Log.i(TAG, "handleLeft: ");
        int resultMargin = marginStart - EVERY_STEP;
        if (resultMargin < MIN_HORIZONTAL) {
            marginStart = MIN_HORIZONTAL;
        }else {
            marginStart -= EVERY_STEP;
        }

        marginEnd = marginStart;
        setGraphicOutRange();
        
    }

    void handleLeft() {
        Log.i(TAG, "handleRight: ");
        int resultMargin = marginStart + EVERY_STEP;
        if (resultMargin > MAX_HORIZONTAL) {
            marginStart = MAX_HORIZONTAL;
        }else {
            marginStart += EVERY_STEP;
        }

        marginEnd = marginStart;
        setGraphicOutRange();

    }

    void handleUp() {
        Log.i(TAG, "handleUp: ");

        int resultMargin = marginTop + EVERY_STEP;
        if (resultMargin > MAX_VERTICAL) {
            marginTop = MAX_VERTICAL;
        }else {
            marginTop += EVERY_STEP;
        }

        marginBottom = marginTop;
        setGraphicOutRange();

    }

    void handleDown() {
        Log.i(TAG, "handleDown: ");
        int resultMargin = marginTop - EVERY_STEP;
        if (resultMargin < MIN_VERTICAL) {
            marginTop = MIN_VERTICAL;
        }else {
            marginTop -= EVERY_STEP;
        }

        marginBottom = marginTop;
        setGraphicOutRange();
    }

    void setGraphicOutRange() {
        mHiDisplayManager.setGraphicOutRange(marginStart
                , marginTop
                , marginEnd
                , marginBottom);
    }

}
