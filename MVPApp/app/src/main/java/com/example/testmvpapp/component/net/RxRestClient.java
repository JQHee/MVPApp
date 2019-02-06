package com.example.testmvpapp.component.net;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
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
    private final File FILE;

    public RxRestClient(String url,
                        WeakHashMap<String, Object> param,
                        RequestBody body,
                        Context context,
                        File file) {
        this.URL = url;
        PARAMS.putAll(param);
        this.BODY = body;
        this.CONTEXT = context;
        this.FILE = file;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method) {
        final RxRestService service = RxRestCreator.getRxRestService();
        Observable<String> observable = null;
        switch (method) {
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                //传入原始数据
                observable = service.postRaw(URL, BODY == null ? getFormData() : BODY);
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
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                //以form的形式提交文件
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                observable = RxRestCreator.getRxRestService().upload(URL, body);
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

        /* 示例的网络请求
        RxRestClient.builder()
                .url("search.php?key=")
                .loader(getContext())
                .build()
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        LatteLoader.stopLoading();
                        final String searchItemText = mSearchEdit.getText().toString();
                        saveItem(searchItemText);
                        mSearchEdit.setText("");
                        //展示一些东西
                        //弹出一段话
                    }
                });
         */

        return observable;
    }

    // 拼接如果有多图和参数一起上传
    private  final RequestBody getFormData() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry: PARAMS.entrySet()
                ) {
            if (entry.getValue() instanceof File) {
                // 这里上传的是多图 (特别 file[])
                builder.addFormDataPart("file[]", ((File)entry.getValue()).getName(), RequestBody.create(MediaType.parse("image/*"), (File)entry.getValue()));
            } else {

                builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        RequestBody requestBody = builder.build();
        return  requestBody;
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
//        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME, SUCCESS, FAILURE, ERROR)
//                .handleDoenload();
        final Observable<ResponseBody> responseBodyObservable = RxRestCreator.getRxRestService().download(URL, PARAMS);
        return responseBodyObservable;
    }
}
