package com.example.testmvpapp.sections.main.personal;

import android.Manifest;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.component.contactsearch.AddressBookActivity;
import com.example.testmvpapp.sections.common.activities.ImageViewPagerActivity;
import com.example.testmvpapp.util.base.CrashHandler;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.log.LatteLogger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 个人资料
 */
public class PersonalInfoActivity extends SimpleActivity {

    @OnClick({R.id.cl_header, R.id.cl_name, R.id.cl_nick_name, R.id.cl_modify_pass_word})
    void onClickItem(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.cl_header:
                intent = new Intent(getContext(), ModifyAvatarActivity.class);
                startActivity(intent);
                break;

            case R.id.cl_name:
                intent = new Intent(getContext(), ImageViewPagerActivity.class);
                final List<String> images = new ArrayList<>();
                images.add("http://pic24.photophoto.cn/20120831/0038038066142800_b.jpg");
                images.add("http://pic24.photophoto.cn/20120831/0038038066142800_b.jpg");
                images.add("http://pic24.photophoto.cn/20120831/0038038066142800_b.jpg");
                images.add("http://pic24.photophoto.cn/20120831/0038038066142800_b.jpg");
                images.add("http://pic24.photophoto.cn/20120831/0038038066142800_b.jpg");
                intent.putStringArrayListExtra(ImageViewPagerActivity.IMG_URLS, (ArrayList<String>) images);
                startActivity(intent);
                break;

            case R.id.cl_nick_name:
                intent = new Intent(getContext(), AddPhotosActivity.class);
                startActivity(intent);
                break;

            case R.id.cl_modify_pass_word:
                final RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions
                        .request(Manifest.permission.READ_CONTACTS)
                        .subscribe(granted -> {
                            if (granted) {
                                Intent tempIntent = new Intent(getContext(), AddressBookActivity.class);
                                startActivity(tempIntent);
                            } else {

                            }
                        });
                break;

            default:break;
        }
    }

    @Override
    protected Object getLayout() {

        return R.layout.activity_personal_info;
    }

    @Override
    protected void init() {
        addToolBar(R.mipmap.menu_back);
        setTitle("个人资料");
        Intent intent = getIntent();
        String text = intent.getStringExtra("name");
        LatteLogger.d(text);
    }

}
