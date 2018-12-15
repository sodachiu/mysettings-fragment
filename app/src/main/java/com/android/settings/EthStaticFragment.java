package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.settings.network.NetworkUtil;
import com.android.settings.utils.EditUtil;
import com.android.settings.utils.FragmentUtil;
import com.android.settings.utils.MyHandler;

import java.net.Inet4Address;
import java.net.InetAddress;

public class EthStaticFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_eth_static_fragment";
    private Context mContext;
    private boolean isNetConnected = false;
    private EthernetManager mEthManager;
    private IntentFilter mFilter;
    private String workMask;
    private MyHandler mHandler;

    private EditText etIP, etMask, etGateway, etDns1, etDns2;
    private Button btnConfirm, btnCancel;
    private ContentResolver resolver;

    public EthStaticFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        resolver = mContext.getContentResolver();
        isNetConnected = NetworkUtil.checkNetConnect(mContext);
        mHandler = new MyHandler(mContext);
        mEthManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        mFilter = new IntentFilter();
        mFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        mFilter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        mContext.registerReceiver(myNetReceiver, mFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_eth_static, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        initView();
    }
    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        resolver = null;
        mHandler.clear();
        mContext.unregisterReceiver(myNetReceiver);
    }

    private void initView(){
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        etIP = (EditText) activity.findViewById(R.id.eth_static_et_ip);
        etMask = (EditText) activity.findViewById(R.id.eth_static_et_mask);
        etGateway = (EditText) activity.findViewById(R.id.eth_static_et_gateway);
        etDns1 = (EditText) activity.findViewById(R.id.eth_static_et_dns1);
        etDns2 = (EditText) activity.findViewById(R.id.eth_static_et_dns2);
        btnConfirm = (Button) activity.findViewById(R.id.static_btn_confirm);
        btnCancel = (Button) activity.findViewById(R.id.static_btn_cancel);


        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.requestFocus();
        showDhcpInfo();

    }


    @Override
    public void onClick(View view){
        Log.i(TAG, "onClick: ");
        switch (view.getId()){
            case R.id.static_btn_confirm:
                //进行静态ip连接
                Log.i(TAG, "onClick: 尝试使用静态IP连接网络");
                setStatic();
                break;
            case R.id.static_btn_cancel:
                Log.i(TAG, "onClick: 隐藏静态ip碎片");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_TYPE_FRAGMENT);
                break;
            default:
                Log.i(TAG, "onClick: 点击了其它控件");
                break;
        }
    }
    
    public void setStatic(){


        Log.i(TAG, "setStatic: ");
        if (EditUtil.checkEditEmpty(etIP)){
            Log.i(TAG, "setStatic: IP为空");
            mHandler.sendEmptyMessage(MyHandler.STATIC_IP_NULL);
            showDhcpInfo();
            return;
        }else if (EditUtil.checkEditEmpty(etMask)){
            Log.i(TAG, "setStatic: 子网掩码为空");
            mHandler.sendEmptyMessage(MyHandler.STATIC_NETMASK_NULL);
            showDhcpInfo();
            return;
        }else if (EditUtil.checkEditEmpty(etGateway)){
            Log.i(TAG, "setStatic: 默认网关为空");
            mHandler.sendEmptyMessage(MyHandler.STATIC_GATEWAY_NULL);
            showDhcpInfo();
            return;
        }else if (EditUtil.checkEditEmpty(etDns1)){
            Log.i(TAG, "setStatic: 主用dns为空");
            mHandler.sendEmptyMessage(MyHandler.STATIC_DNS1_NULL);
            showDhcpInfo();
            return;
        }

        String userIp = etIP.getText().toString();
        String userMask = etMask.getText().toString();
        String userGateway = etGateway.getText().toString();
        String userDns1 = etDns1.getText().toString();
        String userDns2 = etDns2.getText().toString();

        if (!NetworkUtil.checkDhcpItem(userIp)){
            Log.i(TAG, "setStatic: IP不合法");
            mHandler.sendEmptyMessage(MyHandler.STATIC_IP_ILLEGAL);
            showDhcpInfo();
            return;
        }else if (!userMask.equals(workMask)){
            Log.i(TAG, "setStatic: 修改了子网掩码，暂定不合法");
            mHandler.sendEmptyMessage(MyHandler.STATIC_NETMASK_ILLEGAL);
            showDhcpInfo();
            return;
        }else if (!NetworkUtil.checkDhcpItem(userGateway)){
            Log.i(TAG, "setStatic: 默认网关不合法");
            mHandler.sendEmptyMessage(MyHandler.STATIC_GATEWAY_ILLEGAL);
            showDhcpInfo();
            return;
        }else if (!NetworkUtil.checkDhcpItem(userDns1)){
            Log.i(TAG, "setStatic: 主用dns不合法");
            mHandler.sendEmptyMessage(MyHandler.STATIC_DNS1_ILLEGAL);
            showDhcpInfo();
            return;
        }else if (!NetworkUtil.checkDhcpItem(userDns2)){
            Log.i(TAG, "setStatic: 备用dns不合法");
            mHandler.sendEmptyMessage(MyHandler.STATIC_DNS2_ILLEGAL);
            showDhcpInfo();
            return;
        }else if (!NetworkUtil.checkInOneSegment(userIp, userGateway)){
            Log.i(TAG, "setStatic: IP和网关不在同一个网段");
            mHandler.sendEmptyMessage(MyHandler.IP_GATEWAY_ERR);
            showDhcpInfo();
            return;
        }

        mHandler.sendEmptyMessage(MyHandler.CONNECTING);
        DhcpInfo userDhcp = new DhcpInfo();
        InetAddress iUserIp = NetworkUtils.numericToInetAddress(userIp);
        userDhcp.ipAddress = NetworkUtils.inetAddressToInt((Inet4Address) iUserIp);

        InetAddress iUserMask = NetworkUtils.numericToInetAddress(userMask);
        userDhcp.netmask = NetworkUtils.inetAddressToInt((Inet4Address) iUserMask);

        InetAddress iUserGateway = NetworkUtils.numericToInetAddress(userGateway);
        userDhcp.gateway = NetworkUtils.inetAddressToInt((Inet4Address) iUserGateway);

        InetAddress iUserDns1 = NetworkUtils.numericToInetAddress(userDns1);
        userDhcp.dns1 = NetworkUtils.inetAddressToInt((Inet4Address) iUserDns1);

        InetAddress iUserDns2 = NetworkUtils.numericToInetAddress(userDns2);
        userDhcp.dns2 = NetworkUtils.inetAddressToInt((Inet4Address) iUserDns2);

        mEthManager.setEthernetEnabled(false);
        Settings.Secure.putInt(resolver, "default_eth_mod"
                , NetworkUtil.ETHERNET_MODE_STATIC);
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL, userDhcp);
        Log.i(TAG, "setStatic: 手动配置的dhcp信息" + userDhcp.toString());
        mEthManager.setEthernetEnabled(true);

    }

    public void showDhcpInfo(){
        Log.i(TAG, "showDhcpInfo: ");
        NetworkUtil.MyDhcpInfo myDhcpInfo = new NetworkUtil.MyDhcpInfo(mContext, isNetConnected);
        String workIp = myDhcpInfo.getIpAddress();
        workMask = myDhcpInfo.getNetMask();
        String workGateway = myDhcpInfo.getGateway();
        String workDns1 = myDhcpInfo.getDns1();
        String workDns2 = myDhcpInfo.getDns2();

        etIP.setText(workIp);
        etMask.setText(workMask);
        etGateway.setText(workGateway);
        etDns1.setText(workDns1);
        etDns2.setText(workDns2);
    }


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String ethMode = mEthManager.getEthernetMode();
            Log.i(TAG, "onReceive: 接收广播:" + action + "&& 当前网络连接模式:" + ethMode);
            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){
                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                
                if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)) {
                    switch (ethEvent) {
                        case EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED:
                            Log.i(TAG, "onReceive: 静态ip连接成功");
                            isNetConnected = true;
                            mHandler.sendEmptyMessage(MyHandler.STATIC_CONNECT_SUCCESS);
                            break;
                        case EthernetManager.EVENT_STATIC_CONNECT_FAILED:
                            Log.i(TAG, "onReceive: 静态ip连接失败");
                            isNetConnected = false;
                            mHandler.sendEmptyMessage(MyHandler.STATIC_CONNECT_FAILED);
                            break;
                        default:
                            Log.i(TAG, "onReceive: 未处理的静态ip连接事件代码：" + ethEvent);
                            isNetConnected = false;
                            break;
                    }
                }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
                    switch (ethEvent){
                        case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                            Log.i(TAG, "onReceive: dhcp连接成功");
//                            mHandler.sendEmptyMessage(MyHandler.NET_AVAILABLE);
                            isNetConnected = true;
                            break;
                        default:
                            Log.i(TAG, "onReceive: 未处理的dhcp连接事件代码：" + ethEvent);
                            isNetConnected = false;
                            break;
                    }
                }
            }else if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION) &&
                    ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
                if (pppoeEvent == PppoeManager.EVENT_CONNECT_SUCCESSED){
                    Log.i(TAG, "onReceive: pppoe连接成功");
//                    mHandler.sendEmptyMessage(MyHandler.NET_AVAILABLE);
                    isNetConnected = true;
                }else {
                    Log.i(TAG, "onReceive: 未处理pppoe连接事件代码：" + pppoeEvent);
                    isNetConnected = false;
                }
            }else {
                Log.i(TAG, "onReceive: 未知广播：" + action);
            }

            showDhcpInfo();
        }
    };

}
