package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NetInfoFragment extends Fragment {
    private Context mContext;
    private FragmentActivity mActivity;

    public NetInfoFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_net_info, container, false);
        initView();
        return view;
    }

    private void initView(){

    }
}
