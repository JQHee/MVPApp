package com.example.testmvpapp.util.imageload;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * 图片显示的公共类 使用picasso
 * Created by HJQ on 2018/4/2.
 */
public class ImageLoadByPicasso implements ImageLoadInterface {

    private static final String TAG = "PicassoUtils";

    /**
     * glide加载图片
     *
     * @param imageView view
     * @param url       url
     */
    public void display(Context mContext, final ImageView imageView, String url, final ImageConfig config, final ImageLoadProcessInterface imageLoadProcessInterface) {
        if (mContext == null) {
            Log.e("PicassoUtils", "PicassoUtils -> display -> mContext is null");
            return;
        }
        // 不能崩
        if (imageView == null) {
            Log.e("PicassoUtils", "PicassoUtils -> display -> imageView is null");
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
                Log.e("PicassoUtils", "PicassoUtils -> display -> url is null and config is null");
                return;
            }
            RequestCreator requestCreator = null;
            Uri loadUri = null;
            if (url.startsWith("http")) {
                //网络图片
                loadUri = Uri.parse(url);
            } else {
                //本地文件
                if (url.startsWith("file://")) {
                    //文件的方式
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                        url = Uri.parse(url).getPath();
                    }
                }
                File file = new File(url);
                if (file != null && file.exists()) {
                    //本地文件
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                        loadUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                    } else {
                        loadUri = Uri.fromFile(file);
                    }
                } else {
                    //可能是资源路径的地址
                    loadUri = Uri.parse(url);
                }
            }
            requestCreator = Picasso.get().load(loadUri);
            if (config != null) {
                if (config.defaultRes > 0) {
                    requestCreator.placeholder(config.defaultRes);
                }
                if (config.failRes > 0) {
                    requestCreator.error(config.failRes);
                }
                if (config.width > 0 && config.height > 0) {
                    requestCreator.resize(config.width, config.height);
                }
                if (config.radius > 0) {
                    requestCreator.transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            final Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(target);
                            RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
                            canvas.drawRoundRect(rect, config.radius, config.radius, paint);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                            canvas.drawBitmap(source, 0, 0, paint);
                            source.recycle();
                            return target;
                        }

                        @Override
                        public String key() {
                            return "radius-transform";
                        }
                    });
                }
            }

            if (imageLoadProcessInterface != null) {
                requestCreator.tag(url).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (imageLoadProcessInterface != null) {
                            imageLoadProcessInterface.onResourceReady();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (imageLoadProcessInterface != null) {
                            imageLoadProcessInterface.onLoadFailed();
                        }
                    }
                });
            } else {
                requestCreator.tag(url).into(imageView);
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
        if (!TextUtils.isEmpty(url))
            Picasso.get().resumeTag(url);
    }

    /**
     * 清除一个资源的加载
     *
     * @param context
     */
    public void clearImageView(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Picasso.get().invalidate(url);
    }

    /**
     * 暂停加载图片
     *
     * @param context
     */
    public void pauseLoad(Context context, String url) {
        if (!TextUtils.isEmpty(url))
            Picasso.get().pauseTag(url);
    }
}
