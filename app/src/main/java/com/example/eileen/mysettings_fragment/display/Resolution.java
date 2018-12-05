package com.example.eileen.mysettings_fragment.display;

import android.util.Log;

import com.example.eileen.mysettings_fragment.R;

public class Resolution {
    private static final String TAG = "Resolution";
    private boolean isChecked;
    private String resolutionText;
    private int standard;

    public Resolution(boolean isChecked, String resolutionText, int standard){
        Log.i(TAG, "Resolution: ");
        this.isChecked = isChecked;
        this.resolutionText = resolutionText;
        this.standard = standard;
    }

    public int getImgSrc() {
        Log.i(TAG, "getImgSrc: ");
        if (isChecked){
            return R.drawable.radio_checked_normal;
        }else {
            return R.drawable.radio_unchecked_normal;
        }
    }

    public String getResolutionText(){
        Log.i(TAG, "getResolutionText: ");
        return resolutionText;
    }

    public int getStandard() {
        Log.i(TAG, "getIndex: ");
        return standard;
        
    }
}
