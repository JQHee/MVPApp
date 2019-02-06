package com.example.testmvpapp.component.net;

import java.util.ArrayList;

import okhttp3.Interceptor;

public class InterceptorCreator {

    // okhttp拦截器
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();


    /*
     * 静态内部类单例模式的初始化
     */
    private  static  class Holder {
        private  static  final InterceptorCreator INSTANCE = new InterceptorCreator();
    }

    /*
     * 线程安全的懒汉单例模式
     * @return
     */
    public static InterceptorCreator getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 拦截器
     */
    public final InterceptorCreator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        return this;
    }

    public final InterceptorCreator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        return this;
    }


    public final ArrayList<Interceptor> getInterceptors() {
        return INTERCEPTORS;
    }
}
