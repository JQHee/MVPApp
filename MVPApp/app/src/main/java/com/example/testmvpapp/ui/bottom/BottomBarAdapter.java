package com.example.testmvpapp.ui.bottom;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.sections.main.index.IndexFragment;

import java.util.List;

public class BottomBarAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;

    public BottomBarAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
