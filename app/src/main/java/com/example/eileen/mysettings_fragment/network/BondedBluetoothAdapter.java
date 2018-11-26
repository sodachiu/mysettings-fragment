package com.example.eileen.mysettings_fragment.network;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;
import com.example.eileen.mysettings_fragment.utils.FragmentUtil;
import com.example.eileen.mysettings_fragment.utils.MyDialogFragment;
import com.example.eileen.mysettings_fragment.utils.UniqueMark;

import java.util.List;

public class BondedBluetoothAdapter extends RecyclerView.Adapter<BondedBluetoothAdapter.ViewHolder>{

    private static final String TAG = "qll_blue_adapter";
    private List<BluetoothDevice> mDevicesList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDevInfo;
        LinearLayout llDevice;

        public ViewHolder(View view){
            super(view);
            Log.i(TAG, "ViewHolder: ");
            tvDevInfo = (TextView) view.findViewById(R.id.bluetooth_tv_item_info);
            llDevice = (LinearLayout) view.findViewById(R.id.bluetooth_ll_item);
        }

    }

    public BondedBluetoothAdapter(List<BluetoothDevice> devicesList){
        Log.i(TAG, "UnbondBluetoothAdapter: ");
        mDevicesList = devicesList;
    }

    @NonNull
    @Override
    public BondedBluetoothAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.i(TAG, "onCreateViewHolder: ");
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.bluetooth_bonded_item,
                viewGroup, false);
        final BondedBluetoothAdapter.ViewHolder holder = new BondedBluetoothAdapter.ViewHolder(view);

        holder.llDevice.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BluetoothDevice clickDevice = mDevicesList.get(position);
                int bondState = clickDevice.getBondState();
                Log.i(TAG, "onClick: 被点击设备位置：" + position + "&&绑定状态：" + bondState);
                unbondDevice(clickDevice);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(BondedBluetoothAdapter.ViewHolder viewHolder, int i) {
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
        Log.i(TAG, "unbondDevice: ");
        Bundle bundle = new Bundle();
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        String[] promptInfo = new String[1];
        FragmentActivity activity = (FragmentActivity) mContext;

        promptInfo[0] = activity.getResources().getString(R.string.dialog_content_unbond);
        String deviceInfo;
        if (device.getName() == null || device.getName().equals("")){
            deviceInfo = device.getAddress();
        }else {
            deviceInfo = device.getName();
        }
        promptInfo[0] = String.format(promptInfo[0], deviceInfo);

        bundle.putStringArray("prompt_info", promptInfo);
        bundle.putParcelable("bonded_device", device);
        myDialogFragment.setArguments(bundle);
        myDialogFragment.setTargetFragment(FragmentUtil.getCurrentFragment(mContext), UniqueMark.BLUETOOTH_BOND);
        myDialogFragment.show(activity.getSupportFragmentManager(), "unbond_dialog");

    }

}
