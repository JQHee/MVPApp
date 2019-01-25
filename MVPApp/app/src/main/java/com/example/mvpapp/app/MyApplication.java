package com.example.mvpapp.app;

import android.app.Application;
import com.example.mvpapp.di.component.AppComponent;
import com.example.mvpapp.di.component.DaggerAppComponent;
import com.example.mvpapp.di.module.AppModule;

public class MyApplication extends Application {

    private static MyApplication instance;
    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
//                    .httpModule(new HttpModule())  创建网络请求的module这里暂时不管，
                    .build();
        }
        return appComponent;
    }
}
