package com.example.testmvpapp.base;

public interface BaseView {
    void showToast(String msg);//将通用方法封装到这里
    // TODO 可以写上你常用的方法
    void showLoading();
    void dismissLoading();
}
