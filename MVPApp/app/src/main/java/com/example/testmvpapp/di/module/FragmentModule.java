package com.example.testmvpapp.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.testmvpapp.di.scope.ContextLife;
import com.example.testmvpapp.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/20 0020 下午 2:43
 * mail : 2726449200@qq.com
 * describe ：
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }
    @PerFragment
    @Provides
    @ContextLife("Activity")
    public Context provideActivityContext(){
        return mFragment.getActivity();
    }
    @Provides
    @PerFragment
    public Activity provideActivity(){
        return mFragment.getActivity();
    }
    @Provides
    @PerFragment

    public Fragment provideFragment(){
        return mFragment;
    }
}
