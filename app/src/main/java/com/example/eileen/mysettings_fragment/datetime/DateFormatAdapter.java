package com.example.eileen.mysettings_fragment.datetime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;

import com.example.eileen.mysettings_fragment.R;

import java.util.List;

public class DateFormatAdapter extends ArrayAdapter<DateFormat> {

    private static final String TAG = "qll_dt_fm_adapter";
    private int layoutId;


    public DateFormatAdapter(Context context, int layoutId, List<DateFormat> dataList){
        super(context, layoutId, dataList);
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        Log.i(TAG, "getView: 位置----" + pos + " && convertView----" + convertView
                + " && parent" + parent);

        DateFormat format = getItem(pos);
        View view;

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }else {
            view = convertView;
        }
        ImageView imgFormat = (ImageView) view.findViewById(R.id.dt_img_format);
        TextClock tcFormat = (TextClock) view.findViewById(R.id.dt_tc_format);

        imgFormat.setImageResource(format.getImgSrc());
        tcFormat.setFormat12Hour(format.getVisibleText());
        tcFormat.setFormat24Hour(format.getVisibleText());
        return view;
    }
}
