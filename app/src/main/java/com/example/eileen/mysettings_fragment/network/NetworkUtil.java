package com.example.eileen.mysettings_fragment.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.util.Log;

import com.example.eileen.mysettings_fragment.R;

import java.util.regex.Pattern;

public class NetworkUtil {
    private static final String TAG = "qll_network_util";
    public static final int ETHERNET_MODE_PPPOE = 2;
    public static final int ETHERNET_MODE_DHCP = 0;
    public static final int ETHERNET_MODE_STATIC = 1;

    /*
     * 检查ip，网关，dns的格式是否合法
     * true：合法
     * false: 不合法
     * */
    public static boolean checkDhcpItem(String ip){

        Log.i(TAG, "checkDhcpItem: ");
        String ipRegEx = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        return Pattern.matches(ipRegEx, ip);

    }


    /*
     * 检查ip和网关是否在同一个网段
     * true: 在同一个网段
     * false：不在同一个网段
     * */
    public static boolean checkInOneSegment(String ip, String gateway){
        Log.i(TAG, "checkInOneSegment: ip---->" + ip + " gateway---->" + gateway);
        try{
            String[] ips = ip.split("\\.");
            String[] gateways = gateway.split("\\.");
            for (int i = 0; i < 3; i++){
                if (!ips[i].equals(gateways[i])){
                    Log.i(TAG, "checkInOneSegment: 不在同一网段，错误");
                    return false;
                }
            }
            Log.i(TAG, "checkInOneSegment: 同一网段，正确");
            return true;
        }catch (Exception e){
            Log.i(TAG, "checkInOneSegment: 分割字符出错");
            return false;

        }
    }

    /*
     * 检查网络是否连接
     * */
    public static boolean checkNetConnect(Context context){

        Log.i(TAG, "checkNetAvailable: ");
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            Log.i(TAG, "checkNetAvailable: 网络可用");
            return true;
        }

        Log.i(TAG, "checkNetAvailable: 网络不可用");
        return false;
    }

    /*
    * 检查网线是否脱落
    *
    * */

    public static boolean checkIsLinkUp(Context context){
        EthernetManager em = (EthernetManager) context.getSystemService(Context.ETHERNET_SERVICE);
        return em.getNetLinkStatus();
    }

    public static class MyDhcpInfo{
        private String ipAddress, netMask, gateway, dns1, dns2;
        private EthernetManager em;
        private PppoeManager pm;
        private DhcpInfo dhcpInfo;
        private String defaultValue;

        public MyDhcpInfo(Context context, boolean isNetAvailable){

            Log.i(TAG, "MyDhcpInfo: ");
            em = (EthernetManager) context.getSystemService(Context.ETHERNET_SERVICE);
            pm = (PppoeManager) context.getSystemService(Context.PPPOE_SERVICE);
            String ethMode = em.getEthernetMode();
            defaultValue = context.getResources().getString(R.string.eth_default_value);

            ipAddress = defaultValue;
            netMask = defaultValue;
            gateway = defaultValue;
            dns1 = defaultValue;
            dns2 = defaultValue;

            if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                Log.i(TAG, "MyDhcpInfo: 当前网络连接模式为PPPOE，dhcp的信息");
                this.dhcpInfo = pm.getDhcpInfo();
            }else {
                Log.i(TAG, "MyDhcpInfo: 当前网络连接模式为DHCP或Static，获取dhcp的信息");
                this.dhcpInfo = em.getDhcpInfo();
            }

            Log.i(TAG, "MyDhcpInfo: dhcpInfo是否为空---->" + (dhcpInfo == null));
            if (dhcpInfo != null && isNetAvailable){
                this.ipAddress = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                this.netMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                this.gateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                this.dns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                this.dns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();
            }
        }

        public String getIpAddress() {
            Log.i(TAG, "getIpAddress: ");
            return ipAddress;
        }

        public String getNetMask() {
            Log.i(TAG, "getNetMask: ");
            return netMask;
        }

        public String getGateway() {
            Log.i(TAG, "getGateway: ");
            return gateway;
        }

        public String getDns1() {
            Log.i(TAG, "getDns1: ");
            return dns1;
        }

        public String getDns2() {
            Log.i(TAG, "getDns2: ");
            return dns2;
        }

        @Override
        public String toString() {
            Log.i(TAG, "toString: ");
            return "ip:" + ipAddress + "  netmask:" + netMask + "  gateway" + gateway +
                    "  dns1:" + dns1 + "  dns2:" + dns2;
        }


        public String getDefaultValue(){
            Log.i(TAG, "getDefaultValue: ");
            return this.defaultValue;
        }

    }
}
