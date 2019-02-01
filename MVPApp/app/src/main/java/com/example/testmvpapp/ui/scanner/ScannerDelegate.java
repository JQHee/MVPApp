package com.example.testmvpapp.ui.scanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.callback.CallbackManager;
import com.example.testmvpapp.util.callback.CallbackType;
import com.example.testmvpapp.util.callback.IGlobalCallback;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * @author: wuchao
 * @date: 2018/1/11 21:50
 * @desciption:
 */

public class ScannerDelegate extends SimpleActivity implements ZBarScannerView.ResultHandler {

    private ScanView mScanView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mScanView == null) {
            mScanView = new ScanView(this);
        }
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
    protected Object getLayout() {
        return mScanView;
    }

    @Override
    protected void initEventAndData() {

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
