package com.example.testmvpapp.component.net;

import android.content.Context;

import com.example.testmvpapp.component.net.file.FileParentBody;
import com.example.testmvpapp.component.net.file.MultipartBodyCreator;

import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author: wuchao
 * @date: 2017/10/23 22:44
 * @desciption:
 */

public class RxRestClient {

    private static final WeakHashMap<String, Object> PARAMS = RxRestCreator.getParams();
    private final String URL;
    private final RequestBody BODY;
    private final Context CONTEXT;
    private final FileParentBody FILEPARENTBODY;

    public RxRestClient(String url,
                        WeakHashMap<String, Object> param,
                        RequestBody body,
                        Context context,
                        FileParentBody fileParentBody) {
        this.URL = url;
        PARAMS.putAll(param);
        this.BODY = body;
        this.CONTEXT = context;
        this.FILEPARENTBODY = fileParentBody;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Observable request(HttpMethod method) {
        final RxRestService service = RxRestCreator.getRxRestService();
        Observable observable = null;
        switch (method) {
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                //传入原始数据
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                //传入原始数据
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                MultipartBody tempBody = MultipartBodyCreator.paramsAndFilesToMultipartBody(PARAMS,FILEPARENTBODY);
                observable = RxRestCreator.getRxRestService().upload(URL, tempBody);
                break;
            default:
                break;
        }

        /*
        // 特别要注意线程的问题 下载文件不用在AndroidSchedulers.mainThread()
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
          */

        return observable;
    }

    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            //原始数据PARAMS必须为null
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            //原始数据PARAMS必须为null
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody> download() {
        final Observable<ResponseBody> responseBodyObservable = RxRestCreator.getRxRestService().download(URL);
        return responseBodyObservable;
    }
}
