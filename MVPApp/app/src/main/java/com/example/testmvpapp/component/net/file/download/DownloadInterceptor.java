package com.example.testmvpapp.component.net.file.download;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    private DownloadListener downloadListener;
    private Executor mExecutor;

    public DownloadInterceptor(DownloadListener downloadListener, Executor executor) {
        this.downloadListener = downloadListener;
        this.mExecutor = executor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response=chain.proceed(chain.request());
        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), mExecutor, downloadListener)).build();
    }

    /*
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response=chain.proceed(chain.request());
        return response.newBuilder()
                .addHeader("Accept-Encoding", "identity")
                .body(new DownloadResponseBody(response.body(),downloadListener)).build();
    }
    */
}
