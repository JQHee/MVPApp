package com.example.testmvpapp.di.module;

import android.content.Context;


import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.di.scope.ContextLife;
import com.example.testmvpapp.di.scope.PerApp;

import dagger.Module;
import dagger.Provides;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/19 0019 下午 6:56
 * mail : 2726449200@qq.com
 * describe ：
 */
@Module
public class ApplicationModule {
    private MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }
    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext(){
        return mApplication.getApplicationContext();
    }
}
