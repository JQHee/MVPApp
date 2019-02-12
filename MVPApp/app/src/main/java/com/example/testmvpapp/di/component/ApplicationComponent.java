package com.example.testmvpapp.di.component;

import android.content.Context;


import com.example.testmvpapp.di.module.ApplicationModule;
import com.example.testmvpapp.di.scope.ContextLife;
import com.example.testmvpapp.di.scope.PerApp;

import dagger.Component;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/19 0019 下午 6:51
 * mail : 2726449200@qq.com
 * describe ：
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();
}