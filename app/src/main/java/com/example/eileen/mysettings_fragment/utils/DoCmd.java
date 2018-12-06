package com.example.eileen.mysettings_fragment.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoCmd {

    private static final String TAG = "qll_do_cmd";
    public static void doCmd(String cmd) {

        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dis = new DataInputStream(process.getInputStream());

            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();

            process.waitFor();

        } catch (Exception e) {
            Log.i(TAG, "doCmd: 执行命令出错----" + e.toString());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    Log.i(TAG, "doCmd: 关闭对象输出对象出错----" + e.toString());
                }
            }

        }
    }
}
