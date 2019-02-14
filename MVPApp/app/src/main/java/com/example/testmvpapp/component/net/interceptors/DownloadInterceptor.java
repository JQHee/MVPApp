package com.example.testmvpapp.component.net.interceptors;

import com.example.testmvpapp.component.net.entity.DownloadResponseBody;
import com.example.testmvpapp.component.net.listener.ProgressResponseListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    private ProgressResponseListener downloadListener;

    public DownloadInterceptor(ProgressResponseListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response=chain.proceed(chain.request());
        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), downloadListener)).build();
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
