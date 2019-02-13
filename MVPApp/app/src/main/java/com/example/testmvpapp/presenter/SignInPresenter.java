package com.example.testmvpapp.presenter;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.contract.SignInContract;

import javax.inject.Inject;

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    @Inject
    public SignInPresenter() {

    }

    public void goToSignUpAction() {
        mView.gotoSignUp();
    }

    /**
     * 微信登录
     */
    public void gotoWechatLoginAction() {

    }


    @Override
    public void login(String email, String password) {
        if (mView.checkForm()) {
            // 登录成功跳转的主页面
            mView.gotoMain();
        }
    }
}
