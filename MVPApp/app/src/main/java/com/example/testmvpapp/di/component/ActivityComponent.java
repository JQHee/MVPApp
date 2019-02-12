package com.example.testmvpapp.di.component;

import android.app.Activity;
import android.content.Context;


import com.example.testmvpapp.di.module.ActivityModule;
import com.example.testmvpapp.di.scope.ContextLife;
import com.example.testmvpapp.di.scope.PerActivity;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.sections.sign.SignUpActivity;

import dagger.Component;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/19 0019 下午 7:39
 * mail : 2726449200@qq.com
 * describe ：
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    // void inject(ZhihuWebActivity zhihuWebActivity);

}
