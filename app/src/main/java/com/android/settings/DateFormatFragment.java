package com.android.settings;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.settings.datetime.DateFormatAdapter;
import com.android.settings.datetime.DateFormat;
import com.android.settings.utils.MyHandler;
import java.util.ArrayList;

public class DateFormatFragment extends Fragment implements
        AdapterView.OnItemClickListener{
    private static final String TAG = "qll_dt_format_fragment";
    private Context mContext;
    private DateFormatAdapter mAdapter;
    private ArrayList<DateFormat> mFormatsList;
    private ListView lvDateFormats;
    private MyHandler mHandler;
    private static int mNowFormatPos = 0;
    public static final int DEFAULT_FORMAT_POSITION = 0; // 默认日期格式在arrays中的坐标

    public DateFormatFragment(){
        Log.i(TAG, "DateFormatFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mFormatsList = new ArrayList<>();
        mHandler = new MyHandler(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_date_format, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initFormats();
                initView();
            }
        }).start();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mFormatsList = null;
        mAdapter = null;
        lvDateFormats = null;
        mHandler.clear();
        mContext = null;
    }

    void initView(){
        Log.i(TAG, "initView: ");
        lvDateFormats = (ListView) getActivity().findViewById(R.id.dt_lv_date_formats);
        lvDateFormats.setAdapter(mAdapter);
        lvDateFormats.setSelection(mNowFormatPos);
        lvDateFormats.setOnItemClickListener(this);
    }

    /**
     * 初始化所有的日期格式
     * */
    void initFormats(){
        Log.i(TAG, "initFormats: ");

        String[] formats = getResources().getStringArray(R.array.date_formats);

        String nowFormat = Settings.System.getString(mContext.getContentResolver()
                , Settings.System.DATE_FORMAT);
        Log.i(TAG, "initFormats: 当前日期格式----" + nowFormat);

        for (int i = 0; i < formats.length; i++){
            Log.i(TAG, "initFormats: 当前处理第 " + i + " 个日期格式");
            DateFormat dateFormat;
            int imgSrc = R.drawable.checkbox_off;
            String visibleText;

            if (nowFormat.equals(formats[i])){
                imgSrc = R.drawable.checkbox_on;
                mNowFormatPos = i;
            }

            if (i == DEFAULT_FORMAT_POSITION) {
                visibleText = getResources().getString(R.string.dt_default_format);
            } else {
                visibleText = formats[i];
            }

            dateFormat = new DateFormat(formats[i], imgSrc, visibleText);
            mFormatsList.add(dateFormat);
            

        }
        Log.i(TAG, "initFormats: formatList 长度——" + mFormatsList.size());
        mAdapter = new DateFormatAdapter(mContext, R.layout.dt_format_item, mFormatsList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        DateFormat selectFormat = mFormatsList.get(position);
        String nowFormat = Settings.System.getString(mContext.getContentResolver()
                , Settings.System.DATE_FORMAT);
        if (nowFormat.equals(selectFormat.getDateFormat())) {
            mHandler.sendEmptyMessage(MyHandler.DT_FORMAT_ALREADY_USE);
            return;
        }else {
            for (DateFormat item : mFormatsList){
                item.setImgSrc(R.drawable.checkbox_off);
            }
        }

        selectFormat.setImgSrc(R.drawable.checkbox_on);
        String sFormat = selectFormat.getDateFormat();

        Settings.System.putString(mContext.getContentResolver(),
                Settings.System.DATE_FORMAT,
                sFormat);
        Log.i(TAG, "onItemClick: 设置日期格式为----" + selectFormat.getImgSrc());
        mAdapter.notifyDataSetChanged();

    }

}
