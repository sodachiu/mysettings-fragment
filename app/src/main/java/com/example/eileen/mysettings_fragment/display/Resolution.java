package com.example.eileen.mysettings_fragment.display;

import android.util.Log;

public class Resolution {
    private static final String TAG = "Resolution";
    private int imgSrc;
    private String resolutionText;
    private int index;

    public Resolution(int imgSrc, String resolutionText, int index){
        Log.i(TAG, "Resolution: ");
        this.imgSrc = imgSrc;
        this.resolutionText = resolutionText;
        this.index = index;
    }

    public int getImgSrc() {
        Log.i(TAG, "getImgSrc: ");
        return imgSrc;
    }

    public String getResolutionText(){
        Log.i(TAG, "getResolutionText: ");
        return resolutionText;
    }

    public void setImgSrc(int imgSrc) {
        Log.i(TAG, "setImgSrc: ");
        this.imgSrc = imgSrc;
    }

    public int getIndex() {
        Log.i(TAG, "getIndex: ");
        return index;
        
    }
}
