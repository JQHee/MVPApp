package com.example.testmvpapp.component.net.file.download;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadRestService {

    /**
     * 文件下载
     * 注意：不加Streaming会直接写到内存，造成内容溢出
     */
    @Streaming
    @GET
    // Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);
    Observable<ResponseBody> download(@Url String url);
}
