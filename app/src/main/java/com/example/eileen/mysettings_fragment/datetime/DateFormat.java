package com.example.eileen.mysettings_fragment.datetime;

import android.util.Log;

public class DateFormat {
    private static final String TAG = "qll_date_format";
    private String dateFormat;
    private int imgSrc;

    public DateFormat(String dateFormat, int imgSrc){
        Log.i(TAG, "DateFormat: ");
        this.dateFormat = dateFormat;
        this.imgSrc = imgSrc;
    }


    public int getImgSrc() {
        Log.i(TAG, "getImgSrc: ");
        return imgSrc;
    }

    /*
    * 获取默认日期格式 yyyy-M-dd
    * */
    public String getRegionFormat() {
        Log.i(TAG, "getDateFormat: ");
        int beginIndex = dateFormat.indexOf("y");
        int lastIndex = dateFormat.indexOf(")");
        String format = dateFormat.substring(beginIndex, lastIndex);
        Log.i(TAG, "getDateFormat: 需要的日期格式为----" + format);
        return format;
    }

    /*
    * 获取提供的其它日期格式
    * */
    public String getOriginFormat() {
        Log.i(TAG, "getOriginFormat: ");
        return dateFormat;
    }


    /*
    * 用于当用户更改日期格式时，动态更新对应的IMG为打开状态
    * */
    public void setImgSrc(int imgSrc){
        this.imgSrc = imgSrc;
    }
}
