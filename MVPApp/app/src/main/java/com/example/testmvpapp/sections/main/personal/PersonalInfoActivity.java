package com.example.testmvpapp.sections.main.personal;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.log.LatteLogger;

import butterknife.OnClick;

/**
 * 个人资料
 */
public class PersonalInfoActivity extends SimpleActivity {

    @OnClick({R.id.cl_header, R.id.cl_name, R.id.cl_nick_name, R.id.cl_modify_pass_word})
    void onClickItem(View view) {
        switch (view.getId()){
            case R.id.cl_header:
                Intent intent = new Intent(getContext(), ModifyAvatarActivity.class);
                startActivity(intent);
                break;

            case R.id.cl_name:

                break;

            case R.id.cl_nick_name:

                break;

            case R.id.cl_modify_pass_word:

                break;

            default:break;
        }
    }

    @Override
    protected Object getLayout() {

        return R.layout.activity_personal_info;
    }

    @Override
    protected void initView() {
        addToolBar(R.mipmap.menu_back);
        setTitle("个人资料");
        Intent intent = getIntent();
        String text = intent.getStringExtra("name");
        LatteLogger.d(text);
    }

}
