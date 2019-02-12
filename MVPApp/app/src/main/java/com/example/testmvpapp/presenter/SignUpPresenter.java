package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.sections.sign.SignUpActivity;
import com.example.testmvpapp.util.log.LatteLogger;

import javax.inject.Inject;

public class SignUpPresenter implements SignUpContract.Presenter {

    SignUpActivity view;
    @Inject
    public SignUpPresenter() {

    }

    @Override
    public void signUpAction() {
        // 发送注册请求
    }

    @Override
    public void attachView(BaseView view) {
        this.view = (SignUpActivity)view;
    }

    @Override
    public void detachView() {
        LatteLogger.d("SignUpPresenter detach");
    }

    public void gotoSignIn() {
        view.gotoSignIn();
    }

}
