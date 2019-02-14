package com.example.testmvpapp.component.net.listener;

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
