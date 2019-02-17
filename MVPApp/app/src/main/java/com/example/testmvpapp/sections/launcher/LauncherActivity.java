package com.example.testmvpapp.sections.launcher;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.main.MainActivity;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.base.CrashHandler;
import com.example.testmvpapp.util.login.LoginInterceptor;
import com.example.testmvpapp.util.storage.BFPreference;
import com.example.testmvpapp.util.storage.ConstantKey;
import com.example.testmvpapp.util.timer.BaseTimerTask;
import com.example.testmvpapp.util.timer.ITimerListener;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.testmvpapp.ui.widget.UIUtils.getContext;


/**
 * 欢迎页面
 */

public class LauncherActivity extends SimpleActivity implements ITimerListener {

    @BindView(R.id.rl_back_ground)
    RelativeLayout mrlayout;
    @BindView(R.id.tv_launcher_timer)
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
            startActivityAfterLogin(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
            /*
            if (BFPreference.getAppFlag(ConstantKey.IS_LOGIN)) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(LauncherActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
            */
        }


    }

    public void startActivityAfterLogin(Intent intent) {
        //未登录（这里用自己的登录逻辑去判断是否未登录）
        final boolean isLogin = BFPreference.getAppFlag(ConstantKey.IS_LOGIN);
        if (!isLogin) {
            ComponentName componentName = new ComponentName(getContext(), SignInActivity.class);
            intent.putExtra("className", intent.getComponent().getClassName());
            intent.setComponent(componentName);
            super.startActivity(intent);
        } else {
            super.startActivity(intent);
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

    @Override
    protected Object getLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initView() {
        isHiddenToolbar(true);
        setFullScreen();

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
