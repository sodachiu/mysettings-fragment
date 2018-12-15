package com.android.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.settings.utils.FragmentUtil;

public class AdvancedFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "qll_advanced_fragment";


    private EditText etPassword;
    private Button btnConfirm;
    private TextView tvAlarm;

    public AdvancedFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_advanced, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle args) {
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(args);
        initView();
    }

    void initView(){
        FragmentActivity activity = getActivity();

        etPassword = (EditText) activity.findViewById(R.id.advanced_et_pwd);
        btnConfirm = (Button) activity.findViewById(R.id.advanced_btn_confirm);
        tvAlarm = (TextView) activity.findViewById(R.id.advanced_tv_alarm);

        btnConfirm.setOnClickListener(this);

        String preFragment = FragmentUtil.getPreviousFragment();
        if (preFragment.equals(FragmentUtil.ADVANCED_FRAGMENT_2)) {
            btnConfirm.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.advanced_btn_confirm:
                Log.i(TAG, "onClick: 用户点击了确定");
                checkPassword();
                break;
            default:
                Log.i(TAG, "onClick: 点击了其它控件----" + view.getId());
        }
    }

    void checkPassword() {


        Context context = getContext();
        String defPassword = context.getString(R.string.default_password);
        String inputPassword = etPassword.getText().toString();

        boolean isCorrect = defPassword.equals(inputPassword);

        if (isCorrect) {
            tvAlarm.setVisibility(View.INVISIBLE);
            FragmentUtil.showFragment(context, FragmentUtil.ADVANCED_FRAGMENT_2);
        } else {
            tvAlarm.setVisibility(View.VISIBLE);
        }

    }

}
