package com.example.testmvpapp.component.net;


import com.example.testmvpapp.component.net.interceptors.DownloadInterceptor;
import com.example.testmvpapp.component.net.listener.ProgressResponseListener;
import com.example.testmvpapp.component.net.interceptors.UpLoadProgressInterceptor;
import com.example.testmvpapp.component.net.listener.ProgressRequestListener;
import com.example.testmvpapp.component.net.interceptors.CommonInterceptor;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author: wuchao
 * @date: 2017/10/23 22:53
 * @desciption:
 */

public class RxRestCreator {

    /**
     * 参数容器
     */
    private static final class ParamsHolder {
        private static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    public static RxRestService getRxRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    public static <T> T createService(Class<T> tClass){
        return RetrofitHolder.RETROFIT_CLIENT.build().create(tClass);
    }


    /**
     * 创建带响应进度(下载进度)回调的service
     */
    public static <T> T createResponseService(Class<T> tClass, ProgressResponseListener listener){
        return RetrofitHolder.RETROFIT_CLIENT
                .client(OKHttpHolder.addProgressResponseListener(listener))
                .build()
                .create(tClass);
    }


    /**
     * 创建带请求体进度(上传进度)回调的service
     */
    public static <T> T createReqeustService(Class<T> tClass, ProgressRequestListener listener){
        return RetrofitHolder.RETROFIT_CLIENT
                .client(OKHttpHolder.addProgressRequestListener(listener))
                .build()
                .create(tClass);
    }

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = ConstantService.BASE_URL;
        private static final Retrofit.Builder RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {

        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();

        private static OkHttpClient.Builder addInterceptors() {

            // BUILDER.sslSocketFactory(HttpsTrustManager.createSSLSocketFactory());
            // BUILDER.hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier());
            BUILDER.addInterceptor(CommonInterceptor.mBaseHeaderInterceptor);
            BUILDER.addInterceptor(CommonInterceptor.mRewriteCacheControlInterceptor);
            BUILDER.addInterceptor(CommonInterceptor.mLoggingInterceptor);
            return BUILDER;
        }

        private static OkHttpClient addProgressResponseListener(ProgressResponseListener listener) {
            final DownloadInterceptor interceptor = new DownloadInterceptor(listener);
            return addInterceptors()
                    .addInterceptor(interceptor)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
        }

        private static OkHttpClient addProgressRequestListener(ProgressRequestListener listener) {
            final UpLoadProgressInterceptor interceptor = new UpLoadProgressInterceptor(listener);
            return addInterceptors()
                    .addInterceptor(interceptor)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptors()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     *  静态内部类 Service接口
     */
    private static final class RestServiceHolder {
        private static final RxRestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.build().create(RxRestService.class);
    }
}
