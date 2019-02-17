package com.example.testmvpapp.util.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 资源工具类 by pjw on 2016/5/17.
 */
public class ResUtil {

    public static int getDimension(Context context,int res){
        return (int) context.getResources().getDimension(res);
    }

    public static int getColor(Context context,int res){
        return context.getResources().getColor(res);
    }

    public static Drawable getDrawable(Context context,int res){
        return context.getResources().getDrawable(res);
    }

    public static Bitmap getBitmap(Context context,int res){
        Drawable drawable = context.getResources().getDrawable(res);
        return ((BitmapDrawable) drawable).getBitmap();
    }
}
