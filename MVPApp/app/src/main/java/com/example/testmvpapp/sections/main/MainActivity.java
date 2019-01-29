package com.example.testmvpapp.sections.main;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.sections.main.discover.DiscoverFragment;
import com.example.testmvpapp.sections.main.index.IndexFragment;
import com.example.testmvpapp.sections.main.personal.PersonalFragment;
import com.example.testmvpapp.sections.main.sort.SortFragment;
import com.example.testmvpapp.ui.bottom.BottomBarAdapter;
import com.example.testmvpapp.ui.bottom.BottomBarLayout;
import com.example.testmvpapp.ui.bottom.BottomBarViewPager;
import com.example.testmvpapp.ui.bottom.TabFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomBarViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;

    private List<SimpleFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 解决虚拟按键遮挡的问题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initData();
        initListener();
    }
    private void initView() {
        mVpContent = (BottomBarViewPager) findViewById(R.id.vp_content);
        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);
    }

    private void initData() {

        IndexFragment homeFragment = new IndexFragment();
        /*
        Bundle bundle1 = new Bundle();
        bundle1.putString(TabFragment.CONTENT,"第一个页面");
        homeFragment.setArguments(bundle1);
        */
        mFragmentList.add(homeFragment);

        SortFragment sortFragment = new SortFragment();
        mFragmentList.add(sortFragment);

        DiscoverFragment discoverFragment = new DiscoverFragment();
        mFragmentList.add(discoverFragment);

        PersonalFragment personalFragment = new PersonalFragment();
        mFragmentList.add(personalFragment);
    }

    private void initListener() {

        final BottomBarAdapter bottomBarAdapter = new BottomBarAdapter(getSupportFragmentManager(), mFragmentList);
        mVpContent.setAdapter(bottomBarAdapter);
        mBottomBarLayout.setViewPager(mVpContent);

        /*
        *  设置未读消息数
        */
        /*
        mBottomBarLayout.setUnread(0,20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1,101);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3,"NEW");//设置第四个页签显示NEW提示文字
        */
    }
}
