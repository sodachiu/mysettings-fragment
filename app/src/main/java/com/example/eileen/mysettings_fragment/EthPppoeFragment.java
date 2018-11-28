package com.example.eileen.mysettings_fragment;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.eileen.mysettings_fragment.utils.EditUtil;
import com.example.eileen.mysettings_fragment.utils.FragmentUtil;
import com.example.eileen.mysettings_fragment.utils.MyHandler;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;

public class EthPppoeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_eth_pppoe_fragment";
    private Context mContext;
    private EditText etUsername, etPassword;
    private Button btnConfirm, btnCancel;
    private ListView lvMenu;
    private EthernetManager mEthManager;
    private PppoeManager mPppoeManager;
    private String errMsg = "";
    private IntentFilter filter;
    private MyHandler mHandler;
    private ContentResolver resolver;


    public EthPppoeFragment(){
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        resolver = mContext.getContentResolver();
        mHandler = new MyHandler(mContext);
        mEthManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        mPppoeManager = (PppoeManager) mContext.getSystemService(Context.PPPOE_SERVICE);
        filter = new IntentFilter(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        mContext.registerReceiver(myPppoeReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_eth_pppoe, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        etUsername = (EditText) view.findViewById(R.id.pppoe_et_username);
        etPassword = (EditText) view.findViewById(R.id.pppoe_et_password);
        btnConfirm = (Button) view.findViewById(R.id.pppoe_btn_confirm);
        btnCancel = (Button) view.findViewById(R.id.pppoe_btn_cancel);
        lvMenu = (ListView) getActivity().findViewById(R.id.main_lv_menu);


        String username = mPppoeManager.getPppoeUsername();
        String userpwd = mPppoeManager.getPppoePassword();

        if (username == null){
            username = "";
        }
        if (userpwd == null){
            username = "";
        }
        etUsername.setText(username);
        etPassword.setText(userpwd);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.requestFocus();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        resolver = null;
        mHandler.clear();
        mContext.unregisterReceiver(myPppoeReceiver);
    }

    
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pppoe_btn_confirm:
                Log.i(TAG, "onClick: 确定，进行pppoe连接");
                setPppoe();
                break;
            case R.id.pppoe_btn_cancel:
                //退出该fragment
                Log.i(TAG, "onClick: 取消，退出pppoe界面");
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_TYPE_FRAGMENT);
                break;
            default:
                Log.i(TAG, "onClick: 点击了其它控件");
                break;
        }
    }

    private void setPppoe(){
        boolean isUsernameEmpty = EditUtil.checkEditEmpty(etUsername);
        if (isUsernameEmpty){
            Log.i(TAG, "setPppoe: 用户名为空");
            mHandler.sendEmptyMessage(MyHandler.PPPOE_USERNAME_EMPTY);
            return;
        }

        boolean isPasswordEmpty = EditUtil.checkEditEmpty(etPassword);
        if (isPasswordEmpty){
            Log.i(TAG, "setPppoe: 密码为空");
            mHandler.sendEmptyMessage(MyHandler.PPPOE_PASSWORD_EMPTY);
            return;
        }

        String username = etUsername.getText().toString().trim();
        String userpwd = etPassword.getText().toString().trim();
        mEthManager.setEthernetEnabled(false);
        mPppoeManager.setPppoeUsername(username);
        mPppoeManager.setPppoePassword(userpwd);
        Settings.Secure.putInt(resolver, "default_eth_mod", NetworkUtil.ETHERNET_MODE_PPPOE);
        String ifName = mEthManager.getInterfaceName();
        if (ifName != null){
            mPppoeManager.connect(username, userpwd, ifName);
            Log.i(TAG, "setPppoe: 用于拨号连接的用户名：" + username +
                    "&&密码：" + userpwd + "&&网口：" + ifName);
        }else {
            Log.i(TAG, "setPppoe: 网口名为空");
        }
        mEthManager.setEthernetEnabled(true);

    }


    private BroadcastReceiver myPppoeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 广播为---->" + action);
            int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
            int ethMode = Settings.Secure.getInt(resolver, "default_eth_mod", -1);
            if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)
                    && ethMode == NetworkUtil.ETHERNET_MODE_PPPOE){
                switch (pppoeEvent){
                    case PppoeManager.EVENT_CONNECT_SUCCESSED:
                        Log.i(TAG, "onReceive: pppoe连接成功");
                        mHandler.sendEmptyMessage(MyHandler.PPPOE_CONNECT_SUCCESS);
                        break;
                    case PppoeManager.EVENT_CONNECT_FAILED:
                        Log.i(TAG, "onReceive: pppoe连接失败");
                        errMsg = intent.getStringExtra(PppoeManager.EXTRA_PPPOE_ERRMSG);
                        if (errMsg == null){
                            mHandler.sendEmptyMessage(MyHandler.PPPOE_CONNECT_FAILED_999);
                        }else {
                            mHandler.sendEmptyMessage(MyHandler.PPPOE_CONNECT_FAILED_OTHER);
                        }
                        break;
                    case PppoeManager.EVENT_CONNECTING:
                        Log.i(TAG, "onReceive: pppoe正在连接");
                        mHandler.sendEmptyMessage(MyHandler.CONNECTING);
                        break;
                    default:
                        Log.i(TAG, "onReceive: 当前pppoe未处理事件代码---->" + pppoeEvent);
                        break;
                }
            }
        }
    };
}
