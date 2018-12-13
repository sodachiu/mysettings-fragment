package com.example.eileen.mysettings_fragment.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;

public class MyDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "qll_dialog";

    public static final String TITLE = "title";
    public static final String PROMPT_INFOS = "prompt_infos";
    public static final String TOTAL_SECOND = "total_seconds";
    public static final String TIMER_TEXT = "timer_text";
    public static final String PARCELABLE = "parcelable";
    public static final int INTERVAL_TIME = 1000; // 间隔时间 1s

    private TextView tvTitle, tvTimerText, tvTimerNum;
    private Button btnConfirm, btnCancel;
    private LinearLayout llMainInfo, llTimer;
    private Dialog mDialog;
    private CountDownTimer mTimer;
    private Fragment mTargetFragment;
    private Intent resultIntent;

    public static MyDialog getInstance(String title, String[] promptInfos,
                                       String timerText, int totalSecond, Parcelable parcelable) {

        Log.i(TAG, "getInstance: ");
        MyDialog dialog = new MyDialog();

        Bundle dialogArgs = new Bundle();
        dialogArgs.putString(TITLE, title);
        dialogArgs.putStringArray(PROMPT_INFOS, promptInfos);
        dialogArgs.putString(TIMER_TEXT, timerText);
        dialogArgs.putInt(TOTAL_SECOND, totalSecond);
        dialogArgs.putParcelable(PARCELABLE, parcelable);
//        dialogArgs.putSerializable(OBJECT, object);
        dialog.setArguments(dialogArgs);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        Log.i(TAG, "onCreateView: ");
        mDialog = getDialog();
        mTargetFragment = getTargetFragment();
        if (mTargetFragment == null) {
            return null;
        }
        Log.i(TAG, "onCreateView: targetFragment----" + mTargetFragment.getTag());

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        View view = inflater.inflate(R.layout.fragment_my_dialog, null);

        tvTitle = (TextView) view.findViewById(R.id.dialog_tv_title);
        tvTimerNum = (TextView) view.findViewById(R.id.dialog_tv_time_num);
        tvTimerText = (TextView) view.findViewById(R.id.dialog_tv_time_text);
        btnConfirm = (Button) view.findViewById(R.id.dialog_btn_confirm);
        btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        llMainInfo = (LinearLayout) view.findViewById(R.id.dialog_ll_main_info);
        llTimer = (LinearLayout) view.findViewById(R.id.dialog_ll_time_info);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initView();

        return view;
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
        if (mDialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            mDialog.getWindow().setLayout((int) (dm.widthPixels * 0.55)
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_confirm:
                handleConfirmEvent();
                break;
            case R.id.dialog_btn_cancel:
               handleCancelEvent();
                break;

        }
    }

    void initView() {
        Log.i(TAG, "initView: ");
        Bundle bundle = getArguments();

        String[] propmtInfo = bundle.getStringArray(PROMPT_INFOS);
        String title = bundle.getString(TITLE);
        String timeText = bundle.getString(TIMER_TEXT);
        Parcelable parcelable = bundle.getParcelable(PARCELABLE);
        int totalSecond = bundle.getInt(TOTAL_SECOND);

        resultIntent = new Intent();
        resultIntent.putExtra(PARCELABLE, parcelable);

        // 为title赋值
        if (title != null && !title.equals("")) {
            tvTitle.setText(title);
        }

        // 为提示信息赋值
        if (propmtInfo != null) {
            for (String info : propmtInfo) {
                TextView tv = new TextView(getContext());
                tv.setText(info);
                tv.setTextSize(23);
                tv.setTextColor(getResources().getColor(R.color.white));
                llMainInfo.addView(tv);
            }
        }

        // 为倒计时赋值 （如果需要倒计时的话）
        if (totalSecond > 0) {
            showCountDownTimer(timeText, totalSecond);
        }

    }

    /**
     * 显示倒计时的控件
     * */
    void showCountDownTimer(String text, int time) {
        Log.i(TAG, "showCountDownTimer: ");

        llTimer.setVisibility(View.VISIBLE);
        tvTimerText.setText(text);

        mTimer = new CountDownTimer(time * 1000, INTERVAL_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                String formatTime = String.format("%02d", (millisUntilFinished / 1000));
                tvTimerNum.setText(formatTime);
            }

            @Override
            public void onFinish() {
                // 当作取消操作处理
                mDialog.dismiss();
            }
        };
        mTimer.start();
    }

    /**
     * 处理用户点击的确定事件
     * */
    void handleConfirmEvent() {
        Log.i(TAG, "handleConfirmEvent: ");

        String targetFragmentTag = mTargetFragment.getTag();
        if (targetFragmentTag == null || targetFragmentTag.equals("")) {
            return;
        }

        Log.i(TAG, "handleConfirmEvent: targetfragment----" + targetFragmentTag);
        switch (targetFragmentTag) {
            case FragmentUtil.DISPLAY_RESOLUTION_FRAGMENT:
                Log.i(TAG, "handleConfirmEvent: 我进来了");
                mTargetFragment.onActivityResult(UniqueMark.RESOLUTION_FRAGMENT
                        , Activity.RESULT_OK
                        , resultIntent);
                break;
            case FragmentUtil.ETH_BLUETOOTH_FRAGMENT:
                // 传一个bluetoothdevice回去
                mTargetFragment.onActivityResult(UniqueMark.ETH_BLUETOOTH_FRAGMENT
                        , Activity.RESULT_OK
                        , resultIntent);
                break;
            case FragmentUtil.STORAGE_FRAGMENT:
                mTargetFragment.onActivityResult(UniqueMark.STORAGE_FRAGMENT
                        , Activity.RESULT_OK
                        , resultIntent);
        }
        mDialog.dismiss();

    }

    /**
     * 处理用户点击的取消事件
     * */
    void handleCancelEvent() {
        
        String targetFragmentTag = mTargetFragment.getTag();
        if (targetFragmentTag == null || targetFragmentTag.equals("")) {
            return;
        }

        Log.i(TAG, "handleConfirmEvent: targetfragment----" + targetFragmentTag);

        switch (mTargetFragment.getTag()) {
            case FragmentUtil.DISPLAY_RESOLUTION_FRAGMENT:
                Log.i(TAG, "handleCancelEvent: 我进来了");
                mTargetFragment.onActivityResult(UniqueMark.RESOLUTION_FRAGMENT
                        , Activity.RESULT_CANCELED
                        , resultIntent);
                break;
            case FragmentUtil.ETH_BLUETOOTH_FRAGMENT:
                mTargetFragment.onActivityResult(UniqueMark.ETH_BLUETOOTH_FRAGMENT
                        , Activity.RESULT_CANCELED
                        , resultIntent);
                break;
            case FragmentUtil.STORAGE_FRAGMENT:
                mTargetFragment.onActivityResult(UniqueMark.STORAGE_FRAGMENT
                        , Activity.RESULT_CANCELED
                        , resultIntent);
                break;
        }
        mDialog.dismiss();

    }

}
