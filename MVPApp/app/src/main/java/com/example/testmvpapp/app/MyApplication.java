package com.example.testmvpapp.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.testmvpapp.di.component.AppComponent;
import com.example.testmvpapp.di.component.DaggerAppComponent;
import com.example.testmvpapp.di.module.AppModule;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    public static String registrationId;
    private static MyApplication instance;
    public static AppComponent appComponent;

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler

    @Override
    public void onCreate() {
        super.onCreate();

        //对全局属性赋值
        instance = this;
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();


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

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = instance.getPackageManager().getLaunchIntentForPackage(instance.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        instance.startActivity(intent);
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        MyApplication.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        MyApplication.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainThreadLooper(Looper mMainLooper) {
        MyApplication.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        MyApplication.mHandler = mHandler;
    }
}
