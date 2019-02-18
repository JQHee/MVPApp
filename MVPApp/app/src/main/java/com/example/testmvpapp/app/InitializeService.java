package com.example.testmvpapp.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
        /*
        initImageLoader();//初始化图片加载控件
        initRealm();//初始化Realm数据库
        initUser();//初始化用户(Realm数据库)
        initPush();//初始化推送
        initTuSdk();//初始化图sdk
        initQiNiu();//初始化七牛
        initQiyu();//网易七鱼
        initLogger();//注释启动,打开屏蔽打印
        */
    }
}
