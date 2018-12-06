package com.example.eileen.mysettings_fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.storage.StorageUtil;
import com.example.eileen.mysettings_fragment.utils.DoCmd;
import com.example.eileen.mysettings_fragment.utils.FragmentUtil;
import com.example.eileen.mysettings_fragment.utils.MyHandler;
import com.example.eileen.mysettings_fragment.utils.ShowDialog;
import com.example.eileen.mysettings_fragment.utils.UniqueMark;

public class StorageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "qll_storage_fragment";
    private Context mContext;
    private TextView tvTotal, tvAvailable, tvDeviceNum1, tvDeviceNum2;
    private LinearLayout llUninstall;
    private String[] storagePaths;
    private MyHandler myHandler;
    public static final String EXTERNAL_MATCH = "/mnt/sd";

    public StorageFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        mContext.registerReceiver(myStorageReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        View view = inflater.inflate(R.layout.fragment_storage, parent, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle args) {
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(args);
        initView();
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mContext.unregisterReceiver(myStorageReceiver);
        myHandler.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: ");
        if (requestCode != UniqueMark.STORAGE_FRAGMENT) {
            Log.i(TAG, "onActivityResult: 请求码错误");
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            // 卸载设备
            Log.i(TAG, "onActivityResult: 确定卸载");
            uninstallStorage();
        }
    }

    void initView() {
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        llUninstall = (LinearLayout) activity.findViewById(R.id.storage_ll_uninstall);
        tvTotal = (TextView) activity.findViewById(R.id.storage_tv_total);
        tvAvailable = (TextView) activity.findViewById(R.id.storage_tv_available);
        tvDeviceNum1 = (TextView) activity.findViewById(R.id.storage_tv_device_num1);
        tvDeviceNum2 = (TextView) activity.findViewById(R.id.storage_tv_device_num2);
       
        llUninstall.setOnClickListener(this);

        setValues();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.storage_ll_uninstall:
                Log.i(TAG, "onClick: 点击卸载外部设备，尝试显示dialog");
                showDialog();
                break;
            default:
                Log.i(TAG, "onClick: 点击了----" + view.getId());
        }
    }

    void setValues() {
        Log.i(TAG, "setValues: ");

        storagePaths = StorageUtil.getVolumePaths(mContext);
        String sAvailable = getAvailable();
        String sTotal = getTotal();

        int deviceNum = storagePaths.length;
        String sDeviceNum = getString(R.string.storage_number);
        sDeviceNum = String.format(sDeviceNum, deviceNum);

        tvTotal.setText(sTotal);
        tvAvailable.setText(sAvailable);
        tvDeviceNum1.setText(sDeviceNum);
        tvDeviceNum2.setText(sDeviceNum);
    }

    void showDialog() {
        Log.i(TAG, "showDialog: ");

        if (storagePaths.length == 1) {
            myHandler = new MyHandler(mContext);
            myHandler.sendEmptyMessage(MyHandler.STORAGE_NO_UNMOUNT);
            return;
        }
        Fragment targetFragment = FragmentUtil.getCurrentFragment(mContext);
        int requestCode = UniqueMark.STORAGE_FRAGMENT;
        ShowDialog.showDialog(null, targetFragment, requestCode);
    }

    private BroadcastReceiver myStorageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || action.equals("")) {
                Log.i(TAG, "onReceive: 没有接收到任何广播");
            } else {
                Log.i(TAG, "onReceive: 收到广播----" + action);
                setValues();
            }
        }
    };

    String getTotal() {
        long size = 0;
        for (String path : storagePaths) {
            size += StorageUtil.getTotalSize(path);
        }
        return Formatter.formatFileSize(mContext, size);
    }

    String getAvailable() {
        long size = 0;
        for (String path: storagePaths) {
            size += StorageUtil.getAvailableSize(path);
        }
        return Formatter.formatFileSize(mContext, size);
    }

    void uninstallStorage() {

        for (String path : storagePaths) {
            int match = path.indexOf(EXTERNAL_MATCH);
           /* String cmd = "rm -rf ";*/

            if (match >= 0) {
                /*cmd = cmd + path;
                DoCmd.doCmd(cmd);*/
                StorageUtil.unmountVolume(path);
            }
        }

    }



}
