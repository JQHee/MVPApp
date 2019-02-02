package com.example.testmvpapp.util.logout;


/**
 * 1.基类中添加广播
 * 2.ActivityCollector 管理所有的Activity
 */
/*
public class BaseCompatActivity extends AppCompatActivity {
    protected LoginOutBroadcastReceiver locallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建活动时，将其加入管理器中
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.gesoft.admin.loginout");
        locallReceiver = new LoginOutBroadcastReceiver();
        registerReceiver(locallReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 取消注册广播接收器
        unregisterReceiver(locallReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 销毁活动时，将其从管理器中移除
        ActivityCollector.removeActivity(this);
    }
}
*/

/**
 * 退出接收器的实现
 */
/*
public class LoginOutBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCollector.finishAll();  // 销毁所有活动
        Intent intent1 = new Intent(context, MainActivity.class);
        context.startActivity(intent1);
    }
}
*/

/**
 * 退出按钮点击
 */
/*
private void loginOut() {
    Intent intent = new Intent("com.gesoft.admin.loginout");
    sendBroadcast(intent);
}
*/

