package com.example.testmvpapp.ui.toolbar;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import com.example.testmvpapp.R;

public class ToolbarUtil {

    public static void setToolbar(Activity activity, String title, boolean isShowBack) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        AppCompatTextView titleText = activity.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        if (title != null) {
            titleText.setText(title);
        }
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
        // 指定向上返回
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        // 自定义返回的图标
        // 有api API 18 4.3 系统
        // actionBar.setHomeAsUpIndicator(R.drawable.menu_back_bg);  //设置自定义的返回键图标
        actionBar.setDisplayHomeAsUpEnabled(isShowBack);

        /*
         * 自定义返回按钮和自定义actionBar类似
         */

        // 自定义actionBar
        /*
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
        actionBar.setCustomView(R.layout.actionbar_custom);//设置自定义的布局：actionbar_custom
        */
        // 如果不设置 onCreateOptionsMenu 不会调用
    }
}
