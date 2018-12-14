package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;

public class AdvancedFragment2 extends Fragment implements View.OnClickListener{

    private static final String TAG = "qll_advanced_2";

    private Context mContext;
    private LinearLayout llClearAll;
    private RelativeLayout rvSleep1, rvSleep2;
    private ImageView imgSleep1, imgSleep2;
    private HiDisplayManager mHdDisplayManager;
    public static final String PROP_NOOP = "persist.sys.suspend.noop";
    private boolean mIsSleep1Open, mIsSleep2Open;
    public static final int HDMI_SUSPEND_TRUE = 1;
    public static final int HDMI_SUSPEND_FALSE = 0;
    private static final int HDMI_SUSPEND_TIME = 300;
    private static final String PROP_NOOP_TRUE = "true";
    private static final String PROP_NOOP_FALSE = "false";


    public AdvancedFragment2() {

    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mHdDisplayManager = new HiDisplayManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_advanced2, parent, false);
        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();
    }


    void initView() {
        FragmentActivity activity = getActivity();
        llClearAll = (LinearLayout) activity.findViewById(R.id.advanced_ll_clear_all);
        rvSleep1 = (RelativeLayout) activity.findViewById(R.id.advanced_rv_sleep1);
        rvSleep2 = (RelativeLayout) activity.findViewById(R.id.advanced_rv_sleep2);
        imgSleep1 = (ImageView) activity.findViewById(R.id.advanced_img_sleep1);
        imgSleep2 = (ImageView) activity.findViewById(R.id.advanced_img_sleep2);

        llClearAll.requestFocus();

        llClearAll.setOnClickListener(this);
        rvSleep1.setOnClickListener(this);
        rvSleep2.setOnClickListener(this);

        int isSleep1Open = mHdDisplayManager.getHDMISuspendEnable();
        if (isSleep1Open == HDMI_SUSPEND_TRUE) {
            mIsSleep1Open = true;
            imgSleep1.setImageResource(R.drawable.checkbox_on);
        } else {
            mIsSleep1Open = false;
            imgSleep1.setImageResource(R.drawable.checkbox_off);
        }

        String isSleep2Open = SystemProperties.get(PROP_NOOP);
        if (isSleep2Open.equals(PROP_NOOP_TRUE)) {
            mIsSleep2Open = true;
            imgSleep2.setImageResource(R.drawable.checkbox_on);
        } else {
            mIsSleep2Open = false;
            imgSleep2.setImageResource(R.drawable.checkbox_off);
        }
        mHdDisplayManager.setHDMISuspendTime(HDMI_SUSPEND_TIME);



    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.advanced_ll_clear_all:
                FragmentUtil.showFragment(mContext, FragmentUtil.ADVANCED_CLEAT_ALL);
                break;
            case R.id.advanced_rv_sleep1:
                switchSleep1State();
                break;
            case R.id.advanced_rv_sleep2:
                switchSleep2State();
                break;
            default:
                Log.i(TAG, "onClick: 点击了其它控件----" + view.getId());
        }
    }

    void switchSleep1State() {

        Log.i(TAG, "switchSleep1State: ");
        Intent intent = new Intent(mContext, MonitorTvStateService.class);

        if (mIsSleep1Open) {
            mHdDisplayManager.setHDMISuspendEnable(HDMI_SUSPEND_FALSE);
            imgSleep1.setImageResource(R.drawable.checkbox_off);
            mContext.stopService(intent);
        } else {
            mHdDisplayManager.setHDMISuspendEnable(HDMI_SUSPEND_TRUE);
            imgSleep1.setImageResource(R.drawable.checkbox_on);
            mContext.startService(intent);
        }

        mIsSleep1Open = !mIsSleep1Open;

    }

    void switchSleep2State() {
        Log.i(TAG, "switchSleep2State: ");
        if (mIsSleep2Open) {
            SystemProperties.set(PROP_NOOP, PROP_NOOP_FALSE);
            imgSleep2.setImageResource(R.drawable.checkbox_off);
        } else {
            SystemProperties.set(PROP_NOOP, PROP_NOOP_TRUE);
            imgSleep2.setImageResource(R.drawable.checkbox_on);
        }

        mIsSleep2Open = !mIsSleep2Open;
    }


}
