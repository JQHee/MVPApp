package com.example.testmvpapp.sections.main.index;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.ui.scanner.ScannerActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexFragment extends SimpleFragment {

   @BindView(R.id.tv_san)
   AppCompatImageView mScanImageView;

    @OnClick(R.id.tv_san)
    void onClickScanButton() {
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // 跳转二维码扫描页面
                        Intent intent = new Intent(getFragmentComponent().getAcitivty(), ScannerActivity.class);
                        startActivity(intent);
                    } else {

                    }
                });
    }

    @Override
    protected Object getLayout() {
        return R.layout.fragment_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

}
