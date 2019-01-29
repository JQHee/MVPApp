package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.sections.sign.SignInActivity;

import javax.inject.Inject;

public class SignInPresenter implements SignInContract.Presenter {

    SignInActivity view;
    @Inject
    public SignInPresenter() {

    }

    @Override
    public void signInAction() {
        // 发送登录请求
    }

    @Override
    public void attachView(BaseView view) {
        this.view = (SignInActivity) view;
    }

    @Override
    public void detachView() {

    }

    public void goToSignUpAction() {
        view.gotoSignUp();
    }

    /**
     * 微信登录
     */
    public void gotoWechatLoginAction() {

    }


}
