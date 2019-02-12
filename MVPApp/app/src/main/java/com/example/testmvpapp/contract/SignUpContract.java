package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BaseContract;


public class SignUpContract {

    public interface View extends BaseContract.BaseView {
        // 跳转登录页面
        void gotoSignIn();
    }

    public interface Presenter extends BaseContract.BasePresenter<SignUpContract.View> {
        void signUpAction();
    }
}
