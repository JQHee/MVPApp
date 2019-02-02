package com.example.testmvpapp.sections.sign;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.di.component.DaggerActivityComponent;
import com.example.testmvpapp.di.module.ActivityModule;
import com.example.testmvpapp.presenter.SignUpPresenter;
import com.example.testmvpapp.ui.toolbar.ToolbarUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends SimpleActivity implements SignUpContract.View {

    @BindView(R.id.edit_sign_up_name)
    TextInputEditText mName;
    @BindView(R.id.edit_sign_up_email)
    TextInputEditText mEmail;
    @BindView(R.id.edit_sign_up_phone)
    TextInputEditText mPhone;
    @BindView(R.id.edit_sign_up_password)
    TextInputEditText mPassword;
    @BindView(R.id.edit_sign_up_re_password)
    TextInputEditText mRePassword;

    @Inject
    SignUpPresenter mPresenter;

    @OnClick({R.id.btn_sign_up})
    public void onClickSignUp() {

    }

    @OnClick({R.id.tv_link_sign_in})
    public void onClickLink() {
        mPresenter.gotoSignIn();
    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {
        DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
        mPresenter.attachView(this);
        ToolbarUtil.setToolbar(this, "注册", true);
    }

    @Override
    public void gotoSignIn() {

        // 跳转注册页面
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            // getSupportFragmentManager().popBackStack();//suport.v4包
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkForm() {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String rePassword = mRePassword.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            mName.setError("请输入姓名");
            isPass = false;
        } else {
            mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

        if (phone.isEmpty()) {
            mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty()) {
            mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            mRePassword.setError("密码验证错误");
            isPass = false;
        } else {
            mRePassword.setError(null);
        }

        return isPass;
    }

}
