package com.example.testmvpapp.sections.sign;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.BaseActivity;
import com.example.testmvpapp.contract.SignUpContract;
import com.example.testmvpapp.presenter.SignUpPresenter;
import com.example.testmvpapp.ui.toolbar.ToolbarUtil;
import com.example.testmvpapp.util.base.MyImageSpan;
import com.example.testmvpapp.util.log.LatteLogger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity<SignUpPresenter> implements SignUpContract.View {


    @BindView(R.id.tv_protocol_content)
    AppCompatTextView mProtocolTV;

    // 存在多个用户协议的
    String[] protocols = {
            "《创客中心产品认购合同》",
            "《创客中心注册申请合同》",
            "《创客中心系统服务合同》",
            "《创客中心服务合同》",
            "《代理协议》"
    };
    private boolean isChecked;
    // 富文本
    private SpannableStringBuilder spannableStringBuilder;


    public static void start(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    private void setup() {
        final String string = "    我已认真阅读并同意";
        //图标(默认位选中)
        spannableStringBuilder = new SpannableStringBuilder(string);
        setIconSapn(spannableStringBuilder, R.drawable.ic_shop_normal);
        //选择按钮的点击事件
        ClickableSpan imagClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 显示协议内容
                if (isChecked) {
                    setIconSapn(spannableStringBuilder, R.drawable.ic_shop_check);
                } else {
                    setIconSapn(spannableStringBuilder, R.drawable.ic_shop_normal);
                }
                isChecked = !isChecked;

                mProtocolTV.setText(spannableStringBuilder);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.WHITE);
            }
        };
        spannableStringBuilder.setSpan(imagClick, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置徐泽状态图标
     *
     * @param spannableStringBuilder
     * @param resId
     */
    private void setIconSapn(SpannableStringBuilder spannableStringBuilder, int resId) {
        MyImageSpan imageSpan = new MyImageSpan(this, BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), resId), 2);
        spannableStringBuilder.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private SpannableStringBuilder setupProtocolsStyle() {
        for (int i = 0; i < protocols.length; i++) {
            final String protocol = protocols[i];
            SpannableStringBuilder protocolStringBuild = new SpannableStringBuilder(protocol);
            //协议
            //点击span
            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // 显示协议内容
                    // mView.showProtocol(protocol, finalI, protocols.length);
                    LatteLogger.d(protocol);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(Color.WHITE);
                }
            };
            protocolStringBuild.setSpan(clickableSpan, 0, protocol.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 前景
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R.color.colorPrimary));
            protocolStringBuild.setSpan(foregroundColorSpan, 0, protocol.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(protocolStringBuild);
            // 点
            if (i != protocols.length - 1) {
                SpannableStringBuilder dotStringBuild = new SpannableStringBuilder("、");
                ForegroundColorSpan dotSpan = new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R.color.color_666666));
                dotStringBuild.setSpan(dotSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(dotStringBuild);
            }
        }
        return spannableStringBuilder;
    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void init() {
        addToolBar(R.mipmap.menu_back);
        setTitle("注册");
        // 设置返回图标
        // ToolbarUtil.setActivityToolbar(this, "注册", true);

        setup();
        mProtocolTV.setText(setupProtocolsStyle());
        // 开始响应点击事件(不设置则不能相应事件)
        mProtocolTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void gotoSignIn() {
        // 跳转登录页面
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        /*
        MenuInflater inflater = getMenuInflater();
        //设置menu界面为res/menu/menu.xml
        inflater.inflate(R.menu.toolbar_menu, menu);
        */
        return super.onCreateOptionsMenu(menu);
    }


    /*
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
    */

    public boolean checkForm() {
        /*
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
        */

        return true;
    }

}
