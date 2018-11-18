package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.eileen.mysettings_fragment.network.NetworkUtil;
import com.example.eileen.mysettings_fragment.utils.FragmentUtil;

public class EthStaticFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_eth_static_fragment";
    private Context mContext;
    private FragmentActivity mActivity;
    private boolean isNetConnected = false;
    private EthernetManager mEthManager;

    private EditText etIP, etMask, etGateway, etDns1, etDns2;
    private Button btnConfirm, btnCancel;
    private ListView lvMenu;

    public static final int STATIC_CONNECT_SUCCESSED = 0;
    public static final int STATIC_CONNECT_FAILED = 1;
    public static final int STATIC_CONNECTING = 2;
    public static final int STATIC_IP_NULL = 3;
    public static final int STATIC_NETMASK_NULL = 4;
    public static final int STATIC_GATEWAY_NULL = 5;
    public static final int STATIC_DNS1_NULL = 6;
    public static final int STATIC_DNS2_NULL = 7;
    public static final int STATIC_IP_ILLEGAL = 8;
    public static final int STATIC_NETMASK_ILLEGAL = 9;
    public static final int STATIC_GATEWAY_ILLEGAL = 10;
    public static final int STATIC_DNS1_ILLEGAL = 11;
    public static final int STATIC_DNS2_ILLEGAL = 12;
    public static final int IP_GATEWAY_ERR = 13;//ip & gateway不在同一个网段
    public EthStaticFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
        isNetConnected = NetworkUtil.checkNetConnect(mContext);
        mEthManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_eth_static, container, false);
        initView();
        return view;
    }

    private void initView(){
        //拿到所有空间，包括menu
        etIP = (EditText) mActivity.findViewById(R.id.eth_static_et_ip);
        etMask = (EditText) mActivity.findViewById(R.id.eth_static_et_mask);
        etGateway = (EditText) mActivity.findViewById(R.id.eth_static_et_gateway);
        etDns1 = (EditText) mActivity.findViewById(R.id.eth_static_et_dns1);
        etDns2 = (EditText) mActivity.findViewById(R.id.eth_static_et_dns2);
        btnConfirm = (Button) mActivity.findViewById(R.id.static_btn_confirm);
        btnCancel = (Button) mActivity.findViewById(R.id.static_btn_cancel);
        lvMenu = (ListView) mActivity.findViewById(R.id.main_lv_menu);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.requestFocus();
        showDhcpInfo();

    }

    @Override
    public void onHiddenChanged(boolean hidden){
        if (!hidden){
            lvMenu.setFocusable(false);
            btnConfirm.requestFocus();
            showDhcpInfo();
        }else {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View view){
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
        
    }

    public void showDhcpInfo(){
        NetworkUtil.MyDhcpInfo myDhcpInfo = new NetworkUtil.MyDhcpInfo(mContext, isNetConnected);
        String workIp = myDhcpInfo.getIpAddress();
        String workMask = myDhcpInfo.getNetMask();
        String workGateway = myDhcpInfo.getGateway();
        String workDns1 = myDhcpInfo.getDns1();
        String workDns2 = myDhcpInfo.getDns2();

        etIP.setText(workIp);
        etMask.setText(workMask);
        etGateway.setText(workGateway);
        etDns1.setText(workDns1);
        etDns2.setText(workDns2);
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){

        }
    };

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String ethMode = mEthManager.getEthernetMode();
            Log.i(TAG, "onReceive: 接收广播:" + action + "&& 当前网络连接模式:" + ethMode);
            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)
                    && ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){

                int staticEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                switch (staticEvent){
                    case EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED:
                        Log.i(TAG, "onReceive: 静态ip连接成功");
                        isNetConnected = true;
                        handler.sendEmptyMessage(STATIC_CONNECT_SUCCESSED);
                        break;
                    case EthernetManager.EVENT_STATIC_CONNECT_FAILED:
                        Log.i(TAG, "onReceive: 静态ip连接失败");
                        isNetConnected = false;
                        handler.sendEmptyMessage(STATIC_CONNECT_FAILED);
                        break;
                }
            }
        }
    };
}
