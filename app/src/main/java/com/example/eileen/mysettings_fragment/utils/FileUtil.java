package com.example.eileen.mysettings_fragment.utils;

import android.util.Log;
import android.widget.EditText;

import java.io.File;

public class FileUtil {

    private static final String TAG = "qll_file_util";

    public static long getFolderSize(File file) {

        long size = 0;
        File[] files = file.listFiles();
        for (File item : files) {
            if (item.isFile()) {
                size += item.length();
            } else if (item.isDirectory()) {
                size += getFolderSize(item);
            }

        }

        Log.i(TAG, "getFolderSize: file----" + file.toString() + " && size----" + size);
        return size;

    }

    public static void removeFile(File file) {
        Log.i(TAG, "removeFile: file----" + file.toString());

        File[] files = file.listFiles();
        try {
            for (File item : files) {
                if (item.isDirectory()) {
                    removeFile(item);
                }
                item.delete();
                /*if (item.isFile()) {
                    item.delete();
                } else if (item.isDirectory()) {
                    removeFile(item);
                    item.delete();
                }*/
            }
        } catch (Exception e) {
            Log.i(TAG, "removeFile: 删除文件出错----" + e.toString());
        }

    }
}
