package com.example.testmvpapp.sections.sign;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;

public class ForgetPasswordActivity extends SimpleActivity {

    @Override
    protected Object getLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void init() {
        setTitle("忘记密码");
        addToolBar(R.mipmap.menu_back);
    }

}
