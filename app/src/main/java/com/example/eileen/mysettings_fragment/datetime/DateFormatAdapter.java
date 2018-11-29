package com.example.eileen.mysettings_fragment.datetime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.example.eileen.mysettings_fragment.R;

import java.util.List;

public class DateFormatAdapter extends RecyclerView.Adapter<DateFormatAdapter.ViewHolder> {

    private static final String TAG = "qll_dt_fm_adapter";
    private List<DateFormat> mFormatsList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgFormat;
        TextClock tcFormat;
        LinearLayout llFormat;
        public ViewHolder(View view){
            super(view);
            Log.i(TAG, "ViewHolder: view为——" + view);
            imgFormat = (ImageView) view.findViewById(R.id.dt_img_format);
            tcFormat = (TextClock) view.findViewById(R.id.dt_tc_format);
            llFormat = (LinearLayout) view.findViewById(R.id.dt_ll_format);
        }
    }

    public DateFormatAdapter(List<DateFormat> list){
        Log.i(TAG, "DateFormatAdapter: ");
        mFormatsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType){
        Log.i(TAG, "onCreateViewHolder: container----" + container
                + " && viewType----" + viewType);
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.dt_format_item, container, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Log.i(TAG, "onBindViewHolder: holder----" + holder
                + " && position----" + position);
        DateFormat dateFormat = mFormatsList.get(position);
        holder.tcFormat.setFormat12Hour(dateFormat.getDateFormat());
        holder.tcFormat.setFormat12Hour(dateFormat.getDateFormat());
        holder.imgFormat.setImageResource(dateFormat.getImgSrc());
    }

    @Override
    public int getItemCount(){
        return mFormatsList.size();
    }


}
