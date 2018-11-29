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

    public String getDateFormat() {
        Log.i(TAG, "getDateFormat: ");
        return dateFormat;
    }

    public void setDateFormat(String dateFormat){
        Log.i(TAG, "setDateFormat: ");
        this.dateFormat = dateFormat;
    }

    public void setImgSrc(int imgSrc){
        this.imgSrc = imgSrc;
    }
}
