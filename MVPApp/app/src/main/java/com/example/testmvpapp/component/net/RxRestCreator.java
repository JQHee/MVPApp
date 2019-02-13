package com.example.testmvpapp.component.net;


import com.example.testmvpapp.component.net.interceptors.CommonInterceptor;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
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

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = ConstantService.BASE_URL;
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {

        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS = InterceptorCreator.getInstance().getInterceptors();

        private static OkHttpClient.Builder addInterceptor() {

            // BUILDER.sslSocketFactory(HttpsTrustManager.createSSLSocketFactory());
            // BUILDER.hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier());
            /*
            * head 头可能是需要变化的
            */
            /*
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }
            */
            BUILDER.addInterceptor(CommonInterceptor.mRewriteCacheControlInterceptor);
            BUILDER.addInterceptor(CommonInterceptor.mLoggingInterceptor);
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     *  静态内部类 Service接口
     */
    private static final class RestServiceHolder {
        private static final RxRestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RxRestService.class);
    }
}
