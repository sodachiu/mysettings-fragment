package com.android.settings.datetime;

import android.util.Log;

public class DateFormat {
    private static final String TAG = "qll_date_format";
    private String text;
    private int imgSrc;
    private String dateFormat;

    public DateFormat(String dateFormat, int imgSrc, String text){
        Log.i(TAG, "DateFormat: ");
        this.dateFormat = dateFormat;
        this.imgSrc = imgSrc;
        this.text = text;
    }


    public int getImgSrc() {
        Log.i(TAG, "getImgSrc: ");
        return imgSrc;
    }

    /**
     * 获取显示的日期格式
     * */
    public String getVisibleText() {
        return text;
    }

    /**
     * 获取需要设置的日期格式
     * */
    public String getDateFormat() {
        return dateFormat;
    }

    /*
    * 用于当用户更改日期格式时，动态更新对应的IMG为打开状态
    * */
    public void setImgSrc(int imgSrc){
        this.imgSrc = imgSrc;
    }
}
