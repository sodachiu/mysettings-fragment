package com.example.eileen.mysettings_fragment.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.example.eileen.mysettings_fragment.AboutFragment;
import com.example.eileen.mysettings_fragment.EthFragment;
import com.example.eileen.mysettings_fragment.EthPppoeFragment;
import com.example.eileen.mysettings_fragment.EthTypeFragment;
import com.example.eileen.mysettings_fragment.MainActivity;
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
    public static final String DISPLAY_FRAGMENT = "display_fragment";
    public static final String STORAGE_FRAGMENT = "storage_fragment";
    public static final String ADVANCED_FRAGMENT = "advanced_fragment";
    public static final String RECOVERY_FRAGMENT = "recovery_fragment";

    private static AboutFragment aboutFragment = new AboutFragment();
    private static EthFragment ethFragment = new EthFragment();
    private static EthTypeFragment ethTypeFragment = new EthTypeFragment();
    private static EthPppoeFragment ethPppoeFragment = new EthPppoeFragment();




    /*显示指定的fragment*/
    public static void showFragment(Context context, String tag){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment;
        Log.i(TAG, "showFragment: 传入的tag为---->" + tag);
        switch (tag){
            case ABOUT_FRAGMENT:
                fragment = aboutFragment;
                break;
            case ETH_FRAGMENT:
                fragment = ethFragment;
                break;
            case ETH_TYPE_FRAGMENT:
                fragment = ethTypeFragment;
                break;
            case ETH_PPPOE_FRAGMENT:
                fragment = ethPppoeFragment;
                break;
            default:
                Log.i(TAG, "startFragment: 传入tag有误，请检查");
                return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        Log.i(TAG, "showFragment: 被加入栈的fragment数量---->" + fragments.size());

        for (int i = 0; i < fragments.size(); i++){
            Fragment tmpFragment = fragments.get(i);
            if (tmpFragment != null){
                transaction.hide(tmpFragment);
            }
        }

        if (!fragment.isAdded()){
            transaction.add(R.id.main_ll_fragment_container,fragment, tag);
        }
        transaction.show(fragment);

        transaction.commit();

    }

    /*返回当前fragment的tag*/
    public static String getCurrentFragmentTag(Context context){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        String tag = "";
        for (int i = 0; i < fragments.size(); i++){
            Fragment tmpFragment = fragments.get(i);
            if (tmpFragment != null && tmpFragment.isAdded() && tmpFragment.isVisible()){
                tag = tmpFragment.getTag();
            }
        }

        return tag;
    }

}
