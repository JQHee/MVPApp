package com.example.testmvpapp.sections.main.sort;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.ui.toolbar.ToolbarUtil;

import java.lang.reflect.Method;

public class SortFragment extends SimpleFragment {
    
    @Override
    protected Object getLayout() {
        return R.layout.fragment_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    /*
    OverFlow显示icon
    虽然菜单已经显示出来，但是我们设置的图标并没有出现
    */
    @SuppressLint("RestrictedApi")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //使菜单上图标可见
        if (menu != null && menu instanceof MenuBuilder) {
            //编sdk版本24的情况 可以直接使用 setOptionalIconsVisible
            if (Build.VERSION.SDK_INT > 23) {
                MenuBuilder builder = (MenuBuilder) menu;
                builder.setOptionalIconsVisible(true);
            } else {
                //sdk版本24的以下，需要通过反射去执行该方法
                try {
                    MenuBuilder builder = (MenuBuilder) menu;
                    Method m = builder.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            Toast.makeText(getActivity(), "返回", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.action_share) {
            Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.action_commit) {
            Toast.makeText(getActivity(), "完成", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
