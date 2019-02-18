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
        // 跳转忘记密码页面
        void gotoForgetPassword();
        // 跳转国家码选择列表
        void gotoCountryCode();
        // 验证表单信息
        boolean checkForm();
    }

    public interface Presenter extends BaseContract.BasePresenter<View> {
        void login(String userName, String password);
        void gotoSignUpAction();
        void goForgetPasswordAction();
        void gotoCountryCodeAction();
    }
}
