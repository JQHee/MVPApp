package com.example.testmvpapp.component.net;


import com.example.testmvpapp.component.net.entity.HttpResult;
import com.example.testmvpapp.component.rx.rxtool.RxHelper;
import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;


public class HttpUtil {

    /**
     * 构造方法私有
     */
    private HttpUtil() {

    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加线程管理并订阅
     * @param ob
     * @param cacheKey 缓存kay
     * @param event Activity 生命周期
     * @param lifecycleSubject
     * @param isSave 是否缓存
     * @param forceRefresh 是否强制刷新
     */
    public void toSubscribe(Observable ob, final ProgressSubscriber subscriber, String cacheKey, final ActivityEvent event, final PublishSubject<ActivityEvent> lifecycleSubject, boolean isSave, boolean forceRefresh) {
        //数据预处理
        Observable.Transformer<HttpResult<Object>, Object> result = RxHelper.handleResult(event,lifecycleSubject);
        Observable observable = ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示Dialog和一些其他操作
                    }
                });
        RetrofitCache.load(cacheKey,observable,isSave,forceRefresh).subscribe(subscriber);
    }
}
