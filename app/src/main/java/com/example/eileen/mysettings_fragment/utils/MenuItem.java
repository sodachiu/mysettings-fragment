package com.example.eileen.mysettings_fragment.utils;

import android.util.Log;

import com.example.eileen.mysettings_fragment.R;

public class MenuItem {
    private static final String TAG = "qll_menuitem";
    private String itemName;
    private boolean isSelect = false;
    private int backgroundResource;

    public MenuItem(String itemName){
        Log.i(TAG, "MenuItem: 菜单项---->" + itemName);
        this.itemName = itemName;
    }

    public String getItemName(){
        Log.i(TAG, "getItemName: 当前菜单项---->" + itemName);
        return this.itemName;
    }

    public void setIsSelect(boolean isSelect){
        Log.i(TAG, "setIsSelect: 设置" + this.itemName + "是否选中---->" + isSelect);

        this.isSelect = isSelect;
    }

    public boolean getIsSelect(){
        Log.i(TAG, "getIsSelect: " + this.itemName + "是否被选中" + isSelect);
        return this.isSelect;
    }
    
    public int getBackgroundResource(){
        Log.i(TAG, "getBackgroundResource: ");
        if (isSelect){
            this.backgroundResource = R.drawable.menu_item_selector;

        }else {
            this.backgroundResource = R.drawable.menu_item_selector2;

        }
        return this.backgroundResource;
    }


}
