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
import android.widget.ListView;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;

public class EthFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_eth_fragment";
    private Context mContext;
    FragmentActivity mActivity;
    private ListView lvMenu;
    private LinearLayout llSetNet;
    private LinearLayout llSetBluetooth;


    public EthFragment(){
        Log.i(TAG, "EthFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mActivity = (FragmentActivity) mContext;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_eth, container, false);
        initView(view);
        return view;
    }


    private void initView(View v){
        Log.i(TAG, "initView: ");
        llSetNet = (LinearLayout) v.findViewById(R.id.eth_ll_set_net);
        llSetBluetooth = (LinearLayout) v.findViewById(R.id.eth_ll_set_bluetooth);
        lvMenu = (ListView) mActivity.findViewById(R.id.main_lv_menu);
        Log.i(TAG, "initView: 菜单是否可获得焦点---->" + lvMenu.isFocusable());
        llSetNet.setOnClickListener(this);
        llSetBluetooth.setOnClickListener(this);

        String preFragment = FragmentUtil.getPreviousFragment();
        if (preFragment.equals(FragmentUtil.ETH_TYPE_FRAGMENT)){
            llSetNet.requestFocus();
        }else if (preFragment.equals(FragmentUtil.ETH_BLUETOOTH_FRAGMENT)){
            llSetBluetooth.requestFocus();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.eth_ll_set_net:
                //调用下一层碎片
                Log.i(TAG, "onClick: 设置有线网络");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_TYPE_FRAGMENT);
                break;
            case R.id.eth_ll_set_bluetooth:
                //调用设置蓝牙的碎片
                Log.i(TAG, "onClick: 设置蓝牙");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_BLUETOOTH_FRAGMENT);
                break;
            default:
                Log.i(TAG, "onClick: 不知道点击了什么东西");
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        Log.i(TAG, "onHiddenChanged: ");
        if (!hidden && lvMenu.hasFocus()){
            llSetNet.setFocusable(false);
            llSetBluetooth.setFocusable(false);
        }else {
            llSetBluetooth.setFocusable(true);
            llSetNet.setFocusable(true);
        }
    }
    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
