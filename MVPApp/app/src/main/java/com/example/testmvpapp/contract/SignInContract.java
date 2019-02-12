package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;

import io.reactivex.ObservableTransformer;

public class SignInContract {

    public interface View extends BaseView {
        // 跳转注册页面
        void gotoSignUp();
        // 跳转主框架页面
        void gotoMain();

        <T> ObservableTransformer<T, T> bindLifecycle();
    }

    public interface Presenter extends BasePresenter {
        void signInAction();
    }
}
