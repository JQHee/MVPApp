package com.example.mvpapp.main.login;

import com.example.mvpapp.base.contracts.LoginContract;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    view.showToast("登录成功");
                    view.gotoMain();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
