package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EthStaticFragment extends Fragment {
    private Context mContext;
    private FragmentActivity mActivity;

    private EditText etIP, etMask, etGateway, etDns1, etDns2;
    private Button btnConfirm, btnCancel;
    private ListView lvMenu;

    public EthStaticFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_eth_static, container, false);
        initView();
        return view;
    }

    private void initView(){
        //拿到所有空间，包括menu
    }
}
