package com.example.testmvpapp.sections.launcher;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.sign.SignInActivity;
import com.example.testmvpapp.ui.newfeature.LauncherHolderCreator;
import com.example.testmvpapp.util.storage.BFPreference;
import com.example.testmvpapp.util.storage.ConstantKey;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本新特性
 */

public class NewFeaturesActivity extends SimpleActivity implements OnItemClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.convenientBanner)
    ConvenientBanner<Integer> mConvenientBanner;

    @BindView(R.id.btn_skip)
    Button mButton;

    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    @OnClick(R.id.btn_skip)
    void skipButtonAction() {
        BFPreference.setAppFlag(ConstantKey.IS_FIRST_LOAD, true);
        Intent intent = new Intent(NewFeaturesActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_new_features;
    }

    @Override
    protected void initView() {
        initBanner();

    }

    private void initBanner() {
        // 数据清空，不然数据会重复添加
        INTEGERS.clear();
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);

        mConvenientBanner.setPages(new LauncherHolderCreator(), INTEGERS)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,
                // 不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnPageChangeListener(this)
                .setOnItemClickListener(this)
                .setCanLoop(false);

        // 设置数据刷新
        // INTEGERS.remove(0);
        // mConvenientBanner.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        //如果点击的是最后一个
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == INTEGERS.size() - 1) {
            mButton.setVisibility(View.VISIBLE);
            // 是否显示指示器
            mConvenientBanner.setPointViewVisible(false);
        } else {
            mButton.setVisibility(View.GONE);
            mConvenientBanner.setPointViewVisible(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
