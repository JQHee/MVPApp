package com.example.testmvpapp.di.component;

import android.app.Activity;
import com.example.testmvpapp.MainActivity;
import com.example.testmvpapp.di.module.ActivityModule;
import com.example.testmvpapp.di.scope.ActivityScope;
import com.example.testmvpapp.login.LoginActivity;
import dagger.Component;

/**
 * @desc ActivityComponent 用于管理需要进行依赖注入的Activity
 * @author hjq
 * @date 2018/10/9
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);//用于注入MainActivity
    void inject(LoginActivity loginActivity);//用于注入LoginActivity
    //TODO 后续需要注入的Activity类都可以在这里添加 中像上面一样写就好了
}
