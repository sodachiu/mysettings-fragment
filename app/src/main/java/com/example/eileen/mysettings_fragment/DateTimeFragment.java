package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.ConnectException;

public class DateTimeFragment extends Fragment {
    private Context mContext;
    private FragmentActivity mActivity;

    public DateTimeFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_date_time, container, false);
        initView();
        return view;
    }

    private void initView(){

    }
}
