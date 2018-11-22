package com.example.eileen.mysettings_fragment.network;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;

import java.util.List;

public class MyBluetoothAdapter extends RecyclerView.Adapter<MyBluetoothAdapter.ViewHolder> {
    private static final String TAG = "qll_blue_adapter";
    private List<BluetoothDevice> mDevicesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDevInfo;

        public ViewHolder(View view){
            super(view);
            Log.i(TAG, "ViewHolder: ");
            tvDevInfo = (TextView) view.findViewById(R.id.bluetooth_tv_item_info);
        }

    }

    public MyBluetoothAdapter(List<BluetoothDevice> devicesList){
        Log.i(TAG, "MyBluetoothAdapter: ");
        mDevicesList = devicesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bluetooth_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.tvDevInfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BluetoothDevice clickDevice = mDevicesList.get(position);
                int bondState = clickDevice.getBondState();
                if (bondState == BluetoothDevice.BOND_BONDED){
                    unbondDevice(clickDevice);
                }else if (bondState == BluetoothDevice.BOND_NONE){
                    bondingDevice(clickDevice);
                }else {
                    Log.i(TAG, "onClick: 蓝牙设备连接状态代码：" + bondState);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.i(TAG, "onBindViewHolder: ");
        BluetoothDevice remoteDevice = mDevicesList.get(i);
        String devInfo;
        if (remoteDevice.getName() != null && !remoteDevice.getName().equals("")){
            devInfo = remoteDevice.getName();
        }else {
            devInfo = remoteDevice.getAddress();
        }
        viewHolder.tvDevInfo.setText(devInfo);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return mDevicesList.size();
    }

    void unbondDevice(BluetoothDevice device){
        device.removeBond();
    }

    void bondingDevice(BluetoothDevice device){
        device.createBond();
    }
}
