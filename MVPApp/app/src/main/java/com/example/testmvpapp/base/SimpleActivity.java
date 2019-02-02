package com.example.testmvpapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.sections.main.MainActivity;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.login.LoginConfig;
import com.example.testmvpapp.util.login.LoginResult;

import java.util.ListIterator;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @desc SimpleActivity 无MVP的activity基类
 * @author HJQ
 * @date 2018/12/18
 */
public abstract class SimpleActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private static Activity mCurrentActivity;
    private Unbinder mUnBinder;
    private BasePresenter mPresenter;
    protected  Bundle savedInstanceState;
    // 退出时的时间
    private long mExitTime;
    // 间隔
    private static final long WAIT_TIME = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        */
        this.savedInstanceState = savedInstanceState;
        if (getLayout() instanceof Integer) {
            setContentView((Integer) getLayout());;
        } else if (getLayout() instanceof  View) {
            setContentView((View) getLayout());
        } else {
            throw new ClassCastException("getLayout() type must be int or View");
        }

        mUnBinder = ButterKnife.bind(this);
        onViewCreated();
        ActivityCollector.addActivity(this);
        initEventAndData();
    }

    /**
     * 沉浸式状态栏（适配虚拟按键、状态栏）
     */
    protected void setTranslucentStatus() {
        // 5.0以上系统状态栏透明
        /**
         * 会影响布局，页面的空间会被拉伸
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void showToast(String meg) {
        Toast.makeText(this, meg, Toast.LENGTH_LONG).show();
    }


    protected void showLoadingDialog() {

    }

    protected void dissmissLoadingDialog() {

    }

    public void goToLogionActivity() {

    }

    protected void onViewCreated() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter = createPresenter();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    /**
     * 退出应用的方法
     */
    public static void exitApp() {
       ActivityCollector.removeAllActivity();
    }

    /**
     * 统一退出控制
     */
    @Override
    public void onBackPressed() {
        if (mCurrentActivity instanceof MainActivity){
            //如果是主页面
            if (System.currentTimeMillis() - mExitTime > 2000) {// 两次点击间隔大于2秒
//                UIUtils.showToast("再按一次，退出应用");
                mExitTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();// finish()
    }

    //对返回键进行监听
/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > WAIT_TIME) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    protected abstract Object getLayout();
    protected abstract void initEventAndData();
    protected abstract BasePresenter createPresenter();

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 是否需要登录登录
     */
    protected void startActivityWithLogin(Intent intent , boolean isNeedLogin , LoginResult startIntentStype){

        if (isNeedLogin){

            if (LoginConfig.isLogin()) {
                Intent tempIntent = new Intent(this , SignInActivity.class);
                if (LoginResult.START_FOR_RESULT == startIntentStype){
                    startActivityForResult(tempIntent , LoginConfig.REQUEST_CODE);
                }else if(LoginResult.START_NO_RESULT == startIntentStype){
                    startActivity(tempIntent);
                }
            } else {
                this.startActivity(intent);
            }
        }else {
            this.startActivity(intent);
        }
    }
}
