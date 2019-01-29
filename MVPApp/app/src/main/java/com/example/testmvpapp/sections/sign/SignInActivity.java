package com.example.testmvpapp.sections.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    @BindView(R.id.edit_sign_in_email)
    public TextInputEditText mEmail;
    @BindView(R.id.edit_sign_in_password)
    public TextInputEditText mPassword;

    @Inject
    SignInPresenter mPresenter;


    @OnClick({R.id.btn_sign_in})
    public void onClickSignIn() {
        mPresenter.signInAction();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).activityModule(new ActivityModule(this)).build().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void gotoSignUp() {
        finish();
        // 跳转注册页面
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }
}
