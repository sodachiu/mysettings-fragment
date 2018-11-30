package com.example.eileen.mysettings_fragment;

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
import com.example.eileen.mysettings_fragment.datetime.DateFormatAdapter;
import com.example.eileen.mysettings_fragment.datetime.DateFormat;
import com.example.eileen.mysettings_fragment.utils.MyHandler;
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
        initFormats();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        initView();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mFormatsList = null;
        mAdapter = null;
        lvDateFormats = null;
        mHandler = null;
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

        String defaultFormat = formats[DEFAULT_FORMAT_POSITION];
        int beginIndex = defaultFormat.indexOf("y");
        int lastIndex = defaultFormat.indexOf(")");
        defaultFormat = defaultFormat.substring(beginIndex, lastIndex);

        Log.i(TAG, "initFormats: beginIndex----" + beginIndex
                + " && lastIndex----" + lastIndex
                + " && defaultFormat----" + defaultFormat);

        if (nowFormat == null){
            nowFormat = defaultFormat;
        }

        Log.i(TAG, "initFormats: 当前的日期格式为----" + nowFormat);

        for (int i = 0; i < formats.length; i++){
            Log.i(TAG, "initFormats: 当前处理第 " + i + " 个日期格式");
            DateFormat dateFormat;
            int imgSrc = R.drawable.checkbox_off;

            if(i == DEFAULT_FORMAT_POSITION && nowFormat.equals(defaultFormat)){
                imgSrc = R.drawable.checkbox_on;
            }else if (nowFormat.equals(formats[i])){
                imgSrc = R.drawable.checkbox_on;
                mNowFormatPos = i;
            }

            dateFormat = new DateFormat(formats[i], imgSrc);
            mFormatsList.add(dateFormat);
            

        }
        Log.i(TAG, "initFormats: formatList 长度——" + mFormatsList.size());
        mAdapter = new DateFormatAdapter(mContext, R.layout.dt_format_item, mFormatsList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.i(TAG, "onItemClick: parent----" + parent
                + " \n&& view----" + view
                + " \n&& id----" + id);
        DateFormat selectFormat = mFormatsList.get(position);

        int imgOff = R.drawable.checkbox_off;
        int imgOn = R.drawable.checkbox_on;
        if (selectFormat.getImgSrc() == imgOn){
            Log.i(TAG, "onItemClick: 已使用当前日期格式");
            mHandler.sendEmptyMessage(MyHandler.DT_FORMAT_ALREADY_USE);
            return;
        }else {
            for (DateFormat item : mFormatsList){
                item.setImgSrc(imgOff);
            }
        }

        selectFormat.setImgSrc(imgOn);
        String sFormat = selectFormat.getOriginFormat();

        if (position == DEFAULT_FORMAT_POSITION){
            sFormat = selectFormat.getRegionFormat();
        }
        Settings.System.putString(mContext.getContentResolver(),
                Settings.System.DATE_FORMAT,
                sFormat);
        Log.i(TAG, "onItemClick: 设置日期格式为----" + selectFormat.getImgSrc());
        mAdapter.notifyDataSetChanged();

    }

}
