package com.example.testmvpapp.ui.scanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.callback.CallbackManager;
import com.example.testmvpapp.util.callback.CallbackType;
import com.example.testmvpapp.util.callback.IGlobalCallback;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * @author: hjq
 * @date: 2018/1/11 21:50
 * @desciption:
 */

public class ScannerActivity extends SimpleActivity implements ZBarScannerView.ResultHandler {

    private ScanView mScanView = null;

    @Override
    protected Object getLayout() {
        if (mScanView == null) {
            mScanView = new ScanView(this);
        }
        return mScanView;
    }

    @Override
    protected void init() {
        mScanView.setAutoFocus(true);
        mScanView.setResultHandler(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScanView != null) {
            mScanView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanView != null) {
            mScanView.stopCameraPreview();
            mScanView.stopCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_SCAN);
        if (callback != null) {
            callback.executeCallback(result.getContents());
        }
        finish();
    }
}
