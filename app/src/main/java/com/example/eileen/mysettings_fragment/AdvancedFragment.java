package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AdvancedFragment extends Fragment {
    private static final String TAG = "qll_advanced_fragment";
    private Context mContext;

    private ListView lvMenu;

    public AdvancedFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: 这里是高级设置的碎片啊");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_advanced, container, false);
        initView();
        return view;
    }

    public void initView(){
        FragmentActivity activity = getActivity();
        lvMenu = (ListView) activity.findViewById(R.id.main_lv_menu);
        //开始寻找控件吧

    }

    public void onHiddenChanged(boolean hidden){
        if (!hidden){
            lvMenu.setFocusable(false);
        }
    }
}
