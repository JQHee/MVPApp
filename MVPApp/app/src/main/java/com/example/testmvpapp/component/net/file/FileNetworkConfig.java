package com.example.testmvpapp.component.net.file;

import android.os.Handler;
import android.os.Looper;

import com.example.testmvpapp.component.net.ConstantService;
import com.example.testmvpapp.component.net.RxRestService;
import com.example.testmvpapp.component.net.file.download.DownloadInterceptor;
import com.example.testmvpapp.component.net.file.download.DownloadListener;
import com.example.testmvpapp.component.net.file.upload.UpLoadProgressInterceptor;
import com.example.testmvpapp.component.net.file.upload.UploadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 针对文件的Retrofit对象生成
 */
public class FileNetworkConfig {

    private static final String BASE_URL = ConstantService.BASE_URL;
    private static final int DEFAULT_TIMEOUT = 15;

    private static class SingletonHolder {
        private static final FileNetworkConfig INSTANCE = new FileNetworkConfig();
    }

    public static FileNetworkConfig getInstance() {
        return FileNetworkConfig.SingletonHolder.INSTANCE;
    }


    private FileNetworkConfig() {
    }


    /**
     * 文件上传
     * @param listener 文件的上传进度
     */
    public Retrofit getUpLoadRetrofit(UploadListener listener) {
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
     * @param listener 下载回调
     */
    public Retrofit getDownLoadRetrofit(DownloadListener listener) {
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

    /**
     * 文件下载
     * 借助AsyncTask 获取下载进度
     */
    public Retrofit getDownLoadRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
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
     *
     */
    public void downloadFile(final File file, String downloadURL, DownloadListener listener) {
        final Executor executor = new MainThreadExecutor();
        final DownloadInterceptor interceptor = new DownloadInterceptor(listener, executor);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        final RxRestService service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build()
                .create(RxRestService.class);
        new Thread(() -> {
            try {
                Response<ResponseBody> result = service.downloadWithDynamicUrl(downloadURL).execute();
                final File tempFile = writeFile(file, result.body().byteStream());
                if (listener != null){
                    executor.execute(()->{
                        listener.onDownloadFinish(tempFile);
                    });
                }

            } catch (IOException e) {
                if (listener != null){
                    executor.execute(()->{
                        listener.onFailed(e.getMessage());
                    });
                }
                e.printStackTrace();
            }
        }).start();
    }

    private File writeFile(final File file, InputStream ins) {
        if (ins == null)
            return null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadException(e.getMessage(), e);
        } finally {
            try {
                ins.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private class DownloadException extends RuntimeException {
        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private class MainThreadExecutor implements Executor
    {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r)
        {
            handler.post(r);
        }
    }
}
