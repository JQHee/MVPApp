package com.example.testmvpapp.sections.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.sections.main.discover.DiscoverFragment;
import com.example.testmvpapp.sections.main.index.IndexFragment;
import com.example.testmvpapp.sections.main.personal.PersonalFragment;
import com.example.testmvpapp.sections.main.sort.SortFragment;
import com.example.testmvpapp.ui.bottom.BottomBarAdapter;
import com.example.testmvpapp.ui.bottom.BottomBarLayout;
import com.example.testmvpapp.ui.bottom.BottomBarViewPager;
import com.example.testmvpapp.ui.bottom.TabFragment;
import com.example.testmvpapp.util.location.BdLocationUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SimpleActivity {

    private static final int BAIDU_ACCESS_COARSE_LOCATION = 100;

    private BottomBarViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;

    private List<SimpleFragment> mFragmentList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
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

    /**
     * 动态请求权限，安卓手机版本在5.0以上时需要
     */
    private void myPermissionRequest() {
        // BAIDU_ACCESS_COARSE_LOCATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否拥有权限，申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, BAIDU_ACCESS_COARSE_LOCATION);
            }
            else {
                // 已拥有权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                myLocation();
            }
        }else {
            // 安卓手机版本在5.0时，配置清单中已申明权限，作相应处理，此处正对sdk版本低于23的手机
            myLocation();
        }
    }

    /**
     * 百度地图定位的请求方法   拿到 国 省 市  区
     */
    private void myLocation() {
        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    String mCounty = location.getCountry();        //获取国家
                    String mProvince = location.getProvince();     //获取省份
                    String mCity = location.getCity();             //获取城市
                    String mDistrict = location.getDistrict();     //获取区
                    Log.i("==requestLocation===", "myLocation: "+mCounty+"="+mProvince+"="+mCity+"="+mDistrict);
                }
            }
        });
    }

    /**
     * 权限请求的返回结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 第一次获取到权限，请求定位
                    myLocation();
                }
                else {
                    // 没有获取到权限，做特殊处理
                    Log.i("=========", "请求权限失败");
                }
                break;

            default:
                break;
        }
    }
}
