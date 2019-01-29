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
        if (checkForm()) {

        }
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

    /**
     * 表单信息检验
     */
    private boolean checkForm() {
        final String email = view.mEmail.getText().toString();
        final String password = view.mPassword.getText().toString();

        boolean isPass = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            view.mEmail.setError(null);
        }

        if (password.isEmpty()) {
            view.mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            view.mPassword.setError(null);
        }
        return isPass;
    }
}
