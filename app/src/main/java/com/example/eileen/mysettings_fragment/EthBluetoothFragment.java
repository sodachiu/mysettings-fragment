package com.example.eileen.mysettings_fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

//import com.example.eileen.mysettings_fragment.network.BluetoothUtil;
import com.example.eileen.mysettings_fragment.network.BondedBluetoothAdapter;
import com.example.eileen.mysettings_fragment.network.UnbondBluetoothAdapter;
import com.example.eileen.mysettings_fragment.utils.MyHandler;
import com.example.eileen.mysettings_fragment.utils.UniqueMark;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

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
    private UnbondBluetoothAdapter mUnbondAdapter;
    private BondedBluetoothAdapter mBondAdapter;
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
        mContext.registerReceiver(bluetoothReceiver, mFilter);
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
        mContext.unregisterReceiver(bluetoothReceiver);
    }

    /*
    * 用于处理dialogfragment返回的数据
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UniqueMark.BLUETOOTH_BOND && resultCode == RESULT_OK){
            Log.i(TAG, "onActivityResult: 解绑设备");
            BluetoothDevice device = (BluetoothDevice) data.getParcelableExtra("bonded_device");
            device.removeBond();
        }else if (requestCode == UniqueMark.BLUETOOTH_UNBOND && resultCode == RESULT_OK){
            Log.i(TAG, "onActivityResult: 绑定设备");
            BluetoothDevice device = (BluetoothDevice) data.getParcelableExtra("unbond_device");
            device.createBond();
        }
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
        
        llPgbContainer.setVisibility(View.GONE);
        imgBluetoothSwitch.setImageResource(R.drawable.checkbox_on);
    }

    /**
     * 处理 BluetoothAdapter.ACTION_STATE_CHANGED
     * BluetoothAdapter.STATE_TURNING_ON
     */
    void handleTurningOnEvent(){

        Log.i(TAG, "handleTurningOnEvent: 正在打开蓝牙");
        mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_TURNING_ON);
        llPgbContainer.setVisibility(View.VISIBLE);

        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        mBluetoothAdapter.startDiscovery();
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
        mBluetoothAdapter.cancelDiscovery();
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

        /*找到 8 个 远程设备并且菊花还在转，隐藏菊花*/
        if (mUnbondListSize > 8 && llPgbContainer.getVisibility() == View.VISIBLE){
            llPgbContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 处理 BluetoothAdapter.ACTION_DISCOVERY_STARTED
     */
    void handleStartDiscoveryEvent(){
        Set<BluetoothDevice> bondDevice = mBluetoothAdapter.getBondedDevices();
        mBondList.addAll(bondDevice);

        mBondAdapter = new BondedBluetoothAdapter(mBondList);
        mUnbondAdapter = new UnbondBluetoothAdapter(mUnbondList);

        rvBond.setAdapter(mBondAdapter);
        rvUnbond.setAdapter(mUnbondAdapter);

        /*如果在开始扫描时，菊花不可见，则显示菊花*/
        if (llPgbContainer.getVisibility() == View.GONE){
            llPgbContainer.setVisibility(View.VISIBLE);
        }
        rvUnbond.setVisibility(View.VISIBLE);
        if (mBondList.size() > 0){
            rvBond.setVisibility(View.VISIBLE);
        }else {
            rvBond.setVisibility(View.GONE);
        }
    }


    /**
     * 处理 BluetoothDevice.ACTION_BOND_STATE_CHANGED
     */
    void handleBondStateChangedEvent(int state, BluetoothDevice tmpDevice){

//        BluetoothUtil.handleStateChangedEvent(mUnbondList, mBondList, tmpDevice, state);

        if (tmpDevice == null || tmpDevice.getAddress().equals("")){
            Log.i(TAG, "handleBondStateChangedEvent: 获取到的设备为空, 无法进行处理");
            return;
        }

        String devAddress = tmpDevice.getAddress();

        switch (state){
            case BluetoothDevice.BOND_NONE:
                Log.i(TAG, "handleBondStateChangedEvent: 远程设备已解绑");
                handleBondNoneEvent(devAddress, tmpDevice);
                break;
            case BluetoothDevice.BOND_BONDED:
                Log.i(TAG, "handleBondStateChangedEvent: 远程设备已绑定");
                handleBondedEvent(devAddress, tmpDevice);
                break;
            case BluetoothDevice.BOND_BONDING:
                Log.i(TAG, "onReceive: 正在绑定远程设备");
                mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_DEVICE_BONDING);
                break;
            default:
                Log.i(TAG, "onReceive: 未处理的绑定事件代码：" + state);
                break;
        }

    }


    /**
    * 处理设备解绑事件
    * */
    void handleBondNoneEvent(String devAddress, BluetoothDevice tmpDevice){
        mHandler.sendEmptyMessage(MyHandler.BLUETOOTH_DEVICE_BOND_NONE);
        int position = -1;
        for (int i = 0; i < mBondList.size(); i++){
            if (devAddress.equals(mBondList.get(i).getAddress())){
                position = i;
            }
        }

        if (position != -1){
            mBondList.remove(position);
            mUnbondList.add(tmpDevice);
            mUnbondAdapter.notifyItemInserted(mUnbondListSize);
            mBondAdapter.notifyItemRangeRemoved(position, 1);
            Log.i(TAG, "handleBondStateChangedEvent: 成功移除绑定设备");
        }else {
            Log.i(TAG, "handleBondStateChangedEvent: 没有找到匹配项");
        }

        if (mBondList.size() <= 0){
            rvBond.setVisibility(View.GONE);
        }
    }

    /**
     * 处理设备绑定成功事件
     * */

    void handleBondedEvent(String devAddress, BluetoothDevice tmpDevice){
        Log.i(TAG, "handleBondedEvent: ");
        mUnbondListSize--;
        int position = -1;
        for (int j = 0; j < mUnbondList.size(); j++){
            if (devAddress.equals(mUnbondList.get(j).getAddress())){
                position = j;
            }
        }

        if (position != -1){
            mUnbondList.remove(position);
            mUnbondAdapter.notifyItemRangeRemoved(position, 1);
            mBondList.add(tmpDevice);
            rvBond.setVisibility(View.VISIBLE);
            mBondAdapter.notifyItemInserted(mBondList.size() - 1);
            Log.i(TAG, "handleBondStateChangedEvent: 成功显示绑定设备");
        }else {
            Log.i(TAG, "handleBondStateChangedEvent: 没有找到匹配项");
        }
    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
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
                handleStartDiscoveryEvent();

            }else if (action.equals(BluetoothDevice.ACTION_FOUND)){

                try{
                    BluetoothDevice newDevice = (BluetoothDevice) intent.
                            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    handleFindEvent(newDevice);

                }catch (Exception e){
                    Log.e(TAG, "onReceive: 获取设备失败" + e.toString());
                }

            }else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){

                try {
                    BluetoothDevice tmpDevice = (BluetoothDevice) intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i(TAG, "onReceive: 获取设备成功:" + tmpDevice.toString());

                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                    Log.i(TAG, "onReceive: 获取状态成功：" + bondState);

                    handleBondStateChangedEvent(bondState, tmpDevice);

                }catch (Exception e){
                    Log.e(TAG, "onReceive: 获取绑定状态变化的设备失败：" + e.toString());
                }

            }else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)){
                Log.i(TAG, "onReceive: 请求配对");
            }else {
                Log.i(TAG, "onReceive: 我也晕了不知道啥广播");
            }
        }
    };

}
