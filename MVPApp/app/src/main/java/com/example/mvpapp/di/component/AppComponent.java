package com.example.mvpapp.di.component;

import com.example.mvpapp.app.MyApplication;
import com.example.mvpapp.di.module.AppModule;
import javax.inject.Singleton;
import dagger.Component;

/**
 * @desc AppComponent
 * @author hjq
 * @date 2018/10/9
 */
@Singleton
@Component(modules = {AppModule.class/*, HttpModule.class*/})
public interface AppComponent {

    MyApplication getContext();  // 提供App的Context

//    BaseApiService retrofitHelper();  //提供http请求的帮助类这里暂时不管，后续讲

}
