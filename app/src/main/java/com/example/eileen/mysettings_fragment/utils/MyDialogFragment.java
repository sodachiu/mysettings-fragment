package com.example.eileen.mysettings_fragment.utils;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;

public class MyDialogFragment extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "qll_dialog_fragment";
    private Context mContext;
    private Dialog dialog;
    private Fragment targetFragment;
    private BluetoothDevice mBluetoothDevice;
    private Bundle mBundle;
    private Intent resultIntent = new Intent();

    public MyDialogFragment(){
        Log.i(TAG, "MyDialogFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog: ");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        Button btnConfirm = (Button) view.findViewById(R.id.dialog_btn_confirm);
        Button btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.dialog_ll_content);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        dialog = new Dialog(mContext, R.style.dialog_container_style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 600;
        lp.height = 400;
        dialogWindow.setAttributes(lp);

        targetFragment = getTargetFragment();
        if (targetFragment == null){
            Log.e(TAG, "onCreateDialog: 宿主 fragment 为空");
            return dialog;
        }
        mBundle = getArguments();

        if (mBundle != null && mBundle.getStringArray("prompt_info") != null){

            String[] displayInfos = mBundle.getStringArray("prompt_info");
            for (String item : displayInfos){
                TextView tv = new TextView(mContext);
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setTextSize(23);
                tv.setText(item);
                Log.i(TAG, "onCreateDialog: 当前显示信息为：" + item);
                llContent.addView(tv);
            }

        }
        return dialog;
    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.dialog_btn_confirm:
                Log.i(TAG, "onClick: 点击确定，确定解绑");
                handleConfirmEvent();
                break;
            case R.id.dialog_btn_cancel:
                Log.i(TAG, "onClick: 点击取消，确定");
                handleCancelEvent();
                break;
            default:
                break;

        }
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }


    /**
     * 处理用户的确定点击事件
     * */
    void handleConfirmEvent(){
        Log.i(TAG, "handleBundle: ");
        int requestCode = getTargetRequestCode();
        switch (requestCode){
            case UniqueMark.BLUETOOTH_BOND:
                mBluetoothDevice = (BluetoothDevice) mBundle.getParcelable("bonded_device");
                resultIntent.putExtra("bonded_device", mBluetoothDevice);
                targetFragment.onActivityResult(UniqueMark.BLUETOOTH_BOND,
                        Activity.RESULT_OK,
                        resultIntent);
                break;
            case UniqueMark.BLUETOOTH_UNBOND:
                mBluetoothDevice = (BluetoothDevice) mBundle.getParcelable("unbond_device");
                resultIntent.putExtra("unbond_device", mBluetoothDevice);
                targetFragment.onActivityResult(UniqueMark.BLUETOOTH_UNBOND,
                        Activity.RESULT_OK,
                        resultIntent);
                break;

        }
        dialog.dismiss();

    }

    /**
     * 处理用户的取消点击事件
     * */

    void handleCancelEvent(){
        Log.i(TAG, "handleCancelEvent: ");
        int requestCode = getTargetRequestCode();
        switch (requestCode){
            case UniqueMark.BLUETOOTH_BOND:
                targetFragment.onActivityResult(UniqueMark.BLUETOOTH_BOND
                        , Activity.RESULT_CANCELED
                        , null);
                break;
            case UniqueMark.BLUETOOTH_UNBOND:
                targetFragment.onActivityResult(UniqueMark.BLUETOOTH_UNBOND
                        , Activity.RESULT_CANCELED
                        , null);
                break;
        }

        dialog.dismiss();

    }
}
