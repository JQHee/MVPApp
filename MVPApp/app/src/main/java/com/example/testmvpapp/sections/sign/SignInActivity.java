package com.example.testmvpapp.sections.sign;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BaseActivity;
import com.example.testmvpapp.component.net.RxRestClient;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.presenter.SignInPresenter;
import com.example.testmvpapp.sections.main.MainActivity;
import com.example.testmvpapp.util.log.LatteLogger;
import com.trello.rxlifecycle2.LifecycleTransformer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends BaseActivity<SignInPresenter> implements SignInContract.View {

    @BindView(R.id.edit_sign_in_email)
    TextInputEditText mEmail;
    @BindView(R.id.edit_sign_in_password)
    TextInputEditText mPassword;

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
    protected Object getLayout() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void gotoSignUp() {

        // 跳转注册页面
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
     * 测试rx的网络请求
     */
    private void testNetWork() {

        RxRestClient.builder()
                .url("http://120.76.240.104:8017/api/index/index")
                .build()
                .get()
                .compose(this.bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                LatteLogger.d(o);
            }
        });
    }

    /**
     * 表单信息检验
     */
    public boolean checkForm() {
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

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        // 绑定到Activity的pause生命周期（在pause销毁请求）
        // return this.bindUntilEvent(ActivityEvent.PAUSE);
        // 绑定activity，与activity生命周期一样;
        return this.bindToLifecycle();
    }
}
