package com.example.eileen.mysettings_fragment.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.eileen.mysettings_fragment.DisplayResolutionFragment;
import com.example.eileen.mysettings_fragment.R;
import com.example.eileen.mysettings_fragment.display.ResolutionObject;

public class ShowDialog {

    private static final String TAG = "qll_show_dialog";
    private static Parcelable mParcelable;
    private static Fragment mTargetFragment;
    private static int mRequestCode;
    private static String mTitle;
    private static String[] mainPromptTexts;
    private static int mTimeSecond;
    private static String mTimeText;
    private static String mDialogTag;

    public static void showDialog(Parcelable parcelable
            , Fragment targetFragment, int requestCode) {

        if (targetFragment == null) {
            Log.i(TAG, "showDialog: 没有目标 Fragment");
            return;
        }

        String fragmentTag = targetFragment.getTag();
        if (fragmentTag == null || fragmentTag.equals("")) {
            Log.i(TAG, "showDialog: 目标 fragment 没有 tag");
            return;
        }

        Context context = targetFragment.getContext();
        if (context == null) {
            Log.i(TAG, "showDialog: context 为 null");
            return;
        }
        mParcelable = parcelable;
        mTargetFragment = targetFragment;
        mRequestCode = requestCode;

        switch (requestCode) {
            case UniqueMark.BLUETOOTH_ATTEMPT_TO_UNBOND:
                initAttemptToUnbondBluetoothDialog(context);
                break;
            case UniqueMark.BLUETOOTH_ATTEMPT_TO_BOND:
                initAttemptToBondBluetoothDialogInfo(context);
                break;
            case UniqueMark.RESOLUTION_FRAGMENT:
                initResolutionDialogInfo(context);
                break;
            default:
                Log.i(TAG, "showDialog: requestCode 不在列表中");
                return;
        }

        MyDialog dialog = MyDialog.getInstance(mTitle, mainPromptTexts, mTimeText, mTimeSecond, mParcelable);
        dialog.setTargetFragment(mTargetFragment, mRequestCode);
        FragmentActivity activity = (FragmentActivity) context;
        dialog.show(activity.getSupportFragmentManager(), mDialogTag);

        destroyData();


    }

    private static void destroyData() {
        Log.i(TAG, "destroyData: ");
        mParcelable = null;
        mainPromptTexts = null;
        mRequestCode = -1;
        mTargetFragment = null;
        mDialogTag = null;
        mTimeSecond = -1;
        mTimeText = null;
        mTitle = null;

    }

    /**
     * 用于ResolutionDialog的显示信息初始化
     * */
    private static void initResolutionDialogInfo(Context context) {

        String info0 = context.getString(R.string.display_first_confirm_text1);
        mainPromptTexts = new String[1];
        mainPromptTexts[0] = info0;
        mTimeText = context.getString(R.string.display_timer_text);
        mTitle = context.getString(R.string.display_first_confirm_title);
        mTimeSecond = DisplayResolutionFragment.COUNT_TIMER_TOTAL_SECOND;
        mDialogTag = "resolution_dialog";

    }

    /**
     * 显示尝试连接蓝牙设备的DialogFragment
     * */
    private static void initAttemptToBondBluetoothDialogInfo(Context context) {



    }

    /**
     * 初始化尝试解绑蓝牙设备的 DialogFragment 的提示信息
     * */
    private static void initAttemptToUnbondBluetoothDialog(Context context) {

        BluetoothDevice device = (BluetoothDevice) mParcelable;

        String deviceName;
        if (device.getName() == null || device.getName().equals("")){
            deviceName = device.getAddress();
        }else {
            deviceName = device.getName();
        }

        mTitle = deviceName;
        String info0 = context.getString(R.string.dialog_content_unbond);
        info0 = String.format(info0, deviceName);
        mainPromptTexts = new String[1];
        mainPromptTexts[0] = info0;
        mDialogTag = "bluetooth_unbond_dialog";
        mTimeSecond = -1;
        mTimeText = "";

    }
}
