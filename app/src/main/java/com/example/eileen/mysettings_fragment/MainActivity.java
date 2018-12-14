package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.utils.FragmentUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static android.os.Environment.DIRECTORY_ALARMS;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "qll_main_activity";

    private ListView lvMenu;
    private int mSelectPos = 0;
    public static final int ABOUT = 0;
    public static final int ETH_SETTING = 1;
    public static final int NET_INFO = 2;
    public static final int DATE_TIME = 3;
    public static final int DISPLAY = 4;
    public static final int STORAGE = 5;
    public static final int ADVANCED = 6;
    public static final int Recovery = 7;

    private Context mContext;

    private LinearLayout llFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

        /* new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    View rootview = MainActivity.this.getWindow().getDecorView();
                    View aaa = rootview.findFocus();
                    if (aaa == null){
                        Log.i(TAG, "run: 没有找到焦点");
                    }else {
                        Log.i(TAG, "当前焦点位置为：" + aaa.toString());
                    }
                }

            }

        }).start();*/

    }

    private void initView(){
        Log.i(TAG, "initView: ");
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
                    case NET_INFO:
                        Log.i(TAG, "onItemSelected: 网络信息");
                        FragmentUtil.showFragment(mContext, FragmentUtil.NET_INFO_FRAGMENT);
                        break;
                    case DATE_TIME:
                        Log.i(TAG, "onItemSelected: 日期和时间");
                        FragmentUtil.showFragment(mContext, FragmentUtil.DATE_TIME_FRAGMENT);
                        break;
                    case DISPLAY:
                        Log.i(TAG, "onItemSelected: 显示");
                        FragmentUtil.showFragment(mContext, FragmentUtil.DISPLAY_FRAGMENT);
                        break;
                    case STORAGE:
                        Log.i(TAG, "onItemSelected: 存储信息");
                        FragmentUtil.showFragment(mContext, FragmentUtil.STORAGE_FRAGMENT);
                        break;
                    case ADVANCED:
                        Log.i(TAG, "onItemSelected: 高级设置");
                        FragmentUtil.showFragment(mContext, FragmentUtil.ADVANCED_FRAGMENT);
                        break;
                    case Recovery:
                        Log.i(TAG, "onItemSelected: 恢复出厂设置");
                        FragmentUtil.showFragment(mContext, FragmentUtil.RESET_FRAGMENT);
                        break;
                    default:
                        //增加菜单的话，在这上面再添加条件，然后在arrays文件的menuitem中添加菜单项
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
        String nowFragmentTag = FragmentUtil.getCurrentFragmentTag(mContext);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.i(TAG, "onKeyDown: 按下返回键");
                handleBackEvent(nowFragmentTag);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.i(TAG, "onKeyDown: 按下左键");
                handleLeftEvent(nowFragmentTag);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.i(TAG, "onKeyDown: 按下右键");
                handleRightEvent(nowFragmentTag);
            default:
                break;
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
            case FragmentUtil.ETH_STATIC_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_TYPE_FRAGMENT);
                break;
            case FragmentUtil.ETH_BLUETOOTH_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.ETH_FRAGMENT);
                break;
            case FragmentUtil.DATE_FORMAT_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.DATE_TIME_FRAGMENT);
                break;
            case FragmentUtil.DISPLAY_RESOLUTION_FRAGMENT:
                FragmentUtil.showFragment(mContext, FragmentUtil.DISPLAY_FRAGMENT);
                break;
            case FragmentUtil.ADVANCED_FRAGMENT_2:
                FragmentUtil.showFragment(mContext, FragmentUtil.ADVANCED_FRAGMENT);
                break;
            case FragmentUtil.ADVANCED_CLEAT_ALL:
                FragmentUtil.showFragment(mContext, FragmentUtil.ADVANCED_FRAGMENT_2);
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 处理按“左键”时，菜单定位问题
     * */
    void handleLeftEvent(String tag){

        if (tag.equals(FragmentUtil.ABOUT_FRAGMENT)
                || tag.equals(FragmentUtil.ETH_FRAGMENT)
                || tag.equals(FragmentUtil.NET_INFO_FRAGMENT)
                || tag.equals(FragmentUtil.DATE_TIME_FRAGMENT)
                || tag.equals(FragmentUtil.DISPLAY_FRAGMENT)
                || tag.equals(FragmentUtil.STORAGE_FRAGMENT)
                || tag.equals(FragmentUtil.ADVANCED_FRAGMENT)
                || tag.equals(FragmentUtil.RESET_FRAGMENT)){

            lvMenu.setFocusable(true);
        }

        //lvMenu.setFocusable(true);

        Log.i(TAG, "handleLeftEvent: 当前菜单位置应为：" + mSelectPos);
        lvMenu.post(new Runnable() {
            @Override
            public void run() {
                lvMenu.setSelection(mSelectPos);
            }
        });

    }

    private void handleRightEvent(String tag){

        lvMenu.setFocusable(false);
        /*switch (tag){
            *//*case FragmentUtil.ETH_FRAGMENT:
                LinearLayout llSetNet = (LinearLayout) findViewById(R.id.eth_ll_set_net);
                LinearLayout llSetBluetooth = (LinearLayout) findViewById(R.id.eth_ll_set_bluetooth);
                llSetNet.setFocusable(true);
                llSetBluetooth.setFocusable(true);
                break;
                //继续添加其它的第一层fragment处理事件*//*
            case FragmentUtil.ADVANCED_FRAGMENT:
                EditText etPassword = (EditText) findViewById(R.id.advanced_et_pwd);
                etPassword.requestFocus();
                break;
        }*/

    }

}
