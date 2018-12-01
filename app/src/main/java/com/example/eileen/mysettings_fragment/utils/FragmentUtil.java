package com.example.eileen.mysettings_fragment.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.example.eileen.mysettings_fragment.AboutFragment;
import com.example.eileen.mysettings_fragment.DateFormatFragment;
import com.example.eileen.mysettings_fragment.DateTimeFragment;
import com.example.eileen.mysettings_fragment.DisplayFragment;
import com.example.eileen.mysettings_fragment.DisplayResolutionFragment;
import com.example.eileen.mysettings_fragment.EthBluetoothFragment;
import com.example.eileen.mysettings_fragment.EthFragment;
import com.example.eileen.mysettings_fragment.EthPppoeFragment;
import com.example.eileen.mysettings_fragment.EthStaticFragment;
import com.example.eileen.mysettings_fragment.EthTypeFragment;
import com.example.eileen.mysettings_fragment.MainActivity;
import com.example.eileen.mysettings_fragment.NetInfoFragment;
import com.example.eileen.mysettings_fragment.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {
    private static final String TAG = "qll_fragment_util";
    private FragmentActivity mActivity;

    public static final String ABOUT_FRAGMENT = "about_fragment";
    public static final String ETH_FRAGMENT = "eth_fragment";
    public static final String ETH_TYPE_FRAGMENT = "eth_type_fragment";
    public static final String ETH_PPPOE_FRAGMENT = "eth_pppoe_fragment";
    public static final String ETH_STATIC_FRAGMENT = "eth_static_fragment";
    public static final String NET_INFO_FRAGMENT = "net_info_fragment";
    public static final String DATE_TIME_FRAGMENT = "date_time_fragment";
    public static final String DATE_FORMAT_FRAGMENT = "date_format_fragment";
    public static final String DISPLAY_FRAGMENT = "display_fragment";
    public static final String STORAGE_FRAGMENT = "storage_fragment";
    public static final String ADVANCED_FRAGMENT = "advanced_fragment";
    public static final String RECOVERY_FRAGMENT = "recovery_fragment";
    public static final String ETH_BLUETOOTH_FRAGMENT = "eth_bluetooth_fragment";
    public static final String DISPLAY_RESOLUTION_FRAGMENT = "display_resolution_fragment";
    private static String previousFragmentTag = ABOUT_FRAGMENT;




    /*显示指定的fragment*/
    public static void showFragment(Context context, String tag){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment;
        Log.i(TAG, "showFragment: 指定显示的fragment---->" + tag);
        switch (tag){
            case ABOUT_FRAGMENT:
                fragment = new AboutFragment();
                break;
            case ETH_FRAGMENT:
                fragment = new EthFragment();
                break;
            case ETH_TYPE_FRAGMENT:
                fragment = new EthTypeFragment();
                break;
            case ETH_PPPOE_FRAGMENT:
                fragment = new EthPppoeFragment();
                break;
            case ETH_STATIC_FRAGMENT:
                fragment = new EthStaticFragment();
                break;
            case ETH_BLUETOOTH_FRAGMENT:
                fragment = new EthBluetoothFragment();
                break;
            case NET_INFO_FRAGMENT:
                fragment = new NetInfoFragment();
                break;
            case DATE_TIME_FRAGMENT:
                fragment = new DateTimeFragment();
                break;
            case DATE_FORMAT_FRAGMENT:
                fragment = new DateFormatFragment();
                break;
            case DISPLAY_FRAGMENT:
                fragment = new DisplayFragment();
                break;
            case DISPLAY_RESOLUTION_FRAGMENT:
                fragment = new DisplayResolutionFragment();
                break;
            default:
                Log.i(TAG, "startFragment: 传入tag有误，请检查");
                return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        Log.i(TAG, "showFragment: 当前栈中碎片数量---->" + fragments.size());

        try {
            Fragment previousFragment = fragments.get(0);
            if (previousFragment != null){
                previousFragmentTag = previousFragment.getTag();
            }
        }catch (Exception e){
            Log.e(TAG, "showFragment: 当前栈中没有碎片" );
        }finally {
            transaction.replace(R.id.main_ll_fragment_container, fragment, tag);
        }

        transaction.commit();

    }

    /*返回当前fragment的tag*/
    public static String getCurrentFragmentTag(Context context){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();

        Fragment nowFragment = fragments.get(0);
        if (nowFragment != null && nowFragment.isVisible()){
            return nowFragment.getTag();
        }

        return "";
    }


    /*返回当前fragment*/
    public static Fragment getCurrentFragment(Context context){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();

        Fragment nowFragment = fragments.get(0);
        if (nowFragment != null && nowFragment.isVisible()){
            return nowFragment;
        }

        return null;
    }

    /*
    * 返回上一个fragment的信息
    * */

    public static String getPreviousFragment(){
        return previousFragmentTag;
    }



}
