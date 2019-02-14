package com.example.testmvpapp.component.net.file.download;

/**
 * 下载进度接口
 */
public interface DownloadListener {

    void onStartDownload(long length);
    void onProgress(int progress);
    void onDownloadFinish(String filePath);
}
