package com.example.testmvpapp.presenter;

import android.util.Patterns;

import com.example.testmvpapp.base.BaseView;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.sections.sign.SignUpActivity;

import javax.inject.Inject;

public class SignUpPresenter implements SignUpContract.Presenter {

    SignUpActivity view;
    @Inject
    public SignUpPresenter() {

    }

    @Override
    public void signUpAction() {
        // 发送注册请求
        if (checkForm()) {

        }
    }

    @Override
    public void attachView(BaseView view) {
        this.view = (SignUpActivity)view;
    }

    @Override
    public void detachView() {

    }

    private boolean checkForm() {
        final String name = view.mName.getText().toString();
        final String email = view.mEmail.getText().toString();
        final String phone = view.mPhone.getText().toString();
        final String password = view.mPassword.getText().toString();
        final String rePassword = view.mRePassword.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            view.mName.setError("请输入姓名");
            isPass = false;
        } else {
            view.mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            view.mEmail.setError(null);
        }

        if (phone.isEmpty()) {
            view.mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            view.mPhone.setError(null);
        }

        if (password.isEmpty()) {
            view.mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            view.mPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            view.mRePassword.setError("密码验证错误");
            isPass = false;
        } else {
            view.mRePassword.setError(null);
        }

        return isPass;
    }
}
