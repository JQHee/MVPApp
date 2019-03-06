package com.example.testmvpapp.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.testmvpapp.R;
import com.example.testmvpapp.database.message.DatabaseManager;
import com.example.testmvpapp.di.component.ApplicationComponent;
import com.example.testmvpapp.di.component.DaggerApplicationComponent;
import com.example.testmvpapp.di.module.ApplicationModule;
import com.example.testmvpapp.util.base.DensityUtil;
import com.example.testmvpapp.util.base.StatusBarUtils;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.log.LatteLogger;
import com.example.testmvpapp.util.screenadapter.CutoutAdapt;
import com.example.testmvpapp.util.screenadapter.CutoutUtils;
import com.jaeger.library.StatusBarUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * 入口文件优化：https://juejin.im/post/5b59a4e7e51d455f5f4cfa38
 */

public class MyApplication extends Application {

    public static String registrationId;
    private static MyApplication instance;
    private ApplicationComponent mApplicationComponent;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Thread mMainThread; // 主线程
    private static long mMainThreadId; // 主线程id
    private static Looper mMainLooper; // 循环队列
    private static Handler mHandler;   // 主线程Handler

    //根目录
    public static final String APP_ROOT_DIRECTORY = "zhenghexing";
    public static final String APPPATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_ROOT_DIRECTORY
            + File.separator;
    public static String DATA_DATA;// /data/data目录

    @Override
    public void onCreate() {
        super.onCreate();

        // 对全局属性赋值
        instance = this;
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();

        initApplicationComponent();
        ToastUtils.init(this);
        // 子线程初始化第三方（耗时优化）
        // InitializeService.start(this);
        LatteLogger.setup();
        initDB();
        initBugly();
        initJPUSH();
        initActivityLifeCycler();
        initARouter();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    /* 路由初始化 */
    private void initARouter() {
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);    // 尽可能早，推荐在Application中初始化
    }

    /* bugly 收集崩溃日志 */
    private void initBugly() {
        CrashReport.initCrashReport(this, "df307c3993", false);
    }

    /* 初始化极光推送 */
    private void initJPUSH() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // 获取极光推送的ID
        MyApplication.registrationId = JPushInterface.getRegistrationID(this);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        DatabaseManager.getInstance().init(this);
    }

    private void initActivityLifeCycler() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                // ZLog.d(TAG, activity.getClass().getSimpleName() + " Created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                // ZLog.d(TAG, activity.getClass().getSimpleName() + " Started");
                MyApplication.activity = activity;
                // 如果是允许全屏显示到刘海屏区域的刘海屏机型
                if (CutoutUtils.allowDisplayToCutout()) {
                    if (isFullScreen(activity)) { // 是否是全面屏
                        // 如果允许通过显示状态栏方式适配刘海屏
                        if (activity instanceof CutoutAdapt) {
                            // 显示状态栏
                            // StatusBarUtil.showStatusbar(activity.getWindow());
                        } else {
                            // 需自行将该界面视图元素下移，否则可能会被刘海遮挡
                        }
                    } else {
                        // 非全屏界面无需适配刘海屏

                    }
                }

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /* 是否是全屏显示 */
    public boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 解决 使得这个dex的方法数量被限制在65535之内保错问题
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void initApplicationComponent() {
        mApplicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this)).build();
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

    public static Activity getActivity() {
        return activity;
    }
}
