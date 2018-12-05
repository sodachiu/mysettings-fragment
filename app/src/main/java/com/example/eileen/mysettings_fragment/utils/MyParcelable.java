package com.example.eileen.mysettings_fragment.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
    private int oldStandard;


    public int getOldStandard() {
        return oldStandard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        oldStandard = dest.readInt();
        // 继续添加
    }

    /**
     * 给Resolution留的接口
     * */
    public MyParcelable(int oldStandard) {
        this.oldStandard = oldStandard;
    }



    private MyParcelable(Parcel in) {
        oldStandard = in.readInt();
        // 继续添加
    }

    public static final Creator<MyParcelable> CREATOR = new Creator<MyParcelable>() {
        @Override
        public MyParcelable createFromParcel(Parcel source) {
            return new MyParcelable(source);
        }

        @Override
        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

}
