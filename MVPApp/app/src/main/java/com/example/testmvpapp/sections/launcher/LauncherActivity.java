package com.example.testmvpapp.sections.launcher;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.main.MainActivity;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.base.CrashHandler;
import com.example.testmvpapp.util.base.DensityUtil;
import com.example.testmvpapp.util.base.StatusBarUtils;
import com.example.testmvpapp.util.storage.BFPreference;
import com.example.testmvpapp.util.storage.ConstantKey;
import com.example.testmvpapp.util.timer.BaseTimerTask;
import com.example.testmvpapp.util.timer.ITimerListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.testmvpapp.ui.widget.UIUtils.getContext;


/**
 * 欢迎页面
 */

public class LauncherActivity extends AppCompatActivity implements ITimerListener {

    RelativeLayout mrlayout;
    AppCompatTextView mTvLauncherTimer;

    private Timer mTimer = null;
    private int mCount = 3;

    @OnClick(R.id.tv_launcher_timer)
    public void onViewClicked() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            checkIsShowScroll();
        }
    }

    private void initTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    private void checkIsShowScroll() {

        // LoginInterceptor.interceptor(LauncherActivity.this, "NewFeaturesActivity", null);

        if (!BFPreference.getAppFlag(ConstantKey.IS_FIRST_LOAD)) {
            // 跳转
            Intent intent = new Intent(LauncherActivity.this, NewFeaturesActivity.class);
            startActivity(intent);
            finish();

        } else {

            if (BFPreference.getAppFlag(ConstantKey.IS_LOGIN)) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(LauncherActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mrlayout = findViewById(R.id.rl_back_ground);
        mTvLauncherTimer = findViewById(R.id.tv_launcher_timer);

        initView();
    }

    /**
     * 设置全屏显示
     */
    public void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!DensityUtil.isNavigationBarExist(this)) {
                if (getWindow() != null) {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    getWindow().setAttributes(lp);
                    StatusBarUtils.setStatusBarVisible(this, false);
                }
            }
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 动画的使用
     */
    private void startAnimation() {
        //创建动画---ObjectAnimator动画对象
        //参数一,执行动画.参数二,动画类型.参数三,开始结束位置
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mrlayout, "alpha", 0.2f, 1.0f);
        //渐变时间
        objectAnimator.setDuration(800);
        //启动
        objectAnimator.start();

        //重点是监听跳转,
        //用到了设计模式___适配器模式
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            //这里面只选择一个onAnimationEnd执行完成,
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
    }

    private void initView() {
        setFullScreen();
        // hideBottomUIMenu();

        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        // 崩溃日志
                        CrashHandler.getInstance().init(MyApplication.getInstance());
                    } else {

                    }
                });
    }

    @Override
    public void onTimer() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvLauncherTimer != null) {
                    if (mCount >= 0) {
                        mTvLauncherTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    }
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
