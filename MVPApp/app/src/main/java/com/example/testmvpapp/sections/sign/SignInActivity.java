package com.example.testmvpapp.sections.sign;

import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.widget.EditText;

import com.example.testmvpapp.R;
import com.example.testmvpapp.aop.annotation.SingleClick;
import com.example.testmvpapp.base.BaseActivity;
import com.example.testmvpapp.component.net.RxRestClient;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.presenter.SignInPresenter;
import com.example.testmvpapp.sections.main.MainActivity;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.bus.LiveBus;
import com.example.testmvpapp.util.log.LatteLogger;
import com.trello.rxlifecycle2.LifecycleTransformer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends BaseActivity<SignInPresenter> implements SignInContract.View {

    @BindView(R.id.et_phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.et_pass_word)
    EditText mPassword;

    // @SingleClick
    @OnClick({R.id.btn_sign_in})
    public void onClickSignIn() {
        LatteLogger.d("防止重复点击判断");
        // ToastUtils.showToast("请输入手机号");
        // mPresenter.login(mPhoneNumber.getText().toString(), mPassword.getText().toString());
    }

    @OnClick(R.id.tv_register)
    public void onClickRegister() {
        mPresenter.gotoSignUpAction();
    }

    @OnClick(R.id.tv_forget_password)
    public void onClickForgetPassword() {
        mPresenter.goForgetPasswordAction();
    }

    @OnClick(R.id.cb_country_code)
    public void onClickCountryCode() {
        mPresenter.gotoCountryCodeAction();
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

        isHiddenToolbar(true);

        // 获取国家码选择回传的值
        LiveBus.getDefault().subscribe("COUNTRY_CODE").observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object o) {
                LatteLogger.d(o);
            }
        });
    }

    @Override
    public void gotoSignUp() {

        // 跳转注册页面
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoMain() {
        // startActivity();
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void gotoForgetPassword() {
        // 忘记密码
        Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoCountryCode() {
        // 忘记密码
        Intent intent = new Intent(SignInActivity.this, CountryCodeActivity.class);
        startActivity(intent);
    }

    /**
     * 表单信息检验
     */
    @Override
    public boolean checkForm() {
        final String phone = mPhoneNumber.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;
        if (phone.isEmpty() || phone.length() != 11) {
            isPass = false;
        }

        if (password.isEmpty() || password.length() < 6) {
            isPass = false;
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
