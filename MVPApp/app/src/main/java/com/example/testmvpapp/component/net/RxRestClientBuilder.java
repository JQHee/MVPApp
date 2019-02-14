package com.example.testmvpapp.component.net;

import android.content.Context;


import com.example.testmvpapp.component.net.entity.FileParentBody;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author: wuchao
 * @date: 2017/10/23 23:06
 * @desciption:
 */

public class RxRestClientBuilder {

    private static final WeakHashMap<String, Object> PARAMS = RxRestCreator.getParams();
    private String mUrl = null;
    private FileParentBody mFileParentBody = null;
    private RequestBody mBody = null;
    private Context mContext = null;

    RxRestClientBuilder() {

    }

    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RxRestClientBuilder fileParentBody(FileParentBody fileParentBody) {
        this.mFileParentBody = fileParentBody;
        return this;
    }

    public final RxRestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RxRestClientBuilder loader(Context context) {
        this.mContext = context;
        return this;
    }

    public final RxRestClient build() {
        return new RxRestClient(mUrl, PARAMS, mBody, mContext, mFileParentBody);
    }
}
