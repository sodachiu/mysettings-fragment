package com.example.eileen.mysettings_fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.eileen.mysettings_fragment.utils.MyHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public class EthBluetoothFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_bluetooth_fragment";
    private Context mContext;
    private ListView lvMenu;
    private LinearLayout llBluetoothSwitch;
    private ImageView imgBluetoothSwitch;
    private BluetoothAdapter mBluetoothAdapter;
    private MyHandler mHandler;
    private IntentFilter mFilter;

    public EthBluetoothFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mHandler = new MyHandler(mContext);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_NO_ADAPTER);
        }

        mFilter = new IntentFilter();
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        mContext.registerReceiver(bluetoothReveiver, mFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_eth_bluetooth, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        Log.i(TAG, "onHiddenChanged: 碎片是否可见：" + hidden);
        if (!hidden){
            lvMenu.setFocusable(false);
            mContext.registerReceiver(bluetoothReveiver, mFilter);
        }else {
            mHandler.clear();
            mContext.unregisterReceiver(bluetoothReveiver);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.eth_ll_bluetooth_switch:
                changeBluetoothState();
                break;
            default:
        }
    }

    private void initView(){
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        lvMenu = (ListView) activity.findViewById(R.id.main_lv_menu);
        llBluetoothSwitch = (LinearLayout) activity.findViewById(R.id.eth_ll_bluetooth_switch);
        imgBluetoothSwitch = (ImageView) activity.findViewById(R.id.eth_img_bluetooth_switch);
        if (mBluetoothAdapter.isEnabled()){
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_on);
        }else {
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_off);
        }
        llBluetoothSwitch.setOnClickListener(this);
        lvMenu.setFocusable(false);

    }



    private BroadcastReceiver bluetoothReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接收广播：" + action);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
//                changeBluetoothState();
                int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (bluetoothState){
                    case BluetoothAdapter.STATE_TURNING_ON:
                        mBluetoothAdapter.startDiscovery();
                        //显示菊花
                        Log.i(TAG, "onReceive: 正在打开蓝牙");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        //菊花关掉 & button改变，填充两个listviw
                        Log.i(TAG, "onReceive: 蓝牙打开成功");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //显示菊花
                        mBluetoothAdapter.cancelDiscovery();
                        Log.i(TAG, "onReceive: 正在关闭蓝牙");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        //关掉菊花， button改变，两个listview不可见
                        Log.i(TAG, "onReceive: 蓝牙关闭成功");
                        break;
                    default:
                        Log.i(TAG, "onReceive: 未处理蓝牙状态改变事件代码：" + bluetoothState);
                        break;
                }
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                //弹框提示搜索设备结束
                Log.i(TAG, "onReceive: 搜索设备结束");
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
                //弹框提示"正在搜索设备"
                Log.i(TAG, "onReceive: 开始搜索设备");
            }else if (action.equals(BluetoothDevice.ACTION_FOUND)){
                try{
                    BluetoothDevice newDevice = (BluetoothDevice) intent.
                            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i(TAG, "onReceive: 新发现的设备：" + newDevice.toString());
                }catch (Exception e){
                    Log.e(TAG, "onReceive: 获取设备失败" + e.toString());
                }

                //该广播会携带extra_fiels--->EXTRA_DEVICE
                //给listview加item
            }else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                switch (bondState){
                    case BluetoothDevice.BOND_NONE:
                        //什么也没绑定
                        Log.i(TAG, "onReceive: 没有绑定任何远程设备");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        //绑定成功
                        Log.i(TAG, "onReceive: 绑定远程设备成功");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        //正在绑定
                        Log.i(TAG, "onReceive: 正在绑定远程设备");
                        break;
                    default:
                        Log.i(TAG, "onReceive: 未处理的绑定事件代码：" + bondState);
                        break;
                }

            }else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)){
                Log.i(TAG, "onReceive: 请求配对");
            }else {
                Log.i(TAG, "onReceive: 我也晕了不知道啥广播");
            }
        }
    };

    private void changeBluetoothState(){
        Log.i(TAG, "changeBluetoothState: ");
        if (mBluetoothAdapter.isEnabled()){
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_off);
            mBluetoothAdapter.disable();
        }else {
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_on);
            mBluetoothAdapter.enable();
        }
    }
}
