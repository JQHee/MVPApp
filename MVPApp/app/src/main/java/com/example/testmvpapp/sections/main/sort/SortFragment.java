package com.example.testmvpapp.sections.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.ui.toolbar.ToolbarUtil;

public class SortFragment extends SimpleFragment {

    @Override
    protected Object getLayout() {
        return R.layout.fragment_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        ToolbarUtil.setFragmentToolbar(this, rootView,"分类", false, false);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
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
