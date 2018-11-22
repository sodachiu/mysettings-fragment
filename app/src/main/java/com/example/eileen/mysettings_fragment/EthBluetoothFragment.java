package com.example.eileen.mysettings_fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.eileen.mysettings_fragment.network.MyBluetoothAdapter;
import com.example.eileen.mysettings_fragment.utils.CircularLinesProgress;
import com.example.eileen.mysettings_fragment.utils.MyHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class EthBluetoothFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "qll_bluetooth_fragment";
    private Context mContext;
    private ListView lvMenu;
    private LinearLayout llBluetoothSwitch, llPgbContainer;
    private ImageView imgBluetoothSwitch;
    private RecyclerView rvBond, rvUnbond;
    private BluetoothAdapter mBluetoothAdapter;
    private MyHandler mHandler;
    private IntentFilter mFilter;
    private List<BluetoothDevice> mBondList, mUnbondList;
    private MyBluetoothAdapter mBondAdapter, mUnbondAdapter;
    private static int mUnbondListSize = 0;
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
        mBondList = new ArrayList<>();
        mUnbondList = new ArrayList<>();
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
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mHandler.clear();
    }


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.eth_ll_bluetooth_switch:
                if (mBluetoothAdapter.isEnabled()){
                    mBluetoothAdapter.disable();
                }else {
                    mBluetoothAdapter.enable();
                }
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
        rvBond = (RecyclerView) activity.findViewById(R.id.bluetooth_rv_bond_list);
        rvUnbond = (RecyclerView) activity.findViewById(R.id.bluetooth_rv_unbond_list);
        llPgbContainer = (LinearLayout) activity.findViewById(R.id.bluetooth_ll_pgb_container);
        LinearLayoutManager llManager1 = new LinearLayoutManager(mContext);
        LinearLayoutManager llManager2 = new LinearLayoutManager(mContext);
        rvBond.setLayoutManager(llManager1);
        rvUnbond.setLayoutManager(llManager2);

        if (mBluetoothAdapter.isEnabled()){
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_on);
            mBluetoothAdapter.startDiscovery();
        }else {
            imgBluetoothSwitch.setImageResource(R.drawable.checkbox_off);
        }
        llBluetoothSwitch.setOnClickListener(this);
        llBluetoothSwitch.requestFocus();
        lvMenu.setFocusable(false);

    }

    /**
     * 处理 BluetoothAdapter.ACTION_STATE_CHANGED
     * BluetoothAdapter.STATE_ON
     */
    void handleOnEvent(){
        Log.i(TAG, "handleOnEvent: 蓝牙打开成功");
        
        mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_TURNING_ON);
        llPgbContainer.setVisibility(View.GONE);
        imgBluetoothSwitch.setImageResource(R.drawable.checkbox_on);
    }

    /**
     * 处理 BluetoothAdapter.ACTION_STATE_CHANGED
     * BluetoothAdapter.STATE_TURNING_ON
     */
    void handleTurningOnEvent(){

        Log.i(TAG, "handleTurningOnEvent: 正在打开蓝牙");

        Set<BluetoothDevice> bondDevice = mBluetoothAdapter.getBondedDevices();
        mBondList.addAll(bondDevice);

        mBondAdapter = new MyBluetoothAdapter(mBondList);
        mUnbondAdapter = new MyBluetoothAdapter(mUnbondList);

        rvBond.setAdapter(mBondAdapter);
        rvUnbond.setAdapter(mUnbondAdapter);

        llPgbContainer.setVisibility(View.VISIBLE);//下面一层应该也不能操作了
        rvUnbond.setVisibility(View.VISIBLE);
        if (mBondList.size() > 0){
            rvBond.setVisibility(View.VISIBLE);
        }else {
            rvBond.setVisibility(View.GONE);
        }
    }

    /**
     * 处理 BluetoothAdapter.ACTION_STATE_CHANGED
     * BluetoothAdapter.STATE_TURNING_OFF
     */
    void handleTurningOffEvent(){
        Log.i(TAG, "handleOffEvent: 正在关闭蓝牙");
        llPgbContainer.setVisibility(View.VISIBLE);
        rvBond.setVisibility(View.GONE);
        rvUnbond.setVisibility(View.GONE);
        mBondList.clear();
        mUnbondList.clear();
        mUnbondAdapter = null;
        mBondAdapter = null;
    }

    void handleOffEvent(){
        Log.i(TAG, "handleOffEvent: 蓝牙关闭成功");
        mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_OFF);
        llPgbContainer.setVisibility(View.GONE);
        imgBluetoothSwitch.setImageResource(R.drawable.checkbox_off);

    }

    /**
     * 处理 BluetoothDevice.ACTION_FOUND
     *
     */
    void handleFindEvent(BluetoothDevice newDevice){

        if (newDevice.getBondState() != BluetoothDevice.BOND_BONDED
                && !mUnbondList.contains(newDevice)){

            Log.i(TAG, "handleFindEvent: 发现一台新的远程设备" + newDevice.toString());
            mUnbondList.add(newDevice);
            mUnbondAdapter.notifyItemRangeInserted(mUnbondListSize++ , 1); //需要测试一下是否是先返回原值，再执行+1操作
        }
    }



    private BroadcastReceiver bluetoothReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接收广播：" + action);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (bluetoothState){
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.i(TAG, "onReceive: 正在打开蓝牙");
                        handleTurningOnEvent();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(TAG, "onReceive: 蓝牙打开成功");
                        handleOnEvent();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i(TAG, "onReceive: 正在关闭蓝牙");
                        handleTurningOffEvent();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.i(TAG, "onReceive: 蓝牙关闭成功");
                        handleOffEvent();
                        break;
                    default:
                        Log.i(TAG, "onReceive: 未处理蓝牙状态改变事件代码：" + bluetoothState);
                        break;
                }
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                Log.i(TAG, "onReceive: 搜索设备结束，找到" + mUnbondList.size() + "台远程设备");
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
                Log.i(TAG, "onReceive: 开始搜索设备");
            }else if (action.equals(BluetoothDevice.ACTION_FOUND)){

                try{
                    BluetoothDevice newDevice = (BluetoothDevice) intent.
                            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //当我刚进程序时，如果蓝牙是打开状态，那么应该有个圈圈，然后显示
                    handleFindEvent(newDevice);

                }catch (Exception e){
                    Log.e(TAG, "onReceive: 获取设备失败" + e.toString());
                }

            }else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){

                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                switch (bondState){
                    case BluetoothDevice.BOND_NONE:
                        //什么也没绑定
                        Log.i(TAG, "onReceive: 解绑设备成功");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        //绑定成功
                        Log.i(TAG, "onReceive: 绑定远程设备成功");
                        mUnbondListSize--;
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

}
