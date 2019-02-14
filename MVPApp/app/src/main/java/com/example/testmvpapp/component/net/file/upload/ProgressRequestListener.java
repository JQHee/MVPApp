package com.example.testmvpapp.component.net.file.upload;

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
