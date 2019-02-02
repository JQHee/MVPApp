package com.example.testmvpapp.sections.main.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleFragment;

public class DiscoverFragment extends SimpleFragment {

    @Override
    protected Object getLayout() {
        return R.layout.fragment_discover;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
