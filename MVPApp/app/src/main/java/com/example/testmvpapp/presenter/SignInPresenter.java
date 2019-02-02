package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.util.log.LatteLogger;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class SignInPresenter implements SignInContract.Presenter {

    // SignInActivity view;
    // 防止循环引用
    private WeakReference<SignInActivity> weakRefView;
    @Inject
    public SignInPresenter() {

    }

    @Override
    public void signInAction() {
        // 发送登录请求
        if (obtainView().checkForm()) {
            login();
        }
    }

    @Override
    public void attachView(BaseView view) {
        weakRefView = new WeakReference<SignInActivity>((SignInActivity) view);
        // this.view = (SignInActivity) view;
    }

    @Override
    public void detachView() {
        if (weakRefView != null) {
            weakRefView.clear();
            weakRefView = null;
        }

        LatteLogger.d("SignInPresenter");

    }

    protected boolean isAttach() {
        return weakRefView != null && weakRefView.get() != null;
    }

    public SignInActivity obtainView() {
        return  isAttach() ? (SignInActivity)weakRefView.get() : null;
    }

    public void goToSignUpAction() {
        obtainView().gotoSignUp();
    }

    private void login() {
        obtainView().gotoMain();
    }

    /**
     * 微信登录
     */
    public void gotoWechatLoginAction() {

    }


}
