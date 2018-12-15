package com.android.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.settings.utils.FragmentUtil;
import com.android.settings.utils.ShowDialog;
import com.android.settings.utils.UniqueMark;

import java.io.IOException;

public class ResetFragment extends Fragment {
    private static final String TAG = "qll_recovery_fragment";
    private Context mContext;
    private EditText etPassword;
    private TextView tvAlarm;
    private Button btnResetBox;

    public ResetFragment(){

    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: 这里是恢复出厂设置的fragment啊");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_reset, parent, false);
        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != UniqueMark.RESET_FRAGMENT) {
            Log.i(TAG, "onActivityResult: 不是正确的请求码");
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult: ok");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RecoverySystem.rebootWipeUserData(mContext);

                    } catch (IOException e) {
                        Log.i(TAG, "onActivityResult: 重置机顶盒失败----" + e.toString());
                    }
                }
            }).start();

        }

    }

    void initView() {
        FragmentActivity activity = getActivity();
        etPassword = (EditText) activity.findViewById(R.id.reset_et_pwd);
        tvAlarm = (TextView) activity.findViewById(R.id.reset_tv_alarm);
        btnResetBox = (Button) activity.findViewById(R.id.reset_btn_confirm);

        btnResetBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    void checkPassword() {


        Context context = getContext();
        String defPassword = context.getString(R.string.default_password);
        String inputPassword = etPassword.getText().toString();

        boolean isCorrect = defPassword.equals(inputPassword);

        if (isCorrect) {
            tvAlarm.setVisibility(View.INVISIBLE);
            Fragment targetFragment = FragmentUtil.getCurrentFragment(mContext);
            int requestCode = UniqueMark.RESET_FRAGMENT;
            ShowDialog.showDialog(null, targetFragment, requestCode);
        } else {
            tvAlarm.setVisibility(View.VISIBLE);
        }

    }
}
