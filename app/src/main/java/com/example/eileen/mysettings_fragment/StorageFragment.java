package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

public class StorageFragment extends Fragment {

    private static final String TAG = "qll_storage_fragment";
    private Context mContext;

    public StorageFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
    }
}
