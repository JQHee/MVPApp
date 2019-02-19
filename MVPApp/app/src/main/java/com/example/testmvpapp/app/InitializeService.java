package com.example.testmvpapp.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.testmvpapp.database.message.DatabaseManager;
import com.example.testmvpapp.util.log.LatteLogger;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动初始化Service
 * InitializeService.start(this);//启动服务执行耗时操作
 */
public class InitializeService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.guesslive.caixiangji.service.action.app.create";
    public static final String EXTRA_PARAM = "com.guesslive.caixiangji.service.extra.PARAM";

    public InitializeService() {
        super("InitializeService");
    }

    /**
     * 启动调用
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit(EXTRA_PARAM);
            }
        }
    }

    /**
     * 启动初始化操作
     */
    private void performInit(String param) {
        // 初始化化日志打印工具
        LatteLogger.setup();
        initDB();
        initBugly();
        initJPUSH();
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
        MyApplication.registrationId = JPushInterface.getRegistrationID(this);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        DatabaseManager.getInstance().init(this);
    }
}
