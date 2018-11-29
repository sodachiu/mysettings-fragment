package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.eileen.mysettings_fragment.datetime.DateFormatAdapter;

import com.example.eileen.mysettings_fragment.datetime.DateFormat;
import java.util.ArrayList;

public class DateFormatFragment extends Fragment {
    private static final String TAG = "qll_dt_format_fragment";
    private Context mContext;
    private DateFormatAdapter mAdapter;
    private ArrayList<DateFormat> mFormatsList;
    private RecyclerView rvDateFormats;
    private static int mNowFormatPos = 0;

    public DateFormatFragment(){
        Log.i(TAG, "DateFormatFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mFormatsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_date_format, container, false);
        initFormats();
        return view;
    }

    /*@Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        initFormats();
        initView();
    }*/

    @Override
    public void onResume(){
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();
    }

    void initView(){
        Log.i(TAG, "initView: ");
        rvDateFormats = (RecyclerView) getActivity().findViewById(R.id.dt_rv_date_formats);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvDateFormats.setLayoutManager(llManager);
        rvDateFormats.setAdapter(mAdapter);
        rvDateFormats.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: test");
                rvDateFormats.scrollToPosition(mNowFormatPos);
            }
        });
        Log.i(TAG, "initView: end");

    }

    void initFormats(){
        Log.i(TAG, "initFormats: ");
        String[] formats = getResources().getStringArray(R.array.date_formats);
        String nowFormat = Settings.System.getString(mContext.getContentResolver()
                , Settings.System.DATE_FORMAT);

        if (nowFormat == null){
            nowFormat = formats[0];
        }

        for (int i = 0; i < formats.length; i++){
            Log.i(TAG, "initFormats: 当前处理第 " + i + " 个日期格式");
            DateFormat dateFormat;
            int imgSrc = R.drawable.checkbox_off;

            if (nowFormat.equals(formats[i])){
                imgSrc = R.drawable.checkbox_on;
                mNowFormatPos = i;
            }
            dateFormat = new DateFormat(formats[i], imgSrc);
            mFormatsList.add(dateFormat);

        }
        Log.i(TAG, "initFormats: formatList 长度——" + mFormatsList.size());
        mAdapter = new DateFormatAdapter(mFormatsList);

    }
}
