package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;

public class SignUpContract {

    public interface View extends BaseView {
        // 跳转登录页面
        void gotoSignIn();
    }

    public interface Presenter extends BasePresenter {
        void signUpAction();
    }
}
