package com.example.testmvpapp.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testmvpapp.R;


/**
 * @author HJQ
 * @description: 对glide进行封装的工具类
 * @date 2017/6/19  20:43
 */

public class GlideUtils {

    public static void load(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_launcher);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void load(Context context, String url, ImageView iv, int placeHolderResId) {
        if (placeHolderResId == -1) {
            Glide.with(context)
                    .load(url)
                    .into(iv);
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderResId);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void loadRound(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .circleCrop();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    /*
    //获取图片真正的宽高
        Glide.with(this)
                .load(imgUrl)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.d(TAG, "width " + width); //200px
            Log.d(TAG, "height " + height); //200px
        }
    });
    */
}
