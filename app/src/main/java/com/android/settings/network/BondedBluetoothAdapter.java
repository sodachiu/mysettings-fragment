package com.android.settings.network;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.settings.R;
import com.android.settings.utils.FragmentUtil;
import com.android.settings.utils.MyDialog;
import com.android.settings.utils.ShowDialog;
import com.android.settings.utils.UniqueMark;

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
                showDialog(clickDevice);

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
        return mDevicesList.size();
    }

    void showDialog(BluetoothDevice device){
        Log.i(TAG, "showDialog: ");
        Fragment targetFragment = FragmentUtil.getCurrentFragment(mContext);
        ShowDialog.showDialog(device, targetFragment, UniqueMark.BLUETOOTH_ATTEMPT_TO_UNBOND);
    }

}
