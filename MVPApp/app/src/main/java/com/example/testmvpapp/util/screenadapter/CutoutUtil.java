package com.example.testmvpapp.util.screenadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;

import com.example.testmvpapp.app.MyApplication;

import java.lang.reflect.Method;

/**
 * 判断是否是允许全屏界面内容显示到刘海区域的刘海屏机型
 */
public class CutoutUtil {

    private static Boolean sAllowDisplayToCutout;
    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;// 是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;// 是否有圆角

    /**
     * 是否为允许全屏界面显示内容到刘海区域的刘海屏机型（与AndroidManifest中配置对应）
     */
    public static boolean allowDisplayToCutout() {
        if (sAllowDisplayToCutout == null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                // 9.0系统全屏界面默认会保留黑边，不允许显示内容到刘海区域
                return sAllowDisplayToCutout = false;
            }
            Context context = MyApplication.getInstance();
            if (hasCutout_Huawei(context)) {
                return sAllowDisplayToCutout = true;
            }
            if (hasCutout_OPPO(context)) {
                return sAllowDisplayToCutout = true;
            }
            if (hasCutout_VIVO(context)) {
                return sAllowDisplayToCutout = true;
            }
            if (hasCutout_XIAOMI(context)) {
                return sAllowDisplayToCutout = true;
            }
            return sAllowDisplayToCutout = false;
        } else {
            return sAllowDisplayToCutout;
        }
    }


    /**
     * 是否是华为刘海屏机型
     */
    @SuppressWarnings("unchecked")
    private static boolean hasCutout_Huawei(Context context) {
        if (!Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            return false;
        }
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            if (HwNotchSizeUtil != null) {
                Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
                return (boolean) get.invoke(HwNotchSizeUtil);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 华为刘海高度
     */
    public static int getNotchSizeAtHuawei(Context context) {

        int[] ret = new int[] { 0, 0 };
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            Log.e("NotchScreenUtil", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("NotchScreenUtil", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e("NotchScreenUtil", "getNotchSize Exception");
        }
        return ret[1];
    }

    /**
     * 是否是oppo刘海屏机型
     */
    @SuppressWarnings("unchecked")
    private static boolean hasCutout_OPPO(Context context) {
        if (!Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            return false;
        }
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * 官网上给的图上的刘海的固定高度是80px
     */
    public static int getNotchSizeAtOppo() {
        return 80;
    }

    /**
     * 是否是vivo刘海屏机型
     */
    @SuppressWarnings("unchecked")
    private static boolean hasCutout_VIVO(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (Boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);
            Log.d("NotchScreenUtil", "this VIVO device has notch in screen？" + ret);
        } catch (ClassNotFoundException e) {
            Log.e("NotchScreenUtil", "hasNotchInScreen ClassNotFoundException", e);
        } catch (NoSuchMethodException e) {
            Log.e("NotchScreenUtil", "hasNotchInScreen NoSuchMethodException", e);
        } catch (Exception e) {
            Log.e("NotchScreenUtil", "hasNotchInScreen Exception", e);
        }
        return ret;
    }

    public static int getNotchSize_VIVO(Context context){
        return dp2px(context, 32);
    }

    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    private static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,context.getResources().getDisplayMetrics());
    }

    /**
     * 是否是小米刘海屏机型
     */
    @SuppressWarnings("unchecked")
    private static boolean hasCutout_XIAOMI(Context context) {
        if (!Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            return false;
        }
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressLint("PrivateApi")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = "ro.miui.notch";
            params[1] = 0;
            return (Integer) getInt.invoke(SystemProperties, params) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static  int getNotchSize_XIAOMI(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
