package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings_fragment.network.NetworkUtil;
import com.example.eileen.mysettings_fragment.utils.FragmentUtil;


public class EthTypeFragment extends Fragment
        implements View.OnClickListener{
    private static final String TAG = "qll_eth_type_fragment";
    private Context mContext;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private TextView tvPppoe, tvDhcp, tvStatic;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private ListView lvMenu;
    private EthernetManager mEthManager;
    private IntentFilter filter;
    private boolean isNetConnect = false;

    public static final int DHCP_CONNECTING = 0;
    public static final int DHCP_CONNECT_SUCCESS = 1;
    public static final int DHCP_CONNECT_FAILED = 2;
    public static final int NO_LINK_UP = 3;//网线脱落
    public EthTypeFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mEthManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        filter = new IntentFilter();
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
//        filter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        mContext.registerReceiver(myNetReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_eth_type, container, false);
        initView(view);
        return view;
    }

    public void initView(View view){
        llPppoe = (LinearLayout) view.findViewById(R.id.eth_ll_pppoe);
        llDhcp = (LinearLayout) view.findViewById(R.id.eth_ll_dhcp);
        llStatic = (LinearLayout) view.findViewById(R.id.eth_ll_static);
        imgPppoe = (ImageView) view.findViewById(R.id.eth_img_pppoe);
        imgDhcp = (ImageView) view.findViewById(R.id.eth_img_dhcp);
        imgStatic = (ImageView) view.findViewById(R.id.eth_img_static);
        tvPppoe = (TextView) view.findViewById(R.id.eth_tv_pppoe_connect);
        tvDhcp = (TextView) view.findViewById(R.id.eth_tv_dhcp_connect);
        tvStatic = (TextView) view.findViewById(R.id.eth_tv_static_connect);

        FragmentActivity activity = (FragmentActivity) mContext;
        lvMenu = (ListView) activity.findViewById(R.id.main_lv_menu);
        lvMenu.setFocusable(false);

        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

        isNetConnect = checkNetConnect();
        showConnectState();


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
    public void onHiddenChanged(boolean hidden){
        Log.i(TAG, "onHiddenChanged: ");
        if (!hidden){
            lvMenu.setFocusable(false);
            //xx.requesFocus
            isNetConnect = NetworkUtil.checkNetConnect(mContext);
            showConnectState();
            mContext.registerReceiver(myNetReceiver, filter);
        }else {
            //handler.remove
            mContext.unregisterReceiver(myNetReceiver);
        }
    }

    /*
     * 进行dhcp连接
     * */
    public void setDhcp(){
        Log.i(TAG, "setDhcp: ");
        handler.sendEmptyMessage(DHCP_CONNECTING);
        mEthManager.setEthernetEnabled(false);
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_DHCP, null);
        mEthManager.setEthernetEnabled(true);
    }

    /*
    * 判断网络是否连接
    * */


    /*
    *  更新显示的网络状态
    * */
    private void showConnectState(){
        Log.i(TAG, "showConnectState: ");

        if (isNetConnect) {
            String ethMode = mEthManager.getEthernetMode();
            switch (ethMode){
                case EthernetManager.ETHERNET_CONNECT_MODE_DHCP:
                    tvDhcp.setVisibility(View.VISIBLE);
                    tvPppoe.setVisibility(View.INVISIBLE);
                    tvStatic.setVisibility(View.INVISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_checked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                    imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                    llDhcp.requestFocus();
                    break;
                case EthernetManager.ETHERNET_CONNECT_MODE_PPPOE:
                    tvDhcp.setVisibility(View.INVISIBLE);
                    tvPppoe.setVisibility(View.VISIBLE);
                    tvStatic.setVisibility(View.INVISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_checked_normal);
                    imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                    llPppoe.requestFocus();
                    break;
                case EthernetManager.ETHERNET_CONNECT_MODE_MANUAL:
                    tvDhcp.setVisibility(View.INVISIBLE);
                    tvPppoe.setVisibility(View.INVISIBLE);
                    tvStatic.setVisibility(View.VISIBLE);
                    imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                    imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                    imgStatic.setImageResource(R.drawable.radio_checked_normal);
                    llStatic.requestFocus();
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

    private Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          switch (msg.what){
              case DHCP_CONNECT_SUCCESS:
                  Toast.makeText(mContext,
                          "连接成功",
                          Toast.LENGTH_SHORT).show();
                  break;
              case DHCP_CONNECT_FAILED:
                  Toast.makeText(mContext,
                          "连接失败",
                          Toast.LENGTH_SHORT).show();
                  break;
              case DHCP_CONNECTING:
                  Toast.makeText(mContext,
                          "正在连接请稍后...",
                          Toast.LENGTH_SHORT).show();
                  break;
              default:
                  Log.i(TAG, "handleMessage: 没有处理的消息代码---->" + msg.what);
                  break;

          }
      }
    };

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接收广播---->" + action);
            String ethMode = mEthManager.getEthernetMode();
            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)
                    && ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){

                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                switch (ethEvent){
                    case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                        isNetConnect = true;
                        Log.i(TAG, "onReceive: dhcp连接成功");
                        handler.sendEmptyMessage(DHCP_CONNECT_SUCCESS);
                        break;
                    case EthernetManager.EVENT_DHCP_CONNECT_FAILED:
                        isNetConnect = false;
                        Log.i(TAG, "onReceive: dhcp连接失败");
                        handler.sendEmptyMessage(DHCP_CONNECT_FAILED);
                        break;
                    default:
                        Log.i(TAG, "onReceive: 未处理的dhcp事件代码---->" + ethEvent);
                        break;
                }
                showConnectState();
            }
        }
    };

}
