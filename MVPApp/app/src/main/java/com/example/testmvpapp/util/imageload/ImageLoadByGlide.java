package com.example.testmvpapp.util.imageload;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

/**
 * 图片显示的公共类 使用glide
 * Created by HJQ on 2018/4/2.
 */
public class ImageLoadByGlide implements ImageLoadInterface {

    private static final String TAG = "GlideUtils";

    /**
     * glide加载图片
     *
     * @param imageView view
     * @param url       url
     */
    public void display(Context mContext, final ImageView imageView, final String url, final ImageConfig config, final ImageLoadProcessInterface imageLoadProcessInterface){
        if (mContext == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> mContext is null");
            return;
        }
        // 不能崩
        if (imageView == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = imageView.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {//activity是否结束
                return;
            }
        }
        try {
            if ((config == null || config.defaultRes <= 0) && TextUtils.isEmpty(url)) {
                Log.e("GlideUtils", "GlideUtils -> display -> url is null and config is null");
                return;
            }
            RequestOptions requestOptions = new RequestOptions();
            if (config != null) {
                if (config.defaultRes > 0) {
                    requestOptions.placeholder(config.defaultRes);
                }
                if (config.failRes > 0) {
                    requestOptions.error(config.failRes);
                }
                if (config.scaleType != null) {
                    switch (config.scaleType) {
                        case CENTER_CROP:
                            requestOptions.centerCrop();
                            break;
                        case FIT_CENTER:
                            requestOptions.fitCenter();
                            break;
                        default:
                            requestOptions.fitCenter();
                            break;
                    }
                } else {
                    requestOptions.fitCenter();
                }
                if (config.radius > 0) {
                    requestOptions.transform(new RoundedCorners(config.radius));
                }
            }
            ImageViewTarget simpleTarget = new BitmapImageViewTarget(imageView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    Log.i("image", "onLoadStarted");
                    if (imageLoadProcessInterface != null) {
                        imageLoadProcessInterface.onLoadStarted();
                    }
                }

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);
                    Log.i("image", "onResourceReady");
                    if (imageLoadProcessInterface != null) {
                        imageLoadProcessInterface.onResourceReady();
                    }
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.i("image", "onLoadFailed");
                    if (imageLoadProcessInterface != null) {
                        imageLoadProcessInterface.onLoadFailed();
                    }
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                    super.onLoadCleared(placeholder);
                    Log.i("image", "onLoadCleared");
                    if (imageLoadProcessInterface != null) {
                        imageLoadProcessInterface.onLoadCleared();
                    }
                }

                @Override
                public void getSize(@NonNull SizeReadyCallback cb) {
                    if (config != null && config.width >= 0 && config.height >= 0)
                        cb.onSizeReady(config.width, config.height);
                    else {
                        super.getSize(cb);
                    }
                }
            };
            if (simpleTarget != null) {
                Glide.with(context).asBitmap().load(url).apply(requestOptions).into(simpleTarget);
            } else {
                Glide.with(context).asBitmap().load(url).apply(requestOptions).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param context
     */
    public void resumeLoad(Context context, String url) {
        if (context != null)
            Glide.with(context).resumeRequests();
    }

    /**
     * 清除一个资源的加载
     *
     * @param context
     */
    public void clearImageView(Context context, ImageView imageView, String url) {
        if (context != null && imageView != null)
            Glide.with(context).clear(imageView);
    }

    /**
     * 暂停加载图片
     *
     * @param context
     */
    public void pauseLoad(Context context, String url) {
        if (context != null)
            Glide.with(context).pauseRequests();
    }

}
