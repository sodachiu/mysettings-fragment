package com.example.eileen.mysettings_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.display.DisplayManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eileen.mysettings_fragment.display.Resolution;
import com.example.eileen.mysettings_fragment.display.ResolutionAdapter;
import com.example.eileen.mysettings_fragment.display.ResolutionObject;
import com.example.eileen.mysettings_fragment.utils.MyDialog;
import com.example.eileen.mysettings_fragment.utils.MyParcelable;
import com.example.eileen.mysettings_fragment.utils.UniqueMark;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayResolutionFragment extends Fragment {
    private static final String TAG = "qll_resolution_fragment";
    private Context mContext;
    private RecyclerView rvResolution;
    private List<Resolution> mResolutionsList;
    private ResolutionAdapter mAdapter;
    private DisplayManager mDisplayManager;
    private SharedPreferences mSharedPreferences;
    private int[] mSupportStandards;
    // true为自适应，false为用户选择分辨率，恢复出厂时需要将其置为true，用户设置时需要将其置为false
    public static boolean isAdaptiveResolution = true;
    public static final String ADAPT_RESOLUTION = "adapt_resolution";
    public static final int COUNT_TIMER_TOTAL_SECOND = 20;
    public static int nowResolutionPosition = 0;

    public DisplayResolutionFragment(){
        Log.i(TAG, "DisplayResolutionFragment: ");
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach: ");
        super.onAttach(context);
        mContext = context;
        mResolutionsList = new ArrayList<>();
        mDisplayManager = (DisplayManager) mContext.getSystemService(
                Context.DISPLAY_MANAGER_SERVICE);
        mSupportStandards = mDisplayManager.getAllSupportStandards();
        mSharedPreferences = mContext.getSharedPreferences("qll_data", Context.MODE_PRIVATE);
        isAdaptiveResolution = mSharedPreferences.getBoolean(ADAPT_RESOLUTION, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedState){

        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_display_resolution, parent, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        Log.i(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedState);
        initView();
    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause: ");
        super.onPause();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ADAPT_RESOLUTION, isAdaptiveResolution);
        editor.apply();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mSupportStandards = null;
        mDisplayManager = null;
        mResolutionsList = null;
        mSharedPreferences = null;
        mAdapter = null;
        mContext = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != UniqueMark.RESOLUTION_FRAGMENT) {
            return;
        }
        Log.i(TAG, "onActivityResult: ");

        MyParcelable myParcelable = null;
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult: 用户取消操作");
            myParcelable = (MyParcelable) data.getParcelableExtra(MyDialog.PARCELABLE);
        }

        if (myParcelable != null) {
            int standard = myParcelable.getOldStandard();
            mDisplayManager.setDisplayStandard(standard);
        }

        initView();

    }

    void initView(){
        initResolution();
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        rvResolution = (RecyclerView) activity.findViewById(R.id.resolution_rv);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvResolution.setLayoutManager(llManager);
        rvResolution.setAdapter(mAdapter);

        rvResolution.post(new Runnable() {
            @Override
            public void run() {
                rvResolution.setSelected(true);
                rvResolution.smoothScrollToPosition(nowResolutionPosition);
            }
        });

    }

    /**
     * 初始化所有分辨率
     * */
    void initResolution(){
        Log.i(TAG, "initResolution: ");

        // 设置自适应的数据，如果有 1080P 50，则自适应到 1080P 50， 如果没有，自适应最高分辨率
        int adaptStandard = -1;
        mResolutionsList.clear();
        String adaptText = getString(R.string.display_adapt);
        for (int i = 0; i < mSupportStandards.length; i++){
            if (mSupportStandards[i] == DisplayManager.DISPLAY_STANDARD_1080P_50){
                adaptStandard = mSupportStandards[i];
                break;
            }
        }

        if (adaptStandard == -1){
            adaptStandard = mSupportStandards[0];
        }

        Resolution adaptResolution = new Resolution(isAdaptiveResolution, adaptText, adaptStandard);
        mResolutionsList.add(adaptResolution);

        initSupportStandards();

        mAdapter = new ResolutionAdapter(mResolutionsList);

    }

    /**
     * 初始化所有支持的分辨率，switch-case 语句效率高于 if-else 语句
     * */
    void initSupportStandards(){

        Log.i(TAG, "initSupportStandards: ");
        int currentStandard = mDisplayManager.getCurrentStandard();

        for (int standard : mSupportStandards){
            String standardText = "";
            Resolution resolution;
            boolean isChecked = false;
            if (!isAdaptiveResolution && currentStandard == standard){
                isChecked = true;
            }

            switch (standard){
                case DisplayManager.DISPLAY_STANDARD_1080P_60:
                    standardText = getResources().getString(R.string.display_1080p_60);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_50:
                    standardText = getResources().getString(R.string.display_1080p_50);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_30:
                    standardText = getResources().getString(R.string.display_1080p_30);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_25:
                    standardText = getResources().getString(R.string.display_1080p_25);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_24:
                    standardText = getResources().getString(R.string.display_1080p_24);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080I_60:
                    standardText = getResources().getString(R.string.display_1080i_60);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080I_50:
                    standardText = getResources().getString(R.string.display_1080i_50);
                    break;
                case DisplayManager.DISPLAY_STANDARD_720P_60:
                    standardText = getResources().getString(R.string.display_720p_60);
                    break;
                case DisplayManager.DISPLAY_STANDARD_720P_50:
                    standardText = getResources().getString(R.string.display_720p_50);
                    break;
                case DisplayManager.DISPLAY_STANDARD_576P_50:
                    standardText = getResources().getString(R.string.display_576p_50);
                    break;
                case DisplayManager.DISPLAY_STANDARD_480P_60:
                    standardText = getResources().getString(R.string.display_480p_60);
                    break;
                case DisplayManager.DISPLAY_STANDARD_PAL:
                    standardText = getResources().getString(R.string.display_pal);
                    break;
                case DisplayManager.DISPLAY_STANDARD_NTSC:
                    standardText = getResources().getString(R.string.display_ntsc);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_24:
                    standardText = getResources().getString(R.string.display_3840x2160p_24);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_25:
                    standardText = getResources().getString(R.string.display_3840x2160p_25);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_30:
                    standardText = getResources().getString(R.string.display_3840x2160p_30);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_60:
                    standardText = getResources().getString(R.string.display_3840x2160p_60);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_24:
                    standardText = getResources().getString(R.string.display_4096x2160p_24);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_25:
                    standardText = getResources().getString(R.string.display_4096x2160p_25);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_30:
                    standardText = getResources().getString(R.string.display_4096x2160p_30);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_60:
                    standardText = getResources().getString(R.string.display_4096x2160p_60);
                    break;
                default:
                    Log.i(TAG, "initSupportStandards: 未知制式----" + standard);
            }

            resolution = new Resolution(isChecked, standardText, standard);
            mResolutionsList.add(resolution);
        }


    }
}
