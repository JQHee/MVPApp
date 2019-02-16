package com.example.testmvpapp.sections.sign;

import android.content.ComponentName;
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


    @OnClick({R.id.btn_sign_in})
    public void onClickSignIn() {
        mPresenter.login("", "");
    }


    private void startActivity() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("className") != null) {
            String className = getIntent().getExtras().getString("className");
            getIntent().removeExtra("className");
            if (className != null && !className.equals(mActivityComponent.getActivityContext().getClass().getName())) {
                try {
                    ComponentName componentName = new ComponentName(mActivityComponent.getActivityContext(), Class.forName(className));
                    startActivity(getIntent().setComponent(componentName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }
    /*
    @BindView(R.id.edit_sign_in_email)
    TextInputEditText mEmail;
    @BindView(R.id.edit_sign_in_password)
    TextInputEditText mPassword;

    @OnClick({R.id.tv_link_sign_up})
    public void onClickLink() {
        mPresenter.goToSignUpAction();
    }

    @OnClick({R.id.icon_sign_in_wechat})
    public void onClickWeiChat() {
        // 微信登录
        mPresenter.gotoWechatLoginAction();
    }
    */

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
        startActivity();
        /*
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        */
    }

    /**
     * 表单信息检验
     */
    @Override
    public boolean checkForm() {
        /*
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
        */
        return true;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        // 绑定到Activity的pause生命周期（在pause销毁请求）
        // return this.bindUntilEvent(ActivityEvent.PAUSE);
        // 绑定activity，与activity生命周期一样;
        return this.bindToLifecycle();
    }
}
