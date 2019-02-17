package com.example.testmvpapp.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.di.component.DaggerFragmentComponent;
import com.example.testmvpapp.di.component.FragmentComponent;
import com.example.testmvpapp.di.module.FragmentModule;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
//import me.yokeyword.fragmentation.ISupportFragment;


/**
 * @desc SimpleFragment 无MVP的Fragment基类
 * @author HJQ
 * @date 2018/12/18
 */
public abstract class SimpleFragment extends RxFragment {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public final String TAG = this.getClass().getSimpleName();
    private Unbinder mUnbinder = null;
    protected View mRootView,mErrorView, mEmptyView;
    protected ProgressDialog mProgressDialog;


    protected abstract Object getLayout();
    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ARouter.getInstance().inject(this);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() instanceof  Integer) {
            mRootView = inflater.inflate((Integer) getLayout(), null);
        } else if (getLayout() instanceof View) {
            mRootView =  (View) getLayout();
        } else {
            throw new ClassCastException("getLayout() type must be int or View");
        }
        mUnbinder = ButterKnife.bind(this, mRootView);
        onBindView(savedInstanceState, mRootView);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    protected FragmentComponent getFragmentComponent(){

        return DaggerFragmentComponent.builder()
                .applicationComponent(((MyApplication) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
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
}
