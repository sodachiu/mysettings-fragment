package com.example.eileen.mysettings_fragment.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eileen.mysettings_fragment.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<MenuItem> mMenuList;
    private Context mContext;
    private Activity mActivity;
    private static final String TAG = "qll_menuadapter";

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMenuItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuItem = (TextView) itemView.findViewById(R.id.menu_tv_item);

        }
    }

    public MenuAdapter(List<MenuItem> menuList){
        Log.i(TAG, "MenuAdapter: ");
        this.mMenuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType){

        View itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.menu_item, container, false);
        ViewHolder itemHolder = new ViewHolder(itemView);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder itemHolder, final int position){
        Log.i(TAG, "onBindViewHolder: ");
        final MenuItem item = mMenuList.get(position);
        itemHolder.tvMenuItem.setBackgroundResource(item.getBackgroundResource());
        itemHolder.tvMenuItem.setText(item.getItemName());

        itemHolder.tvMenuItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mContext = v.getContext();
                    mActivity = (Activity) mContext;
                    Log.i(TAG, "onFocusChange: 上下文为：" + mContext + "&&&当前活动为："
                            + mActivity);
                    Log.i(TAG, "onFocusChange: 位置为" + position + "的item获得焦点");
                    MenuItem focusItem = mMenuList.get(position);

                    for (MenuItem tmpItem : mMenuList){
                        tmpItem.setIsSelect(false);
                    }
                    focusItem.setIsSelect(true);
                    int selectPosition = getFocusItemPosition();
                    hasFocus(selectPosition);

                }
            }
        });


    }

    @Override
    public int getItemCount(){
        return mMenuList.size();
    }

    /*
    * 获取当前被选中的菜单
    *
    * */
    public int getFocusItemPosition(){
        int position = -1;
        for (int i = 0; i < getItemCount(); i++){
            if (mMenuList.get(i).getIsSelect()){
                position = i;
            }
        }
        return position;
    }

    public void hasFocus(int position){
        switch (position){
            case 0:

        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fm = mActivity.getFragmentManager();


    }

}
