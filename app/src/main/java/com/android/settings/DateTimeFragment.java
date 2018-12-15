package com.android.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.settings.utils.FragmentUtil;

import java.net.ConnectException;
import java.util.Date;

public class DateTimeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_dt_fragment";
    private Context mContext;
    private TextView  tvTimeServer1, tvTimeServer2;
    private TextClock tcDateFormat;
    private ImageView imgUse24Switch;
    private LinearLayout llUse24Format, llSelectFormat;
    private ContentResolver mResolver;
    private boolean is24Format = false;
    public static final String TIME_12 = "12";
    public static final String TIME_24 = "24";

    public DateTimeFragment(){
        Log.i(TAG, "DateTimeFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_date_time, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstancesState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstancesState);
        initView();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mResolver = null;
        mContext = null;
    }

    
    void initView(){
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        tcDateFormat = (TextClock) activity.findViewById(R.id.dt_tc_select_date_format);
        tvTimeServer1 = (TextView) activity.findViewById(R.id.dt_tv_server1);
        tvTimeServer2 = (TextView) activity.findViewById(R.id.dt_tv_server2);
        imgUse24Switch = (ImageView) activity.findViewById(R.id.dt_img_use_24);
        llUse24Format = (LinearLayout) activity.findViewById(R.id.dt_ll_use_24);
        llSelectFormat = (LinearLayout) activity.findViewById(R.id.dt_ll_select_format);

        timeFormat();
        dateFormat();

        String server1 = Settings.Secure.getString(mResolver, "ntp_server");
        String server2 = Settings.Secure.getString(mResolver, "ntp_server2");

        Log.i(TAG, "initView: 主时间地址——" + server1);
        Log.i(TAG, "initView: 备时间地址——" + server2);

        if (server1 != null){
            tvTimeServer1.setText(server1);
        }else {
            tvTimeServer1.setText("");
        }

        if (server2 != null){
            tvTimeServer2.setText(server2);
        }else {
            tvTimeServer2.setText("");
        }

        llSelectFormat.setOnClickListener(this);
        llUse24Format.setOnClickListener(this);

        String preFragment = FragmentUtil.getPreviousFragment();
        if (preFragment.equals(FragmentUtil.DATE_FORMAT_FRAGMENT)){
            llSelectFormat.requestFocus();
        }

    }
    
    @Override
    public void onClick(View view){
        Log.i(TAG, "onClick: ");
        switch (view.getId()){
            case R.id.dt_ll_use_24:
                Log.i(TAG, "onClick: 点击切换时间格式");
                Settings.System.putString(mResolver,
                        android.provider.Settings.System.TIME_12_24,
                        is24Format ? TIME_12 : TIME_24);
                timeFormat();
                break;
            case R.id.dt_ll_select_format:
                Log.i(TAG, "onClick: 点击选择日期格式，尝试进入该fragment");
                FragmentUtil.showFragment(mContext, FragmentUtil.DATE_FORMAT_FRAGMENT);
                break;
            default:
                Log.i(TAG, "onClick: 点击了" + view.getId());
        }
    }

    /**
     * 使用 24 小时制与否的处理方法
     * */
    void timeFormat(){
        is24Format = DateFormat.is24HourFormat(mContext);
        Log.i(TAG, "timeFormat: 当前系统时间显示格式为24小时制吗？——" + is24Format);
        if (is24Format){
            // 时间显示为 24 小时制
            imgUse24Switch.setImageResource(R.drawable.checkbox_on);
        }else {
            // 时间显示为 12 小时制
            imgUse24Switch.setImageResource(R.drawable.checkbox_off);
        }
    }

    /**
     * 格式化日期的显示格式
     * */
    void dateFormat(){
        Log.i(TAG, "dateFormat: ");
        String nowFormat = Settings.System.getString(mResolver
                , Settings.System.DATE_FORMAT);
        String[] dateFormats = getResources().getStringArray(R.array.date_formats);
        if (nowFormat == null || nowFormat.equals("")){
            nowFormat = dateFormats[DateFormatFragment.DEFAULT_FORMAT_POSITION];
        }
        tcDateFormat.setFormat12Hour(nowFormat);
        tcDateFormat.setFormat24Hour(nowFormat);

        Settings.System.putString(mContext.getContentResolver()
                , Settings.System.DATE_FORMAT, nowFormat);
    }

}
