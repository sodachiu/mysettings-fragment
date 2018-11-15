package com.example.eileen.mysettings_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
    private static final String TAG = "qll_mainactivity";

    private ListView lvMenu;


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
                if (view.hasFocus()){
                    view.setBackgroundResource(R.drawable.menu_item_selector);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        return false;

    }



}
