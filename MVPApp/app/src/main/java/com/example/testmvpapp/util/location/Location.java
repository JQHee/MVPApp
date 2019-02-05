package com.example.testmvpapp.util.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 高德定位类
 * Created by pjw on 2016/12/6.
 */
public class Location {
    //声明AMapLocationClient类对象
    private static AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private static AMapLocationClientOption mLocationOption = null;
    private static LocationCallback mLocationCallback;
    public static AMapLocation mMapLocation;//位置信息类



    /**
     * 定位回调
     */
    public interface LocationCallback{
        void result(AMapLocation aMapLocation);
    }

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //初始化的时候启动一次定位
        startLocation(null);
    }

    /**
     * 启动定位
     */
    public static void startLocation(LocationCallback mLocationCallback){
        Location.mLocationCallback=mLocationCallback;
        if(mLocationClient!=null){
            //启动定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 声明定位回调监听器
     * AMapLocationListener.java
     *
     * @param aMapLocation
     */
    static AMapLocationListener mAMapLocationListener = new AMapLocationListener(){

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mLocationClient.stopLocation();
            if(aMapLocation!=null && aMapLocation.getErrorCode()==0){
                //定位成功
                mMapLocation=aMapLocation;
                if(mLocationCallback!=null){
                    mLocationCallback.result(aMapLocation);
                }
            }else{
                //定位失败
                if(mLocationCallback!=null){
                    mLocationCallback.result(null);
                }
            }

        }
    };

}
