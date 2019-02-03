package com.example.testmvpapp.ui.toolbar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.testmvpapp.R;

public class ToolbarUtil {

    /**
     * Activity 设置toolbar
     * @param activity 当前activity
     * @param title 标题
     * @param isShowBack 是否显示返回按钮
     */
    public static void setActivityToolbar(Activity activity,
                                          String title,
                                          boolean isShowBack
    ) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        AppCompatTextView titleText = activity.findViewById(R.id.toolbar_title);
        // 靠近返回键的标题 默认会显示应用名，将其隐藏
        toolbar.setTitle("");
        if (title != null) {
            titleText.setText(title);
        }
        final AppCompatActivity tempActivity = (AppCompatActivity)activity;
        tempActivity.setSupportActionBar(toolbar);
        // 指定向上返回
        ActionBar actionBar = tempActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowBack);
        }

    }

    /**
     * Activity 设置toolbar
     * @param activity 当前activity
     * @param title 标题
     * @param backTitle 靠近返回按的标题
     * @param isShowBack 是否显示返回按钮
     */
    public static void setActivityToolbar(Activity activity,
                                  String title,
                                  String backTitle,
                                  boolean isShowBack
    ) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        AppCompatTextView titleText = activity.findViewById(R.id.toolbar_title);
        // 靠近返回键的标题 默认会显示应用名，将其隐藏
        toolbar.setTitle(backTitle == null ? "" : backTitle);
        if (title != null) {
            titleText.setText(title);
        }
        final AppCompatActivity tempActivity = (AppCompatActivity)activity;
        tempActivity.setSupportActionBar(toolbar);
        // 指定向上返回
        ActionBar actionBar = tempActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowBack);
        }

    }

    /**
     * Activity 设置toolbar
     * @param activity 当前activity
     * @param backTitle 靠近返回按的标题
     */
    public static void setActivityToolbarBackTitle(Activity activity, String backTitle) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        // 靠近返回键的标题 默认会显示应用名，将其隐藏
        toolbar.setTitle(backTitle == null ? "" : backTitle);
    }

    /**
     * Activity 设置toolbar
     * @param activity 当前activity
     * @param title 标题
     */
    public static void setActivityToolbarTitle(Activity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        if (toolbar == null) {
            return;
        }
        AppCompatTextView titleText = activity.findViewById(R.id.toolbar_title);
        if (title != null && titleText != null) {
            titleText.setText(title);
        }
    }

    /**
     * Fragment 设置toolbar
     * @param fragment 当前fragment
     * @param rootView 当前fragment view
     * @param title 标题
     * @param isShowBack 是否显示返回按钮
     * @param isHasMenu 是否显示menu菜单
     */
    public static void setFragmentToolbar(Fragment fragment,
                                  View rootView,
                                  String title,
                                  boolean isShowBack,
                                  boolean isHasMenu
    ) {
        Toolbar toolbar = rootView.findViewById(R.id.id_toolbar);
        AppCompatTextView titleText = rootView.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        if (title != null) {
            titleText.setText(title);
        }
        final AppCompatActivity activity = (AppCompatActivity)fragment.getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        // 指定向上返回
        ActionBar actionBar = ((AppCompatActivity)fragment.getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowBack);
        }
        // 如果不设置 onCreateOptionsMenu 不会调用 (需要适配版本)
        if (isHasMenu) {
            fragment.setHasOptionsMenu(isHasMenu);
        }
    }

    /**
     * Fragment 设置toolbar
     * @param fragment 当前fragment
     * @param rootView 当前fragment view
     * @param title 标题
     * @param backTitle 返回按钮的标题
     * @param isShowBack 是否显示返回按钮
     * @param isHasMenu 是否显示menu菜单
     */
    public static void setFragmentToolbar(Fragment fragment,
                                          View rootView,
                                          String title,
                                          String backTitle,
                                          boolean isShowBack,
                                          boolean isHasMenu
    ) {
        Toolbar toolbar = rootView.findViewById(R.id.id_toolbar);
        AppCompatTextView titleText = rootView.findViewById(R.id.toolbar_title);
        toolbar.setTitle(backTitle == null ? "" : backTitle);
        if (title != null) {
            titleText.setText(title);
        }
        final AppCompatActivity activity = (AppCompatActivity)fragment.getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        // 指定向上返回
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowBack);
        }
        // 如果不设置 onCreateOptionsMenu 不会调用 (需要适配版本)
        if (isHasMenu) {
            fragment.setHasOptionsMenu(true);
        }

    }

    /**
     * Fragment 设置toolbar
     * @param rootView 当前fragment view
     * @param backTitle 返回按钮的标题
     */
    public static  void setFragmentToolbarBackTitle(View rootView, String backTitle) {
        if (rootView == null) {
            return;
        }
        Toolbar toolbar = rootView.findViewById(R.id.id_toolbar);
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(backTitle == null ? "" : backTitle);
    }

    /**
     * Fragment 设置toolbar
     * @param rootView 当前fragment view
     * @param title 标题
     */
    public static  void setFragmentToolbarTitle(View rootView, String title) {
        if (rootView == null) {
            return;
        }
        Toolbar toolbar = rootView.findViewById(R.id.id_toolbar);
        if (toolbar == null) {
            return;
        }
        AppCompatTextView titleText = rootView.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        if (title != null) {
            titleText.setText(title);
        }
    }

    /**
     * Activity 或 fragment toolbar 重新绘制 （如果menu 文字或图片需要动态改变）
     * @param activity 当前activity  (fragment 使用 fragment.getActivity())
     */
    public  static void setInvalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
    }
}
