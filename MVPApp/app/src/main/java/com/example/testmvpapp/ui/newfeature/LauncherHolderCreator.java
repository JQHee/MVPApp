package com.example.testmvpapp.ui.newfeature;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;


public class LauncherHolderCreator implements CBViewHolderCreator<LauncherHolder> {

    @Override
    public LauncherHolder createHolder() {
        return new LauncherHolder();
    }
}
