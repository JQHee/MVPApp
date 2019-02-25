package com.example.testmvpapp.util.imageload;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片加载的接口
 * Created by hjq on 2018/4/2.
 */
public interface ImageLoadInterface {

    /**
     * 显示路径中的图片（网络、文件中）
     *
     * @param mContext
     * @param view
     * @param url
     * @param config                    配置参数
     * @param imageLoadProcessInterface 加载过程监听
     */
    void display(Context mContext, final ImageView view, String url, ImageConfig config, ImageLoadProcessInterface imageLoadProcessInterface);

    /**
     * 开始加载
     *
     * @param context
     */
    void resumeLoad(Context context, String url);

    /**
     * 暂停加载
     *
     * @param context
     */
    void pauseLoad(Context context, String url);

    /**
     * 清除一个资源的加载
     *
     * @param context
     */
    void clearImageView(Context context, ImageView imageView, String url);
}
