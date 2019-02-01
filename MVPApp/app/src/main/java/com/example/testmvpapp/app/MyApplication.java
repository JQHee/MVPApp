package com.example.testmvpapp.app;

import android.app.Application;
import com.example.testmvpapp.di.component.AppComponent;
import com.example.testmvpapp.di.component.DaggerAppComponent;
import com.example.testmvpapp.di.module.AppModule;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    public static String registrationId;
    private static MyApplication instance;
    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initBugly();
        initJPUSH();
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


    /* bugly 收集崩溃日志 */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "df307c3993", false);
    }

    /* 初始化极光推送 */
    private void initJPUSH() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // 获取极光推送的ID
        registrationId = JPushInterface.getRegistrationID(this);
    }
}
