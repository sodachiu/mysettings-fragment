package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
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

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;


public class EthTypeFragment extends Fragment
        implements View.OnClickListener{
    private static final String TAG = "qll_eth_type_fragment";
    private Context mContext;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private TextView tvPppoe, tvDhcp, tvStatic;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private ListView lvMenu;
    public EthTypeFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
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
        lvMenu = activity.findViewById(R.id.main_lv_menu);
        lvMenu.setFocusable(false);

        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

        /*判断当前连接模式，相应的ll获取焦点*/
        llPppoe.requestFocus();

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
                break;
            case R.id.eth_ll_static:
                Log.i(TAG, "onClick: 点击静态IP，尝试进入静态IP设置界面");
                setDhcp();
                break;
            default:
                Log.i(TAG, "onClick: 点击了位置的东西");
                FragmentUtil.showFragment(mContext, FragmentUtil.)
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        if (hidden){
            lvMenu.setFocusable(true);
        }else {
            lvMenu.setFocusable(false);
        }
    }

    public void setDhcp(){

    }

}
