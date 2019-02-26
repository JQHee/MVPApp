package com.example.testmvpapp.component.net.entity;

import com.example.testmvpapp.component.net.listener.ProgressResponseListener;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.MediaType;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private ProgressResponseListener downloadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, ProgressResponseListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
        // downloadListener.onStartDownload(responseBody.contentLength());
    }
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                if (null != downloadListener) {
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Logger.t("DownloadUtil").d("已经下载的：" + totalBytesRead + "共有：" + responseBody.contentLength());
                    final int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                    downloadListener.onProgress(progress);
                }
                return bytesRead;
            }
        };
    }


}
