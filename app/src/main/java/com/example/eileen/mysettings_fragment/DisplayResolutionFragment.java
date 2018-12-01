package com.example.eileen.mysettings_fragment;

import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class DisplayResolutionFragment extends Fragment {
    private static final String TAG = "qll_resolution_fragment";
    private Context mContext;
    private RecyclerView rvResolution;
    private List<Resolution> mResolutionsList;
    private ResolutionAdapter mAdapter;
    private DisplayManager mDisplayManager;
    // true为自适应，false为用户选择分辨率，恢复出厂时需要将其置为true，用户设置时需要将其置为false
    public static boolean isAdaptiveResolution = true;

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
        initResolution();
        initView();
    }

    void initView(){
        Log.i(TAG, "initView: ");
        FragmentActivity activity = getActivity();
        rvResolution = (RecyclerView) activity.findViewById(R.id.resolution_rv);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvResolution.setLayoutManager(llManager);
        rvResolution.setAdapter(mAdapter);

    }

    /**
     * 初始化电视支持的所有分辨率制式
     * */
    void initResolution(){
        int[] resolutionIndex = getResources().getIntArray(R.array.resolutions_index);
        String[] resolutionText = getResources().getStringArray(R.array.resolutions_text);
        int[] usableResolution = mDisplayManager.getAllSupportStandards();

        int imgSrc = R.drawable.radio_checked_normal;
        int nowResolution = mDisplayManager.getCurrentStandard();

        Resolution adjust = new Resolution(imgSrc, resolutionText[0], resolutionIndex[0]);
        mResolutionsList.add(adjust);
        for (int i = 0; i < resolutionIndex.length; i++){
            // 在列表大全里找到对应的index，保留i值，用于取text
            for (int j = 0; j < usableResolution.length; j++){
                if (usableResolution[j] == resolutionIndex[i]){
                    Resolution resolution = new Resolution(imgSrc, resolutionText[i], resolutionIndex[i]);
                    mResolutionsList.add(resolution);
                    break;
                }
            }
        }

        mAdapter = new ResolutionAdapter(mResolutionsList);
    }
}
