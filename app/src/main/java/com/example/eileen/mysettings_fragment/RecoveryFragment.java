package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

public class RecoveryFragment extends Fragment {
    private static final String TAG = "qll_recovery_fragment";
    private Context mContext;

    public RecoveryFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: 这里是恢复出厂设置的fragment啊");
        super.onAttach(context);
        mContext = context;
    }
}
