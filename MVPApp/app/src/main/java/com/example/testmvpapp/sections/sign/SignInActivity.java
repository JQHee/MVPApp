package com.example.testmvpapp.sections.sign;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.di.component.DaggerActivityComponent;
import com.example.testmvpapp.di.module.ActivityModule;
import com.example.testmvpapp.presenter.SignInPresenter;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

public class SignInActivity extends SimpleActivity implements SignInContract.View {

    @BindView(R.id.edit_sign_in_email)
    TextInputEditText mEmail;
    @BindView(R.id.edit_sign_in_password)
    TextInputEditText mPassword;

    @Inject
    SignInPresenter mPresenter;

    @OnClick({R.id.btn_sign_in})
    public void onClickSignIn() {
        if (checkForm()) {
            mPresenter.signInAction();
        }
    }

    @OnClick({R.id.tv_link_sign_up})
    public void onClickLink() {
        mPresenter.goToSignUpAction();
    }

    @OnClick({R.id.icon_sign_in_wechat})
    public void onClickWeiChat() {
        // 微信登录
        mPresenter.gotoWechatLoginAction();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void initEventAndData() {
        DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }


    @Override
    public void gotoSignUp() {

        // 跳转注册页面
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 表单信息检验
     */
    private boolean checkForm() {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty()) {
            mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }
        return isPass;
    }
}
