package com.example.eileen.mysettings_fragment.storage;

import android.content.Context;
import android.os.IBinder;
import android.os.StatFs;
import android.os.storage.IMountService;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StorageUtil {

    private static final String TAG = "qll_storage_util";
    public static String[] getVolumePaths(Context context) {
        String paths[] = null;
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class
                    .getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            paths = (String[]) getVolumePathsMethod.invoke(storageManager, params);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return paths;
    }

    public static void unmountVolume(String path) {

        try {
            Method method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, "mount");
            IMountService service = IMountService.Stub.asInterface(binder);
            service.unmountVolume(path, true, true);
        } catch (Exception e) {
            Log.i(TAG, "unmountVolume: 卸载设备出错----" + e.toString());
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;

        return size;

    }

    public static long getTotalSize(String path) {
        StatFs statFs = new StatFs(path);

        return statFs.getTotalBytes();
    }

    public static long getAvailableSize(String path) {
        StatFs statFs = new StatFs(path);

        return statFs.getFreeBytes();
    }


}
