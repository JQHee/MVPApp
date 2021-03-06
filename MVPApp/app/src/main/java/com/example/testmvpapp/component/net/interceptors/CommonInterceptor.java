package com.example.testmvpapp.component.net.interceptors;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.testmvpapp.util.log.LatteLogger;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 常用的http请求拦截器
 */
public class CommonInterceptor {

    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为only-if-cached时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    public static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//获得上一个请求
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 添加请求头
     */
    public static final Interceptor mBaseHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request mRequest = chain.request().newBuilder()
                    .header("token","login_token")
                    .build();
            return chain.proceed(mRequest);
        }
    };


    /**
     * 获取返回的状态码
     */
    public static final Interceptor mBaseResponseCodeInterceptor = new Interceptor() {

        public static final int HTTP_CODE_NOT_LOGIN = 300;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            LatteLogger.d("状态码" + String.valueOf(response.code()) );
            // 常用于拦截登录状态
            if (response.code() == HTTP_CODE_NOT_LOGIN) {

            }
            return response;
        }
    };

    /**
     * 日志拦截器
     */
    public static final HttpLoggingInterceptor mLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);


    private static final Interceptor mLoggingIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            String isSuccess = response.isSuccessful() ? "true" : "false";
            Logger.w(isSuccess);
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset();
            }
            String bodyString = buffer.clone().readString(charset);
            Logger.w(String.format("Received response json string " + bodyString));
            return response;
        }
    };

}

/* 添加公共参数
private class commonInterceptor  implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原先的请求
        Request originalRequest = chain.request();
        //重新构建url
        HttpUrl.Builder builder = originalRequest.url().newBuilder();
        //如果是post请求的话就把参数重新拼接一下，get请求的话就可以直接加入公共参数了
        if(originalRequest.method().equals("POST")){
            FormBody body = (FormBody) originalRequest.body();
            for(int i = 0; i < body.size();i++){
                Log.i("RequestFatory",body.name(i) + "---" + body.value(i));
                builder.addQueryParameter(body.name(i),body.value(i));
            }
        }
        //这里是我的2个公共参数
        builder.addQueryParameter("mallId", SharedPreferManager.getInstance().getValue("mallId"))
                .addQueryParameter("robotNo",SharedPreferManager.getInstance().getValue("robotNo"));
        //新的url
        HttpUrl httpUrl = builder.build();
        Request request = originalRequest.newBuilder()
                .method(originalRequest.method(),originalRequest.body())
                .url(httpUrl).build();
        return chain.proceed(request);
    }
}
*/


