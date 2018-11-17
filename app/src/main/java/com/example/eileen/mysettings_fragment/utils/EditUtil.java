package com.example.eileen.mysettings_fragment.utils;

import android.widget.EditText;

public class EditUtil {

    /*
    * 检查EditView里的值是否为空
    * */
    public static boolean checkEditEmpty(EditText editText){
        String inputValue = editText.getText().toString().trim();
        return inputValue.equals("");
    }
}
