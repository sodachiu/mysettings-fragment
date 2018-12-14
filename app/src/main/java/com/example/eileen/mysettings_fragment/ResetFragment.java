package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResetFragment extends Fragment {
    private static final String TAG = "qll_recovery_fragment";
    private Context mContext;

    public ResetFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: 这里是恢复出厂设置的fragment啊");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_reset, parent, false);
        return view;
    }
}
