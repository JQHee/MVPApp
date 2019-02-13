package com.example.testmvpapp.contract;

import com.example.testmvpapp.base.BaseContract;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.BaseView;

import io.reactivex.ObservableTransformer;

public class SignInContract {

    public interface View extends BaseContract.BaseView {
        // 跳转注册页面
        void gotoSignUp();
        // 跳转主框架页面
        void gotoMain();
        // 验证表单信息
        boolean checkForm();
    }

    public interface Presenter extends BaseContract.BasePresenter<View> {
        void login(String email, String password);
    }
}
