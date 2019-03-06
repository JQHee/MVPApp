package com.example.testmvpapp.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testmvpapp.R;
import com.example.testmvpapp.sections.common.listener.PermissionListener;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.base.CleanLeakUtils;
import com.example.testmvpapp.util.base.DensityUtil;
import com.example.testmvpapp.util.base.ResUtil;
import com.example.testmvpapp.util.base.StatusBarUtils;
import com.example.testmvpapp.util.base.StringUtils;
import com.example.testmvpapp.util.storage.BFPreference;
import com.example.testmvpapp.util.storage.ConstantKey;
import com.github.nukc.stateview.StateView;
import com.jaeger.library.StatusBarUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @desc SimpleActivity 无MVP的activity基类
 * @author HJQ
 * @date 2018/12/18
 */
public abstract class SimpleActivity extends RxAppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected ProgressDialog mProgressDialog;
    protected Context mContext;
    private Unbinder mUnBinder = null;
    // 用于显示加载中、网络异常，空布局、内容布局
    protected StateView mStateView = null;
    public PermissionListener mPermissionListener = null;

    // toolbar基本参数配置
    private Menu mMenu;
    private LinearLayout mBaseLayout;
    private LinearLayout mContentLayout;
    protected Toolbar mToolbar;// 标题栏
    private boolean right1Visiabled = false;// 是否显示右1菜单
    private boolean right2Visiabled = false;// 是否显示右2菜单
    private boolean right1Enabled = true;// 右1菜单是否可用
    private boolean right2Enabled = true;// 右2菜单是否可用
    private int resRight1;// 最右的菜单图标
    private int resRight2;// 右数第二个图标
    private int mToolbarHeight;
    protected int topMargin = 0;
    private TextView actionText;
    private MenuItem mRight1MenuView;
    private MenuItem mRight2MenuView;
    private Handler mHandler;
    // 广播
    private BaseBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;


    protected abstract Object getLayout();
    protected abstract void init();
    // 有些设置必须在SetContent之前完成
    public void initBeforeSetContentView() {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initBeforeSetContentView();
        if (getLayout() instanceof Integer) {
            setCusContentView((Integer) getLayout());;
        } else if (getLayout() instanceof  View) {
            setCusContentView((View) getLayout());
        } else {
            throw new ClassCastException("getLayout() type must be int or View");
        }
        mUnBinder = ButterKnife.bind(this);
        ActivityCollector.addActivity(this);
        init();
        startReceiver();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_main), 38);
    }

    private void setCusContentView(Object layout) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        int baseId = R.layout.activity_base;
        View inflaterView = inflater.inflate(baseId, null);
        mBaseLayout = (LinearLayout) inflaterView.findViewById(R.id.base_layout);
        mContentLayout = (LinearLayout) inflaterView.findViewById(R.id.llContent);
        actionText = (TextView) inflaterView.findViewById(R.id.title);
        mToolbar = (Toolbar) inflaterView.findViewById(R.id.toolbar);

        View child;
        if (layout  instanceof Integer) {
            child = inflater.inflate((Integer) layout, null);
        } else {
            child =  (View) layout;
        }
        /*
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = topMargin;
        mContentLayout.addView(child, params);
        setContentView(mBaseLayout);
    }



    @Override
    protected void onDestroy() {

        // 键盘造成的内存泄漏
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);// 销毁广播
            mReceiver = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 生命周期
     */
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 验证用户登录
     */
    public void startActivityAfterLogin(Intent intent) {
        //未登录（这里用自己的登录逻辑去判断是否未登录）
        final boolean isLogin = BFPreference.getAppFlag(ConstantKey.IS_LOGIN);
        if (!isLogin) {
            ComponentName componentName = new ComponentName(mContext, SignInActivity.class);
            intent.putExtra("className", intent.getComponent().getClassName());
            intent.setComponent(componentName);
            super.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }


    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }


    /**
     * 加载框
     */
    public void showLoading(String message) {
        mProgressDialog = new ProgressDialog(mContext);
        if (mProgressDialog != null && !StringUtils.isEmpty(message)) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * 设置全屏显示
     */
    public void setFullScreen(boolean isShowToolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!DensityUtil.isNavigationBarExist(this)) {
                if (getWindow() != null) {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    getWindow().setAttributes(lp);
                    StatusBarUtils.setStatusBarVisible(this, isShowToolbar);
                }
            }
        }
    }

    /**
     * 退出应用的方法
     */
    public static void exitApp() {
       ActivityCollector.removeAllActivity();
    }

    /**
     * 事件总线
     */
    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }

    /**
     * 启动广播接收
     */
    private void startReceiver() {
        mIntentFilter = new IntentFilter();
        registerReceiver(mIntentFilter);
        // 没有广播则不注册广播
        if (mIntentFilter.countActions() > 0) {
            mReceiver = new BaseBroadcastReceiver();
            registerReceiver(mReceiver, mIntentFilter);
        }
    }

    /**
     * 接收广播 子类重写并从intent得到广播信息
     */
    public void receiveBroadcast(Context context, Intent intent) {
    }

    /**
     * 注册广播 子类重写并通过mIntentFilter.addAction()添加接收的广播
     */
    public void registerReceiver(IntentFilter mIntentFilter) {
    }

    private class BaseBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receiveBroadcast(context, intent);
        }
    }

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        return mHandler;
    }


    /**
     * 菜单从右往左数
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu=menu;
        getMenuInflater().inflate(R.menu.main, menu);
        mRight1MenuView = menu.findItem(R.id.ac_search);
        mRight2MenuView = menu.findItem(R.id.ac_share);
        mRight1MenuView.setVisible(right1Visiabled);
        mRight2MenuView.setVisible(right2Visiabled);
        if (resRight1 > 0) {
            mRight2MenuView.setIcon(resRight1);
        }
        if (resRight2 > 0) {
            mRight1MenuView.setIcon(resRight2);
        }
        mRight1MenuView.setEnabled(right1Enabled);
        mRight2MenuView.setEnabled(right2Enabled);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 刷新menu
     */
    public void refreshOptionsMenu() {
        if (mMenu != null) {
            mRight1MenuView = mMenu.findItem(R.id.ac_search);
            mRight2MenuView = mMenu.findItem(R.id.ac_share);
            mRight1MenuView.setVisible(right1Visiabled);
            mRight2MenuView.setVisible(right2Visiabled);
            if (resRight1 > 0) {
                mRight2MenuView.setIcon(resRight1);
            }
            if (resRight2 > 0) {
                mRight1MenuView.setIcon(resRight2);
            }
            mRight1MenuView.setEnabled(right1Enabled);
            mRight2MenuView.setEnabled(right2Enabled);
        }
    }


    /**
     * 设置标题（标题是居中的）
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        actionText.setText(title.toString());
    }
    /**
     * 设置标题（标题是居中的）
     * @param titleId
     */
    @Override
    public void setTitle(int titleId) {
        actionText.setText(titleId);
    }

    /**
     * 把Toolbar当做ActionBar，menu还是可以像ActionBar一样用和处理的
     * @param resBack 返回按钮为图片
     */
    public void addToolBar(int resBack) {
        //设置返回按钮图片
        mToolbar.setNavigationIcon(resBack);
        setSupportActionBar();
    }

    /**
     * 把Toolbar当做ActionBar，menu还是可以像ActionBar一样用和处理的
     * 返回按钮显示字符串
     * @param backText 字符串
     */
    public void addToolBar(final String backText) {
        ViewTreeObserver vto2 = mToolbar.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mToolbar.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                mToolbarHeight = mToolbar.getHeight();
                mToolbar.setNavigationIcon(createDrawable(backText));
            }
        });
        setSupportActionBar();
    }

    /**
     * 把Toolbar当做ActionBar，menu还是可以像ActionBar一样用和处理的
     * 返回按钮显示返回图片+字符串
     * @param resBack 返回图片
     * @param backText 字符串
     */
    public void addToolBar(final int resBack,final String backText) {
        ViewTreeObserver vto2 = mToolbar.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mToolbar.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                mToolbarHeight = mToolbar.getHeight();
                mToolbar.setNavigationIcon(createDrawable(resBack, backText));
            }
        });
        setSupportActionBar();
    }

    /**
     * 设置SupportActionBar
     */
    @SuppressLint("RestrictedApi")
    private void setSupportActionBar(){
        //把Toolbar当做ActionBar
        setSupportActionBar(mToolbar);
        //返回按钮点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //menu菜单点击事件
        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.ac_share) {
                    onRight2Click(findViewById(R.id.ac_share));
                } else if (i == R.id.ac_search) {
                    onRight1Click(findViewById(R.id.ac_search));
                } else {

                }
                return true;
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
    }

    /**
     * 右边第一个按钮（子类重写即可）
     * @param view
     */
    public void onRight1Click(View view) {
        // TODO Auto-generated method stub

    }

    /**
     * 右边第二个按钮（子类重写即可）
     * @param view
     */
    public void onRight2Click(View view) {
        // TODO Auto-generated method stub

    }

    /**
     * 是否显示右1菜单
     * @param visiabled
     */
    public void setRight1Menu(boolean visiabled) {
        right1Visiabled = visiabled;
        if (mRight1MenuView != null) {
            mRight1MenuView.setVisible(visiabled);
        }
    }

    /**
     * 是否显示右2菜单
     * @param visiabled
     */
    public void setRight2Menu(boolean visiabled) {
        right2Visiabled = visiabled;
        if (mRight2MenuView != null) {
            mRight2MenuView.setVisible(visiabled);
        }
    }

    /**
     * 右1菜单是否可用
     */
    public void setRight1Enabled(boolean enabled) {
        right1Enabled = enabled;
        if(mRight1MenuView!=null){
            mRight1MenuView.setEnabled(enabled);
        }
    }

    /**
     * 右2菜单是否可用
     */
    public void setRight2Enabled(boolean enabled) {
        right2Enabled = enabled;
        if(mRight2MenuView!=null){
            mRight2MenuView.setEnabled(enabled);
        }
    }

    /**
     * 自定义右边菜单不带监听事件
     * @param textAction
     */
    public void setRightTextAction(String textAction) {
        setRightTextAction(textAction, null);
    }

    /**
     * 自定义右边菜单带监听事件
     * @param textAction
     * @param listener
     */
    @SuppressLint("RtlHardcoded")
    public void setRightTextAction(String textAction, View.OnClickListener listener) {
        if (StringUtils.isBlank(textAction)) {
            return;
        }
        TextView actionText = new TextView(mContext);
        actionText.setText(textAction);
        actionText.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        int right = Gravity.RIGHT;
        params.gravity = right;
        actionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResUtil.getDimension(this,R.dimen.text_size_16));
        actionText.setPadding(12, 12, ResUtil.getDimension(this,R.dimen.base_padding_right), 12);
        actionText.setLayoutParams(params);
        actionText.setTextColor(Color.WHITE);
        if (listener != null) {
            actionText.setOnClickListener(listener);
        }
        mToolbar.addView(actionText);
    }

    /**
     * 自定义右边菜单带监听事件
     * @param view
     * @param listener
     */
    @SuppressLint("RtlHardcoded")
    public void setRightTextAction(View view, View.OnClickListener listener) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        int right = Gravity.RIGHT;
        params.gravity = right;
        if (listener != null) {
            view.setOnClickListener(listener);
        }
        mToolbar.addView(view, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT
                | Gravity.CENTER));
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 设置右上角的菜单
     * 只用一个按钮默认用右边第二个按钮
     * @param res
     */
    public void setRightMenuIcon(int... res) {
        if (res.length == 1) {
            resRight1 = res[0];
            setRight2Menu(true);
        } else if (res.length == 2) {
            resRight1 = res[0];
            resRight2 = res[1];
            setRight1Menu(true);
            setRight2Menu(true);
        }
    }

    /**
     * 是否隐藏Toolbar
     */
    public void isHiddenToolbar(boolean isHidden) {
        if (isHidden) {
            mToolbar.setVisibility(View.GONE);
        } else {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 创建文字Drawable
     * @param resBack 返回图片
     * @param text 文字
     */
    private Drawable createDrawable(int resBack,String text) {
        Bitmap imgMarker;//返回图片
        int width, height; // 新画的图片高度和宽带
        Bitmap imgTemp; // 临时标记图
        imgMarker = BitmapFactory.decodeResource(getResources(),resBack);

        // imgTemp左间距
        int l = (int) getResources().getDimension(R.dimen.spacing);

        // 设置letter字符串字体大小和颜色和得到字符串的宽度和长度信息
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(getResources().getDimension(
                R.dimen.dp_18));
        textPaint.setColor(Color.WHITE);
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        //设置新图片宽高
        width = l + imgMarker.getWidth() + rect.width() + 10;// 左间距+返回图片宽度+返回字体宽度+10
        height = mToolbarHeight;// imgTemp高度
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setFilterBitmap(true);

        // 图片的left值保持不变，top通过得到Toolbar高度除2，然后图片高度除2，两个再相减，这样图片就竖向居中了
        int t = (height / 2) - (imgMarker.getHeight() / 2);
        canvas.drawBitmap(imgMarker, l, t, paint);

        // y通过得到Toolbar高度除2，然后根据字体的高度除2，两个再相减，再加上字体高度，这样字体就竖向居中了
        int y = ((height / 2) - (rect.height() / 2)) + rect.height();
        y = (int) (y - getResources().getDimension(R.dimen.back_text_size_h));// 向上移动调整一下
        canvas.drawText(text, imgMarker.getWidth() + l, y, textPaint);

        canvas.save();
        // 下面这个方法废弃
        // canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        //得到返回图片和字符串拼成的Drawable
        return (Drawable) new BitmapDrawable(getResources(), imgTemp);
    }

    /**
     * 创建文字Drawable
     * @param text 文字
     */
    private Drawable createDrawable(String text) {
        int width, height; // 新画的图片高度和宽带
        Bitmap imgTemp; // 临时标记图

        // imgTemp左间距
        int l = (int) getResources().getDimension(R.dimen.spacing);

        // 设置letter字符串字体大小和颜色和得到字符串的宽度和长度信息
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(getResources().getDimension(
                R.dimen.dp_18));
        textPaint.setColor(Color.WHITE);
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        //设置新图片宽高
        width = l + rect.width() + 10;// 左间距+返回字体宽度+10
        height = mToolbarHeight;// imgTemp高度
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setFilterBitmap(true);

        // y通过得到Toolbar高度除2，然后根据字体的高度除2，两个再相减，再加上字体高度，这样字体就竖向居中了
        int y = ((height / 2) - (rect.height() / 2)) + rect.height();
        y = (int) (y - getResources().getDimension(R.dimen.back_text_size_h));// 向上移动调整一下
        canvas.drawText(text, l, y, textPaint);

        canvas.save();
        // canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        //得到返回图片和字符串拼成的Drawable
        return (Drawable) new BitmapDrawable(getResources(), imgTemp);
    }

    /**
     * 设置背景颜色
     * @param resId
     */
    public void setBackground(int resId){
        mBaseLayout.setBackgroundResource(resId);
    }
    public void setBackgroundColor(int colorId){
        mBaseLayout.setBackgroundColor(colorId);
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }


}
