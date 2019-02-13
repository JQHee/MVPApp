package com.example.testmvpapp.presenter;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.component.net.ConstantService;
import com.example.testmvpapp.component.net.RxRestClient;
import com.example.testmvpapp.contract.SignInContract;
import com.example.testmvpapp.util.log.LatteLogger;

import java.io.IOException;

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
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String response) {
                            LatteLogger.d(response);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                HttpException httpException = (HttpException) e;
                                try {
                                    String responseString = httpException.response().errorBody().string();
                                    LatteLogger.d(responseString);
                                } catch(IOException e1){
                                    e1.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            // 登录成功跳转的主页面
            // mView.gotoMain();
        }
    }
}
