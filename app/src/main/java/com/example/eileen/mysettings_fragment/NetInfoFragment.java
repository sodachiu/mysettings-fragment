package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.network.NetworkUtil;
import com.example.eileen.mysettings_fragment.utils.MyHandler;

public class NetInfoFragment extends Fragment {
    private static final String TAG = "qll_netinfo_fragment";
    private Context mContext;
    private TextView tvTitle, tvIp, tvMask, tvGateway, tvDns1, tvDns2;
    private boolean isNetAvailable = true;
    private ContentResolver resolver;
    private MyHandler mHandler;

    public NetInfoFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        resolver = mContext.getContentResolver();
        mHandler = new MyHandler(mContext);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        mContext.registerReceiver(myNetReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_net_info, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        resolver = null;
        mContext.unregisterReceiver(myNetReceiver);
    }

    private void initView(View view){
        Log.i(TAG, "initView: ");
        tvTitle = (TextView) view.findViewById(R.id.netinfo_tv_title);
        tvIp = (TextView) view.findViewById(R.id.netinfo_tv_ip);
        tvMask = (TextView) view.findViewById(R.id.netinfo_tv_mask);
        tvGateway = (TextView) view.findViewById(R.id.netinfo_tv_gateway);
        tvDns1 = (TextView) view.findViewById(R.id.netinfo_tv_dns1);
        tvDns2 = (TextView) view.findViewById(R.id.netinfo_tv_dns2);
        setValues();

    }

    void setValues(){
        Log.i(TAG, "setValues: ");
        NetworkUtil.MyDhcpInfo myDhcpInfo = new NetworkUtil.MyDhcpInfo(mContext, isNetAvailable);
        String sIp = myDhcpInfo.getIpAddress();
        String sMask = myDhcpInfo.getNetMask();
        String sGateway = myDhcpInfo.getNetMask();
        String sDns1 = myDhcpInfo.getDns1();
        String sDns2 = myDhcpInfo.getDns2();

        tvIp.setText(sIp);
        tvMask.setText(sMask);
        tvGateway.setText(sGateway);
        tvDns1.setText(sDns1);
        tvDns2.setText(sDns2);
        
        String netInfoTitle = getString(R.string.netinfo_eth_connected);
        if (isNetAvailable){
            Log.i(TAG, "setValues: 网络已连接");
            int ethMode = Settings.Secure.getInt(resolver, "default_eth_mod", 0);
            switch (ethMode){
                case NetworkUtil.ETHERNET_MODE_DHCP:
                    Log.i(TAG, "setValues: 当前网络连接模式——DHCP");
                    netInfoTitle = String.format(netInfoTitle, getString(R.string.eth_dhcp));
                    break;
                case NetworkUtil.ETHERNET_MODE_STATIC:
                    Log.i(TAG, "setValues: 当前网络连接模式——静态IP");
                    netInfoTitle = String.format(netInfoTitle, getString(R.string.eth_static));
                    break;
                case NetworkUtil.ETHERNET_MODE_PPPOE:
                    Log.i(TAG, "setValues: 当前网络连接模式——PPPoE");
                    netInfoTitle = String.format(netInfoTitle, getString(R.string.eth_pppoe));
                    break;
                default:
                    Log.e(TAG, "setValues: 未知网络连接模式");
                    netInfoTitle = String.format(netInfoTitle, "");
                    
            }
        }else {
            Log.i(TAG, "setValues: 网络未连接");
            netInfoTitle = getString(R.string.netinfo_eth_no_connect);
        }
        
        tvTitle.setText(netInfoTitle);
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接收广播——" + action);
            int ethMode = Settings.Secure.getInt(resolver, "default_eth_mod", -1);
            int ethEvent;
            switch (ethMode){
                case NetworkUtil.ETHERNET_MODE_DHCP:
                    ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                    break;
                case NetworkUtil.ETHERNET_MODE_STATIC:
                    ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                    break;
                case NetworkUtil.ETHERNET_MODE_PPPOE:
                    ethEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
                    break;
                default:
                    ethEvent = 1;
            }

            isNetAvailable = (ethEvent == EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED
                    || ethEvent == EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED
                    || ethEvent == PppoeManager.EVENT_CONNECT_SUCCESSED
                    || ethEvent == EthernetManager.EVENT_PHY_LINK_UP);

            if (ethEvent == EthernetManager.EVENT_PHY_LINK_DOWN){
                mHandler.sendEmptyMessage(MyHandler.LINK_DOWN);
            }

            setValues();
        }
    };
}
