package com.example.testmvpapp.presenter;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.component.net.ConstantService;
import com.example.testmvpapp.component.net.DefaultObserver;
import com.example.testmvpapp.component.net.RxRestClient;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.util.log.LatteLogger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    @Inject
    public SignInPresenter() {

    }

    public void goToSignUpAction() {
        mView.gotoSignUp();
    }

    /**
     * 微信登录
     */
    public void gotoWechatLoginAction() {

    }


    @Override
    public void login(String userName, String password) {
        if (mView.checkForm()) {

            RxRestClient.builder()
                    .url(ConstantService.LOGIN)
                    .params("account", userName)
                    .params("password", password)
                    .build()
                    .post()
                    .compose(mView.bindToLife())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultObserver<String>(){
                        @Override
                        public void onSuccess(String response) {

                        }
                    });
            // 登录成功跳转的主页面
            // mView.gotoMain();
        }
    }
}
