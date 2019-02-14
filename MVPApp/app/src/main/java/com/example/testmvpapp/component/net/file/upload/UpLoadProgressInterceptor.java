package com.example.testmvpapp.component.net.file.upload;

import com.example.testmvpapp.util.log.LatteLogger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class UpLoadProgressInterceptor implements Interceptor {

    private ProgressRequestListener mUploadListener;

    public UpLoadProgressInterceptor(ProgressRequestListener uploadListener) {
        mUploadListener = uploadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(null == request.body()){
            return chain.proceed(request);
        }

        Request build = request.newBuilder()
                .method(request.method(),
                        new UploadProgressRequestBody(request.body(),
                                mUploadListener))
                .build();
        return chain.proceed(build);
    }

}
