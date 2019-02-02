package com.example.testmvpapp.ui.corner;

import android.content.Context;

public class ShapeCornerTools {


    /**
     * 得到demo的px大小
     *
     * @param dimen
     * @return
     */
    public static int getDimen720Px(Context context, int dimen) {
        float dp = dimen * 1080f / 750 / 3;
        return dip2px(context, dp);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
}
