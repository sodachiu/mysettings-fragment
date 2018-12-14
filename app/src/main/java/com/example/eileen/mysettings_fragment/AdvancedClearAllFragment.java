package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.storage.StorageUtil;
import com.example.eileen.mysettings_fragment.utils.FileUtil;

import java.io.File;

public class AdvancedClearAllFragment extends Fragment {

    private static final String TAG = "qll_clear_all";

    private Context mContext;
    private TextView tvCacheSize;
    private Button btnClear;
    private File internalCache, externalCache;

    public AdvancedClearAllFragment() {

    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        internalCache = mContext.getCacheDir();
        externalCache = mContext.getExternalCacheDir();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {

        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_ad_clear_all, parent, false);

        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();
    }

    void initView() {
        FragmentActivity activity = getActivity();
        tvCacheSize = (TextView) activity.findViewById(R.id.advanced_tv_cache_size);
        btnClear = (Button) activity.findViewById(R.id.advanced_btn_clear);
        btnClear.requestFocus();

        setCacheText();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.removeFile(internalCache);
                if (externalCache != null) {
                    FileUtil.removeFile(externalCache);
                }

                setCacheText();
            }
        });

    }

    String getCacheSize() {

        long cacheSize = FileUtil.getFolderSize(internalCache);
        if (externalCache != null) {
            cacheSize += FileUtil.getFolderSize(externalCache);
        }
        return Formatter.formatFileSize(mContext, cacheSize);
    }

    void setCacheText() {
        String cacheText = getString(R.string.advanced_cache_size);
        cacheText = String.format(cacheText, getCacheSize());
        tvCacheSize.setText(cacheText);
    }

}
