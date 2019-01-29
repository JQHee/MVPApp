package com.example.testmvpapp.sections.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.di.component.DaggerActivityComponent;
import com.example.testmvpapp.di.module.ActivityModule;
import com.example.testmvpapp.presenter.SignUpPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    @BindView(R.id.edit_sign_up_name)
    public TextInputEditText mName;
    @BindView(R.id.edit_sign_up_email)
    public TextInputEditText mEmail;
    @BindView(R.id.edit_sign_up_phone)
    public TextInputEditText mPhone;
    @BindView(R.id.edit_sign_up_password)
    public TextInputEditText mPassword;
    @BindView(R.id.edit_sign_up_re_password)
    public TextInputEditText mRePassword;

    @Inject
    SignUpPresenter mPresenter;

    @OnClick({R.id.btn_sign_up})
    public void onClickSignUp() {

    }

    @OnClick({R.id.tv_link_sign_in})
    public void onClickLink() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).activityModule(new ActivityModule(this)).build().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void gotoSignIn() {
        // 跳转登录页面
        finish();
        // 跳转注册页面
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }
}
