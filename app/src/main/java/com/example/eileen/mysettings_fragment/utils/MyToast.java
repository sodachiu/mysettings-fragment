package com.example.eileen.mysettings_fragment.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    public static void showToast(Context context, String toastInfo){
        Toast.makeText(context, toastInfo, Toast.LENGTH_SHORT).show();
    }
}
