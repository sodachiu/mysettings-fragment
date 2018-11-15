package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    private static final String TAG = "qll_fragment_about";
    private Context mContext;
    private View mView;

    public AboutFragment() {
        Log.i(TAG, "AboutFragment: ");
        
    }
    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_about, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        TextView tvModel = (TextView) mView.findViewById(R.id.about_tv_model);
        TextView tvSoftVersion = (TextView) mView.findViewById(R.id.about_tv_software_version);
        TextView tvSystemVersion = (TextView) mView.findViewById(R.id.about_tv_system_version);
        TextView tvMac = (TextView) mView.findViewById(R.id.about_tv_mac);
        TextView tvStbid = (TextView) mView.findViewById(R.id.about_tv_stbid);
        TextView tvAuthAddr1 = (TextView) mView.findViewById(R.id.about_tv_auth_addr1);
        TextView tvAuthAddr2 = (TextView) mView.findViewById(R.id.about_tv_auth_addr2);
        TextView tvTeminal1 = (TextView) mView.findViewById(R.id.about_tv_terminal_addr1);
        TextView tvTeminal2 = (TextView) mView.findViewById(R.id.about_tv_terminal_addr2);
        TextView tvAccount = (TextView) mView.findViewById(R.id.about_tv_auth_account);

        String sModel = SystemProperties.get("ro.product.model"); //硬件产品品牌型号
        String sSoftware = SystemProperties.get("ro.build.version.incremental"); //软件版本
        String sAndroid = SystemProperties.get("ro.build.version.release"); //安卓版本
        String sMAC = SystemProperties.get("ro.mac"); //mac地址
        String sStbid = SystemProperties.get("ro.serialno"); //32位设备串号，同Build.SERIAL
        String sTerminal1 = SystemProperties.get("persist.sys.tr069.ServerURL"); //终端管理主地址
        String sTerminal2 = SystemProperties.get("persist.sys.tr069.ServerURLBK"); //终端管理备地址
        String sAccout = SystemProperties.get("ro.deviceid"); //15位串号
        String sAuthen1 = "";
        String sAuthen2 = "";

        Uri tableAuth  = Uri.parse("content://stbconfig/authentication");
        Cursor cursor;

        try{
            cursor = mContext.getContentResolver().query(tableAuth,
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()){

                Log.i(TAG, "onActivityCreated: 数据表的行数：" + cursor.getCount());

                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String value = cursor.getString(cursor.getColumnIndex("value"));
                    Log.i(TAG, "onActivityCreated: " + name + "----" + value);
                    if (sAuthen1.equals("") && name.equals("eds_server")){
                        sAuthen1 = value;
                    }

                    if (sAuthen2.equals("") && name.equals("eds_server2")){
                        sAuthen2 = value;
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }else {
                Log.i(TAG, "onActivityCreated: cursor为null");
            }

        }catch (Exception e){
            Log.e(TAG, "onActivityCreated: 获取信息失败");
        }finally {
            tvModel.setText(sModel);
            tvSoftVersion.setText(sSoftware);
            tvSystemVersion.setText(sAndroid);
            tvMac.setText(sMAC);
            tvStbid.setText(sStbid);
            tvAuthAddr1.setText(sAuthen1);
            tvAuthAddr2.setText(sAuthen2);
            tvTeminal1.setText(sTerminal1);
            tvTeminal2.setText(sTerminal2);
            tvAccount.setText(sAccout);
        }

    }






    @Override
    public void onDetach() {
        super.onDetach();
    }

}
