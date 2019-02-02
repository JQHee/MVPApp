package com.example.testmvpapp.ui.toolbar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.testmvpapp.R;
import com.example.testmvpapp.contract.SignInContract;

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
        actionBar.setDisplayHomeAsUpEnabled(isShowBack);

        // 重新绘制menu(如果menu中的文字或图片需要改变)
        // activity.invalidateOptionsMenu();
    }

    public static void setToolbarTitle(Activity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.id_toolbar);
        if (toolbar == null) {
            return;
        }
        AppCompatTextView titleText = activity.findViewById(R.id.toolbar_title);
        if (title != null && titleText != null) {
            titleText.setText(title);
        }
    }

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
        ((AppCompatActivity)fragment.getActivity()).setSupportActionBar(toolbar);
        // 指定向上返回
        ActionBar actionBar = ((AppCompatActivity)fragment.getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(isShowBack);
        // 如果不设置 onCreateOptionsMenu 不会调用
        if (isHasMenu) {
            fragment.setHasOptionsMenu(isHasMenu);
        }
        // 重绘
        // fragment.getActivity().invalidateOptionsMenu();

    }

    public static  void setFragmentToolbarTitle(View rootView, String title) {
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
}
