package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;

import java.util.List;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "qll_mainactivity";

    private ListView lvMenu;
    private int mSelectPos = 0;
    public static final int ABOUT = 0;
    public static final int ETH_SETTING = 1;
    public static final int NET_INFO = 2;
    public static final int DATE_TIME = 3;
    public static final int DISPLAY = 4;
    public static final int STORAGE = 5;
    public static final int ADVANCED = 6;
    public static final int RESET = 7;

    private Context mContext;

    private LinearLayout llFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void initView(){

        String[] menuArray = getResources().getStringArray(R.array.menu_item);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.menu_item, menuArray);
        lvMenu = (ListView) findViewById(R.id.main_lv_menu);
        llFragment = (LinearLayout) findViewById(R.id.main_ll_fragment_container);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: 位置为：" + position);
                mSelectPos = position;
                switch (position){
                    case ABOUT:
                        Log.i(TAG, "onItemSelected: 关于本机");
                        FragmentUtil.showFragment(mContext, FragmentUtil.ABOUT_FRAGMENT);
                        break;
                    case ETH_SETTING:
                        Log.i(TAG, "onItemSelected: 网络配置");
                        FragmentUtil.showFragment(mContext, FragmentUtil.ETH_FRAGMENT);
                        break;
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        String fragmentTag = FragmentUtil.getCurrentFragmentTag(mContext);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                handleBackEvent(fragmentTag);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                handleLeftEvent(fragmentTag);
            }
        return false;
    }

    private void handleBackEvent(String tag){
        Log.i(TAG, "handleBackEvent: 当前活动的fragment为---->" + tag);
        switch (tag){
            case FragmentUtil.ETH_TYPE_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_FRAGMENT);
                break;
            case FragmentUtil.ETH_PPPOE_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_TYPE_FRAGMENT);
                break;
            default:
                finish();
                break;
        }
    }

    private void handleLeftEvent(String tag){
        /*后续还要加条件*/
        if (tag.equals(FragmentUtil.ABOUT_FRAGMENT) || tag.equals(FragmentUtil.ETH_FRAGMENT)){
            lvMenu.setFocusable(true);
        }
    }
}
