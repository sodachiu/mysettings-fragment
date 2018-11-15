package com.example.eileen.mysettings_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.example.eileen.mysettings_fragment.utils.MenuAdapter;
import com.example.eileen.mysettings_fragment.utils.MenuItem;
import com.example.eileen.mysettings_fragment.utils.MyUtils;

import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "qll_mainactivity";
    private RecyclerView rvMenu;
    private List<MenuItem> menuList;
    private Context mContext;
    private MenuAdapter mAdapter;
    private MenuAdapter.ViewHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        rvMenu = (RecyclerView) findViewById(R.id.main_rv_menu);
        mContext = getApplicationContext();
        menuList = MyUtils.getMenuList(mContext);
        mAdapter = new MenuAdapter(menuList);
        mHolder = mAdapter.onCreateViewHolder();
//        menuList.get(0).setIsSelect(true);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvMenu.setLayoutManager(llManager);
        rvMenu.setAdapter(mAdapter);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        Log.i(TAG, "initView: rv是否有焦点" + rvMenu.hasFocus());
        if (!rvMenu.hasFocus()){
//            mAdapter.notifyItemChanged(selectPosition);
            mAdapter.notifyDataSetChanged();
        }

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
//                refreshMenuDown();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
//                refreshMenuUp();
                break;
        }
        return false;
    }



}
