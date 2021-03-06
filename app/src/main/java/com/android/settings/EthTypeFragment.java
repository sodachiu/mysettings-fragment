package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settings.network.NetworkUtil;
import com.android.settings.utils.FragmentUtil;
import com.android.settings.utils.MyHandler;



public class EthTypeFragment extends Fragment
        implements View.OnClickListener{
    private static final String TAG = "qll_eth_type_fragment";
    private Context mContext;
    private MyHandler mHandler;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private TextView tvPppoe, tvDhcp, tvStatic;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private ListView lvMenu;
    private EthernetManager mEthManager;
    private IntentFilter filter;
    private boolean isNetConnect = false;
    private ContentResolver resolver;


    public EthTypeFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mEthManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        mHandler = new MyHandler(mContext);
        resolver = mContext.getContentResolver();
        filter = new IntentFilter();
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        mContext.registerReceiver(myNetReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_eth_type, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void initView(){
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        llPppoe = (LinearLayout) activity.findViewById(R.id.eth_ll_pppoe);
        llDhcp = (LinearLayout) activity.findViewById(R.id.eth_ll_dhcp);
        llStatic = (LinearLayout) activity.findViewById(R.id.eth_ll_static);
        imgPppoe = (ImageView) activity.findViewById(R.id.eth_img_pppoe);
        imgDhcp = (ImageView) activity.findViewById(R.id.eth_img_dhcp);
        imgStatic = (ImageView) activity.findViewById(R.id.eth_img_static);
        tvPppoe = (TextView) activity.findViewById(R.id.eth_tv_pppoe_connect);
        tvDhcp = (TextView) activity.findViewById(R.id.eth_tv_dhcp_connect);
        tvStatic = (TextView) activity.findViewById(R.id.eth_tv_static_connect);

        lvMenu = (ListView) activity.findViewById(R.id.main_lv_menu);
        lvMenu.setFocusable(false);

        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

        isNetConnect = NetworkUtil.checkNetConnect(mContext);
        showConnectState();
        String preFragment = FragmentUtil.getPreviousFragment();
        if (preFragment.equals(FragmentUtil.ETH_PPPOE_FRAGMENT)){
            llPppoe.requestFocus();
        }else if (preFragment.equals(FragmentUtil.ETH_STATIC_FRAGMENT)){
            llStatic.requestFocus();
        }else {
            if (isNetConnect){
                int ethMode = Settings.Secure.getInt(resolver,
                        "default_eth_mod", -1);
                switch (ethMode){
                    case NetworkUtil.ETHERNET_MODE_DHCP:
                        llDhcp.requestFocus();
                        break;
                    case NetworkUtil.ETHERNET_MODE_STATIC:
                        llStatic.requestFocus();
                        break;
                    default:
                        llPppoe.requestFocus();
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.eth_ll_pppoe:
                Log.i(TAG, "onClick: 点击pppoe，尝试进入pppoe设置界面");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_PPPOE_FRAGMENT);
                break;
            case R.id.eth_ll_dhcp:

                Log.i(TAG, "onClick: 点击dhcp，尝试连接");
                setDhcp();
                break;
            case R.id.eth_ll_static:
                Log.i(TAG, "onClick: 点击静态IP，尝试进入静态IP设置界面");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_STATIC_FRAGMENT);
                break;
            default:
                Log.i(TAG, "onClick: 点击了位置的东西");
                break;
        }
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mHandler.clear();
        mContext.unregisterReceiver(myNetReceiver);
        resolver = null;
    }

    /*
     * 进行dhcp连接
     * */
    public void setDhcp(){
        Log.i(TAG, "setDhcp: ");
        if (!NetworkUtil.checkIsLinkUp(mContext)){
            Log.i(TAG, "setDhcp: 网线脱落");
            mHandler.sendEmptyMessage(MyHandler.LINK_DOWN);
            return;
        }
        mHandler.sendEmptyMessage(MyHandler.CONNECTING);
        mEthManager.setEthernetEnabled(false);
        Settings.Secure.putInt(mContext.getContentResolver()
                , "default_eth_mod"
                , NetworkUtil.ETHERNET_MODE_DHCP);
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_DHCP, null);
        mEthManager.setEthernetEnabled(true);
    }


    /*
    *  更新显示的网络状态
    * */
    private void showConnectState(){
        Log.i(TAG, "showConnectState: ");

        if (isNetConnect) {
            int ethMode = Settings.Secure.getInt(resolver, "default_eth_mod", -1);
            switch (ethMode){
                case NetworkUtil.ETHERNET_MODE_DHCP:
                    tvDhcp.setVisibility(View.VISIBLE);
                    tvPppoe.setVisibility(View.INVISIBLE);
                    tvStatic.setVisibility(View.INVISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_checked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                    imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                    break;
                case NetworkUtil.ETHERNET_MODE_PPPOE:
                    tvDhcp.setVisibility(View.INVISIBLE);
                    tvPppoe.setVisibility(View.VISIBLE);
                    tvStatic.setVisibility(View.INVISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_checked_normal);
                    imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                    break;
                case NetworkUtil.ETHERNET_MODE_STATIC:
                    tvDhcp.setVisibility(View.INVISIBLE);
                    tvPppoe.setVisibility(View.INVISIBLE);
                    tvStatic.setVisibility(View.VISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                    imgStatic.setImageResource(R.drawable.radio_checked_normal);
                    break;
                default:
                    tvDhcp.setVisibility(View.INVISIBLE);
                    tvPppoe.setVisibility(View.INVISIBLE);
                    tvStatic.setVisibility(View.INVISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                    imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                    break;
            }
        }else {
            tvDhcp.setVisibility(View.INVISIBLE);
            tvPppoe.setVisibility(View.INVISIBLE);
            tvStatic.setVisibility(View.INVISIBLE);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }

    }


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接收广播---->" + action);
            int ethMode = Settings.Secure.getInt(resolver, "default_eth_mod", 0);
            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){
                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);

                if (ethMode == NetworkUtil.ETHERNET_MODE_DHCP
                        && ethEvent != EthernetManager.EVENT_PHY_LINK_DOWN){
                    switch (ethEvent){
                        case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                            isNetConnect = true;
                            Log.i(TAG, "onReceive: dhcp连接成功");
                            mHandler.sendEmptyMessage(MyHandler.DHCP_CONNECT_SUCCESS);
                            break;
                        case EthernetManager.EVENT_DHCP_CONNECT_FAILED:
                            isNetConnect = false;
                            Log.i(TAG, "onReceive: dhcp连接失败");
                            mHandler.sendEmptyMessage(MyHandler.DHCP_CONNECT_FAILED);
                            break;
                        default:
                            Log.i(TAG, "onReceive: 未处理的dhcp事件代码---->" + ethEvent);
                            break;
                    }
                }else if (ethEvent == EthernetManager.EVENT_PHY_LINK_DOWN){
                    isNetConnect = false;
                    mHandler.sendEmptyMessage(MyHandler.LINK_DOWN);
                }

                showConnectState();
            }
        }
    };
}
