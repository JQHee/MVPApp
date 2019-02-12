package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.sections.sign.SignUpActivity;
import com.example.testmvpapp.util.log.LatteLogger;

import javax.inject.Inject;

public class SignUpPresenter extends BasePresenter<SignUpContract.View> implements SignUpContract.Presenter {

    @Inject
    public SignUpPresenter() {

    }

    @Override
    public void signUpAction() {
        // 发送注册请求
    }


    public void gotoSignIn() {
        mView.gotoSignIn();
    }

}
