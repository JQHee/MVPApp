package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;

public class SignInContract {

    public interface View extends BaseView {
        // 跳转注册页面
        void gotoSignUp();
    }

    public interface Presenter extends BasePresenter {
        void signInAction();
    }
}
