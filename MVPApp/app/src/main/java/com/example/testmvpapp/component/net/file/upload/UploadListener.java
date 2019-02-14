package com.example.testmvpapp.component.net.file.upload;

public interface UploadListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
