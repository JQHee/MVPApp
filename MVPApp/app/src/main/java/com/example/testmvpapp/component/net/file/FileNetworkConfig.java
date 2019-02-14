package com.example.testmvpapp.component.net.file;

import android.os.Handler;
import android.os.Looper;

import com.example.testmvpapp.component.net.ConstantService;
import com.example.testmvpapp.component.net.file.download.DownloadInterceptor;
import com.example.testmvpapp.component.net.file.download.DownloadListener;
import com.example.testmvpapp.component.net.file.upload.UpLoadProgressInterceptor;
import com.example.testmvpapp.component.net.file.upload.UploadListener;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 针对文件的Retrofit对象生成
 */
public class FileNetworkConfig {

    private static final String BASE_URL = ConstantService.BASE_URL;
    private static final int DEFAULT_TIMEOUT = 60;

    /**
     * 文件上传
     * @param listener 文件的上传进度
     */
    public static Retrofit getUpLoadRetrofit(UploadListener listener) {
        final UpLoadProgressInterceptor interceptor = new UpLoadProgressInterceptor(listener);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 文件下载
     * @param listener 文件的下载进度
     */
    public static Retrofit getDownLoadRetrofit(DownloadListener listener) {
        final Executor executor = new MainThreadExecutor();
        final DownloadInterceptor interceptor = new DownloadInterceptor(listener, executor);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static class MainThreadExecutor implements Executor
    {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r)
        {
            handler.post(r);
        }
    }
}
