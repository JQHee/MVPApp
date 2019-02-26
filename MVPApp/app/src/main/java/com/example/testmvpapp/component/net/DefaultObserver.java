package com.example.testmvpapp.component.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.testmvpapp.util.log.LatteLogger;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 封装DefaultObserver 处理返回的json
 */
public abstract class DefaultObserver<T> implements Observer<T> {

    private static final String TAG = "DefaultObserver";
    public static final String CODE = "code";
    public static final int NO_LOGI_CODE = 302;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
        if (response instanceof String) {
            final String tempResponse = (String) response;
            final JSONObject profileJson = JSON.parseObject(tempResponse);
            final int code = profileJson.getInteger(CODE);
            if (code == NO_LOGI_CODE) {
                // 尚未登录
            }
        }
        onSuccess(response);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        // LogUtils.e("Retrofit", e.getMessage());
        if (e instanceof HttpException) {     //   HTTP错误
            // onException(ExceptionReason.BAD_NETWORK);
            // onFail(e.getMessage());
            /*
            HttpException httpException = (HttpException) e;
            try {
                String responseString = httpException.response().errorBody().string();
                final JSONObject profileJson = JSON.parseObject(responseString);
                final String message = profileJson.getString("message");
                onFail(message == null ? "" : message);
            } catch(IOException e1){
                // e1.printStackTrace();
            }
            */
            HttpException httpException = (HttpException) e;
            final String errorMsg = convertStatusCode(httpException);
            onFail(errorMsg);

        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);

        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);

        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        onFinish();
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    private static String convertStatusCode(HttpException httpException){
        Log.d(TAG, "convertStatusCode: "+httpException.code());
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            if(httpException.code() == 401){
//                throw new UnLoginException();
                msg = "请重新登录";
            } else {
                msg = "服务器无法处理请求";
            }
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

    /**
     * 服务器返回数据，但响应码不为200
     *
     */
    public void onFail(String message) {
        // ToastUtils.show(message);
    }

    public void onFinish(){}

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                // ToastUtils.show(R.string.connect_error, Toast.LENGTH_SHORT);
                break;

            case CONNECT_TIMEOUT:
                // ToastUtils.show(R.string.connect_timeout, Toast.LENGTH_SHORT);
                break;

            case BAD_NETWORK:
                // ToastUtils.show(R.string.bad_network, Toast.LENGTH_SHORT);
                break;

            case PARSE_ERROR:
                // ToastUtils.show(R.string.parse_error, Toast.LENGTH_SHORT);
                break;

            case UNKNOWN_ERROR:
            default:
                // ToastUtils.show(R.string.unknown_error, Toast.LENGTH_SHORT);
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}

