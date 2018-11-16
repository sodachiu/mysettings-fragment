package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EthPppoeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_eth_pppoe_fragment";
    private Context mContext;
    private FragmentActivity mActivity;
    private EditText etUsername, etPassword;
    private Button btnConfirm, btnCancel;
    private ListView lvMenu;

    public EthPppoeFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
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
        lvMenu = (ListView) mActivity.findViewById(R.id.main_lv_menu);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        if (hidden){
            lvMenu.setFocusable(true);
        }else {
            lvMenu.setFocusable(false);
        }
    }
    
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pppoe_btn_confirm:
                Log.i(TAG, "onClick: 确定，进行pppoe连接");
                //进行pppoe连接
                break;
            case R.id.pppoe_btn_cancel:
                //退出该fragment
                Log.i(TAG, "onClick: 取消，退出pppoe界面");
                break;
            default:
                Log.i(TAG, "onClick: 点击了不知道什么东西");
                break;
        }
    }
}
