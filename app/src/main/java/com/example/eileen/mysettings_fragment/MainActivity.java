package com.example.eileen.mysettings_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "qll_mainactivity";

    private ListView lvMenu;
    private int mSelectPos = 0;
    public static final int ABOUT = 0;
    public static final int ETH_SETTING = 1;
    public static final int NET_INFO = 2;
    public static final int DATE_TIME = 3;
    public static final int DISPLAY = 4;
    public static final int STORAGE = 5;
    public static final int ADVANCED = 6;
    public static final int RESET = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){

        String[] menuArray = getResources().getStringArray(R.array.menu_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.menu_item, menuArray);
        lvMenu = (ListView) findViewById(R.id.main_lv_menu);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: 位置为：" + position);
                mSelectPos = position;
                switch (position){
                    case ABOUT:
                        Log.i(TAG, "onItemSelected: 关于本机");
                        inflateFragment(new AboutFragment());
                        break;
                    case ETH_SETTING:
                        Log.i(TAG, "onItemSelected: 网络配置");
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void inflateFragment(Fragment fragment){
        Log.i(TAG, "inflateFragment: ");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_ll_fragment_container, fragment);
        transaction.commit();
    }


}
