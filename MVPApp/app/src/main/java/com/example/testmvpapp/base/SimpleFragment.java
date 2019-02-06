package com.example.testmvpapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.di.component.DaggerFragmentComponent;
import com.example.testmvpapp.di.component.FragmentComponent;
import com.example.testmvpapp.di.module.FragmentModule;
import com.example.testmvpapp.sections.common.listener.PermissionListener;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;


/**
 * @desc SimpleFragment 无MVP的Fragment基类
 * @author HJQ
 * @date 2018/12/18
 */
public abstract class SimpleFragment extends LazyLoadFragment {

    public final String TAG = this.getClass().getSimpleName();
    protected View mRootView = null;
    protected Activity mActivity = null;
    protected Context mContext = null;
    // 用于显示加载中、网络异常，空布局、内容布局
    protected StateView mStateView = null;
    private Unbinder mUnbinder = null;
    // 是否初始化
    protected boolean isInited = false;
    private BasePresenter mPresenter = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) { // 防止重复添加
            if (getLayout() instanceof  Integer) {
                mRootView = inflater.inflate((Integer) getLayout(), null);
            } else if (getLayout() instanceof View) {
                mRootView =  (View) getLayout();
            } else {
                throw new ClassCastException("getLayout() type must be int or View");
            }
            mUnbinder = ButterKnife.bind(this, mRootView);
            onBindView(savedInstanceState, mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter = createPresenter();
    }

    @Override
    protected void onFragmentFirstVisible() {
        // 当第一次可见的时候，加载数据
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    protected void showToast(String meg) {
        Toast.makeText(MyApplication.getInstance(), meg, Toast.LENGTH_LONG).show();
    }

    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }

    protected abstract Object getLayout();
    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);
    protected abstract BasePresenter createPresenter();


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
}
