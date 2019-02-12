package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.log.LatteLogger;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    @Inject
    public SignInPresenter() {

    }

    @Override
    public void signInAction() {
        // 发送登录请求
        login();
    }

    public void goToSignUpAction() {
        mView.gotoSignUp();
    }

    private void login() {
        mView.gotoMain();
    }

    /**
     * 微信登录
     */
    public void gotoWechatLoginAction() {

    }


}
