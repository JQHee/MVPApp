package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;

/**
 * 创建LoginContract来管理LoginView和LoginPresenter的方法接口
 */

public class LoginContract {

    public interface View extends BaseView {
        void gotoMain();
    }

    public interface Presenter extends BasePresenter {
        void login();
    }
}
