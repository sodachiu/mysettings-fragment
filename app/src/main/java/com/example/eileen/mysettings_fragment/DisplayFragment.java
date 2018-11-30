package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.net.ConnectException;

public class DisplayFragment extends Fragment {
    private static final String TAG = "qll_display_fragment";
    private Context mContext;

    public DisplayFragment(){
        Log.i(TAG, "DisplayFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
    }
}
