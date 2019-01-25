package com.example.mvpapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mvpapp.MainActivity;
import com.example.mvpapp.R;
import com.example.mvpapp.app.MyApplication;
import com.example.mvpapp.contract.LoginContract;
import com.example.mvpapp.di.component.DaggerActivityComponent;
import com.example.mvpapp.di.module.ActivityModule;
import com.example.mvpapp.presenter.LoginPresenter;
import javax.inject.Inject;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).activityModule(new ActivityModule(this)).build().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void gotoMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClickLogin(View view) {
        // 调用登录方法
        mPresenter.login();
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
