package com.example.eileen.mysettings_fragment.display;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder>
            implements View.OnClickListener{
    private static final String TAG = "qll_resolution_adapter";
    private List<Resolution> mResolutionsList;
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
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Log.i(TAG, "onBindViewHolder: holder----" + holder
                + " && position----" + position);
        Resolution resolution = mResolutionsList.get(position);
        holder.imgResolution.setImageResource(resolution.getImgSrc());
        holder.tvResolution.setText(resolution.getResolutionText());
        holder.llResolution.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        Log.i(TAG, "onClick: ");
    }

    @Override
    public int getItemCount(){
        return mResolutionsList.size();
    }

}
