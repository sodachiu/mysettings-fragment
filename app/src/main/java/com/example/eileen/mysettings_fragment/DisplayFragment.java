package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;

import java.net.ConnectException;

public class DisplayFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_display_fragment";
    private Context mContext;
    private LinearLayout llResolution, llScale;

    public DisplayFragment(){
        Log.i(TAG, "DisplayFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_display, parent, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        initView();
    }

    void initView(){
        FragmentActivity activity = getActivity();
        llResolution = (LinearLayout) activity.findViewById(R.id.display_ll_resolution);
        llScale = (LinearLayout) activity.findViewById(R.id.display_ll_scale);

        llResolution.setOnClickListener(this);
        llScale.setOnClickListener(this);

        String preFragment = FragmentUtil.getPreviousFragment();
        if (preFragment.equals(FragmentUtil.DISPLAY_RESOLUTION_FRAGMENT)){
            llResolution.requestFocus();
        }

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.display_ll_resolution:
                // 进入设置分辨率的 fragment
                Log.i(TAG, "onClick: 尝试进入分辨率Fragment");
                FragmentUtil.showFragment(mContext, FragmentUtil.DISPLAY_RESOLUTION_FRAGMENT);
                break;
            case R.id.display_ll_scale:
                // 进入新的activity
                Log.i(TAG, "onClick: 尝试进入调整画面输出区域的 activity");
                break;
            default:
                Log.i(TAG, "onClick: 点击了----" + view.getId());
        }
    }
}
