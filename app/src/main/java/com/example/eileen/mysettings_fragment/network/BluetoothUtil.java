/*
package com.example.eileen.mysettings_fragment.network;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BluetoothUtil {

    private static final String TAG = "qll_blue_util";
    public static void handleStateChangedEvent(List<BluetoothDevice> bondList,
                                               List<BluetoothDevice> unbondList,
                                               BluetoothDevice stateChangedDevice,
                                               int state){

        // 认真思考了以下，传过来的List应该是引用，所以在这里修改也是影响原list的
        switch (state){
            case BluetoothDevice.BOND_NONE:

                int position = -1;
                for (int i = 0; i < bondList.size(); i++){
                    if (stateChangedDevice == bondList.get(i)){
                        position = i;
                    }
                }
                Log.i(TAG, "handleBondStateChangedEvent: 找到匹配项，坐标为：" + position);
                if (position != -1){
                    try {
                        bondList.remove(position);
//                        mBondAdapter.notifyItemRangeRemoved(position, 1);

                        unbondList.add(tmpDevice);
//                        mUnbondAdapter.notifyItemInserted(unbondListSize);
                    }catch (Exception e){
                        Log.e(TAG, "handleBondStateChangedEvent: 移出绑定设备出错" + e.toString() );
                    }

                }

                if (bondList.size() <= 0){
                    rvBond.setVisibility(View.GONE);
                }
                Log.i(TAG, "onReceive: 解绑设备成功");
                break;
            case BluetoothDevice.BOND_BONDED:
                unbondListSize--;
                int position1 = -1;
                for (int j = 0; j < unbondList.size(); j++){
                    if (tmpDevice == bondList.get(j)){
                        position1 = j;
                    }
                }

                if (position1 != -1){
                    unbondList.remove(position1);
                    mUnbondAdapter.notifyItemRangeRemoved(position1, 1);
                }

                bondList.add(tmpDevice);
                mBondAdapter.notifyAll();
                rvBond.setVisibility(View.VISIBLE);
                Log.i(TAG, "onReceive: 绑定远程设备成功");
                break;
            case BluetoothDevice.BOND_BONDING:
                Log.i(TAG, "onReceive: 正在绑定远程设备");
                break;
            default:
                Log.i(TAG, "onReceive: 未处理的绑定事件代码：" + state);
                break;
        }

    }
}
*/
