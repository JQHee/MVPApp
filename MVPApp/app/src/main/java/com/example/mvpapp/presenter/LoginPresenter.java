package com.example.mvpapp.presenter;


import com.example.mvpapp.base.BaseView;
import com.example.mvpapp.contract.LoginContract;
import com.example.mvpapp.login.LoginActivity;

import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

    LoginActivity view;
    @Inject
    public LoginPresenter() {

    }

    @Override
    public void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    view.showToast("登录成功");
                    // 执行跳转
                    view.gotoMain();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void attachView(BaseView view) {
        this.view = (LoginActivity) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
