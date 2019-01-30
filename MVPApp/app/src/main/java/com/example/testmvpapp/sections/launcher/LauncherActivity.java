package com.example.testmvpapp.sections.launcher;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.view.Window;
import android.view.WindowManager;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.login.LoginInterceptor;
import com.example.testmvpapp.util.timer.BaseTimerTask;
import com.example.testmvpapp.util.timer.ITimerListener;
import com.jaeger.library.StatusBarUtil;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 欢迎页面
 */

public class LauncherActivity extends SimpleActivity implements ITimerListener {

    @BindView(R.id.tv_launcher_timer)
    AppCompatTextView mTvLauncherTimer;

    private Timer mTimer = null;
    private int mCount = 5;

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
        finish();
        // LoginInterceptor.interceptor(LauncherActivity.this, "NewFeaturesActivity", null);
        Intent intent = new Intent(LauncherActivity.this, NewFeaturesActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        StatusBarUtil.setTransparent(this);
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
