package com.example.testmvpapp.component.net.listener;

import java.io.File;

/**
 * 下载进度接口
 */
public interface ProgressResponseListener {

    void onStartDownload(long length);
    void onProgress(int progress);
    void onDownloadFinish(File file);
    void onFailed(String message);
}
