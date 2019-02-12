package com.example.testmvpapp.sections.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDLocation;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.component.jpush.NotificationsUtils;
import com.example.testmvpapp.sections.main.discover.DiscoverFragment;
import com.example.testmvpapp.sections.main.index.IndexFragment;
import com.example.testmvpapp.sections.main.personal.PersonalFragment;
import com.example.testmvpapp.sections.main.sort.SortFragment;
import com.example.testmvpapp.ui.bottom.BottomBarAdapter;
import com.example.testmvpapp.ui.bottom.BottomBarLayout;
import com.example.testmvpapp.ui.bottom.BottomBarViewPager;
import com.example.testmvpapp.util.location.BdLocationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends SimpleActivity {

    protected final String TAG = this.getClass().getSimpleName();
    private static final int BAIDU_ACCESS_COARSE_LOCATION = 100;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private BottomBarViewPager mVpContent = null;
    private BottomBarLayout mBottomBarLayout = null;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mVpContent = (BottomBarViewPager) findViewById(R.id.vp_content);
        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);
        initData();
        initListener();
        checkNotificationPermissions();
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
        mBottomBarLayout.setCurrentItem(1);

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


    /**
     * 高德地图定位
     */
    public void startLocaion(){

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation !=null ) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    Log.i(TAG,"当前定位结果来源-----"+amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i(TAG,"纬度 ----------------"+amapLocation.getLatitude());//获取纬度
                    Log.i(TAG,"经度-----------------"+amapLocation.getLongitude());//获取经度
                    Log.i(TAG,"精度信息-------------"+amapLocation.getAccuracy());//获取精度信息
                    Log.i(TAG,"地址-----------------"+amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i(TAG,"国家信息-------------"+amapLocation.getCountry());//国家信息
                    Log.i(TAG,"省信息---------------"+amapLocation.getProvince());//省信息
                    Log.i(TAG,"城市信息-------------"+amapLocation.getCity());//城市信息
                    Log.i(TAG,"城区信息-------------"+amapLocation.getDistrict());//城区信息
                    Log.i(TAG,"街道信息-------------"+amapLocation.getStreet());//街道信息
                    Log.i(TAG,"街道门牌号信息-------"+amapLocation.getStreetNum());//街道门牌号信息
                    Log.i(TAG,"城市编码-------------"+amapLocation.getCityCode());//城市编码
                    Log.i(TAG,"地区编码-------------"+amapLocation.getAdCode());//地区编码
                    Log.i(TAG,"当前定位点的信息-----"+amapLocation.getAoiName());//获取当前定位点的AOI信息
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    /**
     * 检查通知权限是否已经开启
     */
    private void checkNotificationPermissions() {
        if (NotificationsUtils.isNotificationEnabled(this)) {
            Log.e(TAG, "onCreate: 通知权限 已开启");
            setBasicSetup(1);
            setBasicSetup(4);
        } else {
            Log.e(TAG, "onCreate: 通知权限 未开启");
            //提示用户去设置，跳转到应用信息界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
    }

    /**
     * 1-2-3-4
     * 增、删、改、查
     */
    public void setBasicSetup(int type) {
        if (type == 1) {
            //设置别名（新的调用会覆盖之前的设置）
            JPushInterface.setAlias(this, 0, "0x123");
            //设置标签（同上）
            JPushInterface.setTags(this, 0, setUserTags());
        } else if (type == 2) {
            //删除别名
            JPushInterface.deleteAlias(this, 0);
            //删除指定标签
            JPushInterface.deleteTags(this, 0, setUserTags());
            //删除所有标签
            JPushInterface.cleanTags(this, 0);
        } else if (type == 3) {
            //增加tag用户量(一般都是登录成功设置userid为目标，在别处新增加比较少见)
            JPushInterface.addTags(this, 0, setUserTags());
        } else if (type == 4) {
            //查询所有标签
            JPushInterface.getAllTags(this, 0);
            //查询别名
            JPushInterface.getAlias(this, 0);
            //查询指定tag与当前用户绑定的状态（MyJPushMessageReceiver获取）
            JPushInterface.checkTagBindState(this, 0, "0x123");
            //获取注册id
            JPushInterface.getRegistrationID(this);
        }
    }

    /**
     * 标签用户
     */
    private static Set<String> setUserTags() {
        //添加3个标签用户（获取登录userid较为常见）
        Set<String> tags = new HashSet<>();
        tags.add("0x123");
        tags.add("0x124");
        tags.add("0x125");
        return tags;
    }

}
