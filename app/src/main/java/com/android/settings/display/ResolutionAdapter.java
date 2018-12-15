package com.android.settings.display;

import android.content.Context;
import android.os.Bundle;
import android.os.display.DisplayManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.settings.DisplayResolutionFragment;
import com.android.settings.R;
import com.android.settings.utils.FragmentUtil;
import com.android.settings.utils.MyDialog;
import com.android.settings.utils.MyParcelable;
import com.android.settings.utils.ShowDialog;
import com.android.settings.utils.UniqueMark;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder> {

    private static final String TAG = "qll_resolution_adapter";
    private List<Resolution> mResolutionsList;
    private DisplayManager mDisPlayManager;
    private Context mContext;
    private static final int ADAPTIVE_POSITION = 0;
    public static int old_standard_position = 0;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgResolution;
        TextView tvResolution;
        LinearLayout llResolution;
        public ViewHolder(View view){
            super(view);
            imgResolution = (ImageView) view.findViewById(R.id.resolution_img);
            tvResolution = (TextView) view.findViewById(R.id.resolution_tv);
            llResolution = (LinearLayout) view.findViewById(R.id.resolution_ll_item);
        }
    }

    public ResolutionAdapter (List<Resolution> list){
        this.mResolutionsList = list;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.i(TAG, "onCreateViewHolder: parent----" + parent.getId()
                + " && viewType----" + viewType);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resolution_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        Log.i(TAG, "onBindViewHolder: holder----" + holder
                + " && position----" + position);
        Resolution resolution = mResolutionsList.get(position);
        holder.imgResolution.setImageResource(resolution.getImgSrc());
        holder.tvResolution.setText(resolution.getResolutionText());
        boolean isChecked = resolution.getIschecked();
        if (isChecked) {
            holder.llResolution.requestFocus();
        }
        holder.llResolution.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                handleClick(position);
            }
        });

    }


    @Override
    public int getItemCount(){
        return mResolutionsList.size();
    }

    void handleClick(int position){
        Log.i(TAG, "handleClick: position----" + position);
        DisplayManager dm = (DisplayManager) mContext
                .getSystemService(Context.DISPLAY_MANAGER_SERVICE);

        boolean isAdaptChecked = (position == 0); // 是否点击了自适应分辨率
        DisplayResolutionFragment.nowIsAdaptive = isAdaptChecked;

        Log.i(TAG, "handleClick: 将要设置的制式----" + mResolutionsList.get(position).getStandard());
        int standard = mResolutionsList.get(position).getStandard();
        dm.setDisplayStandard(standard);

        // 调用 DialogFragment 提示用户已做更改， 这里重新写个方法吧
        Fragment targetFragment = FragmentUtil.getCurrentFragment(mContext);
        Log.i(TAG, "handleClick: 传递的坐标为----" + old_standard_position);
        int oldStandard = mResolutionsList.get(old_standard_position).getStandard();
        MyParcelable resolutionParcelable = new MyParcelable(oldStandard, 1);
        ShowDialog.showDialog(resolutionParcelable, targetFragment, UniqueMark.RESOLUTION_FRAGMENT);

    }


}
