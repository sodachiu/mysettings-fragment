package com.android.settings.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.settings.R;

public class MyHandler extends Handler {
    private static final String TAG = "qll_my_handler";
    public static final int STATIC_CONNECT_SUCCESS = 0;
    public static final int STATIC_CONNECT_FAILED = 1;
    public static final int NET_AVAILABLE = 14;
    public static final int CONNECTING = 2;
    public static final int STATIC_IP_NULL = 3;
    public static final int STATIC_NETMASK_NULL = 4;
    public static final int STATIC_GATEWAY_NULL = 5;
    public static final int STATIC_DNS1_NULL = 6;
    public static final int STATIC_DNS2_NULL = 7;
    public static final int STATIC_IP_ILLEGAL = 8;
    public static final int STATIC_NETMASK_ILLEGAL = 9;
    public static final int STATIC_GATEWAY_ILLEGAL = 10;
    public static final int STATIC_DNS1_ILLEGAL = 11;
    public static final int STATIC_DNS2_ILLEGAL = 12;
    public static final int IP_GATEWAY_ERR = 13;//ip & gateway不在同一个网段
    public static final int DHCP_CONNECT_SUCCESS = 16;
    public static final int DHCP_CONNECT_FAILED = 17;
    public static final int LINK_DOWN = 18; //网线脱落
    public static final int PPPOE_USERNAME_EMPTY = 19;
    public static final int PPPOE_PASSWORD_EMPTY = 20;
    public static final int PPPOE_CONNECT_SUCCESS = 21;
    public static final int PPPOE_CONNECT_FAILED_999 = 23;
    public static final int PPPOE_CONNECT_FAILED_OTHER = 24;
    public static final int BLUETOOTH_NO_ADAPTER = 25;
    public static final int BLUETOOTH_TURNING_ON = 26;
    public static final int BLUETOOTH_ON = 27;
    public static final int BLUETOOTH_TURNING_OFF = 28;
    public static final int BLUETOOTH_OFF = 29;
    public static final int BLUETOOTH_DISCOVERING = 30;
    public static final int BLUETOOTH_DISCOVERED = 31;
    public static final int BLUETOOTH_DEVICE_BONDING = 32;
    public static final int BLUETOOTH_DEVICE_BOND_NONE = 33;
    public static final int DT_FORMAT_ALREADY_USE = 34;
    public static final int STORAGE_MOUNTED = 35;
    public static final int STORAGE_UNMOUNTED = 36;
    public static final int STORAGE_NO_UNMOUNT = 37;


    private Context handlerContext;

    public MyHandler(Context context){
        handlerContext = context;
    }

    @Override
    public void handleMessage(Message msg){
        String toastInfo = "";
        Log.i(TAG, "handleMessage: 收到的消息：" + msg.what);
        switch (msg.what){
            case STATIC_CONNECT_SUCCESS:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_connect_success);
                break;
            case STATIC_CONNECT_FAILED:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_connect_failed);
                break;
            case STATIC_IP_NULL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_ip_null);
                break;
            case STATIC_IP_ILLEGAL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_ip_illegal);
                break;
            case STATIC_NETMASK_NULL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_mask_null);
                break;
            case STATIC_NETMASK_ILLEGAL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_mask_illegal);
                break;
            case STATIC_GATEWAY_NULL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_gateway_null);
                break;
            case STATIC_GATEWAY_ILLEGAL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_gateway_illegal);
                break;
            case STATIC_DNS1_NULL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_dns1_null);
                break;
            case STATIC_DNS1_ILLEGAL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_dns1_illegal);
                break;
            case STATIC_DNS2_NULL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_dns2_null);
                break;
            case STATIC_DNS2_ILLEGAL:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_dns2_illegal);
                break;
            case CONNECTING:
                toastInfo = handlerContext.getResources().getString(R.string.eth_connecting);
                break;
            case IP_GATEWAY_ERR:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_ip_gateway_err);
                break;
            case NET_AVAILABLE:
                toastInfo = handlerContext.getResources().getString(R.string.eth_static_net_available);
                break;
            case DHCP_CONNECT_SUCCESS:
                toastInfo = handlerContext.getResources().getString(R.string.eth_dhcp_connect_success);
                break;
            case DHCP_CONNECT_FAILED:
                toastInfo = handlerContext.getResources().getString(R.string.eth_dhcp_connect_failed);
                break;
            case LINK_DOWN:
                toastInfo = handlerContext.getResources().getString(R.string.eth_no_cable);
                break;
            case PPPOE_USERNAME_EMPTY:
                toastInfo = handlerContext.getResources().getString(R.string.eth_pppoe_username_empty);
                break;
            case PPPOE_PASSWORD_EMPTY:
                toastInfo = handlerContext.getResources().getString(R.string.eth_pppoe_userpwd_empty);
                break;
            case PPPOE_CONNECT_SUCCESS:
                toastInfo = handlerContext.getResources().getString(R.string.eth_pppoe_connect_success);
                break;
            case PPPOE_CONNECT_FAILED_999:
                toastInfo = handlerContext.getResources().getString(R.string.eth_pppoe_connect_failed_999);
                break;
            case PPPOE_CONNECT_FAILED_OTHER:
                toastInfo = handlerContext.getResources().getString(R.string.eth_pppoe_connect_failed_other);
                break;
            case BLUETOOTH_NO_ADAPTER:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_no_adapter);
                break;
            case BLUETOOTH_TURNING_ON:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_turning_on);
                break;
            case BLUETOOTH_TURNING_OFF:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_turning_off);
                break;
            case BLUETOOTH_ON:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_on);
                break;
            case BLUETOOTH_OFF:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_off);
                break;
            case BLUETOOTH_DEVICE_BONDING:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_bonding);
                break;
            case BLUETOOTH_DEVICE_BOND_NONE:
                toastInfo = handlerContext.getResources().getString(R.string.eth_bluetooth_bond_none);
                break;
            case DT_FORMAT_ALREADY_USE:
                toastInfo = handlerContext.getResources().getString(R.string.dt_format_used);
                break;
            case STORAGE_MOUNTED:
                toastInfo = handlerContext.getString(R.string.storage_mounted);
                break;
            case STORAGE_UNMOUNTED:
                toastInfo = handlerContext.getString(R.string.storage_unmounted);
                break;
            case STORAGE_NO_UNMOUNT:
                toastInfo = handlerContext.getString(R.string.storage_no_device_unmount);
                break;
            default:
                break;
        }
        Toast.makeText(handlerContext, toastInfo, Toast.LENGTH_SHORT).show();
    }

    public void clear(){
        Log.i(TAG, "clear: ");
        handlerContext = null;
        removeCallbacksAndMessages(null);
    }
}
