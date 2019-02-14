package com.example.testmvpapp.component.net.file.download;

import android.util.Log;

import com.example.testmvpapp.util.log.LatteLogger;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.MediaType;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadListener downloadListener;
    private Executor mExecutor;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, Executor executor, DownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.mExecutor = executor;
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
                    // Logger.t("DownloadUtil").d("已经下载的：" + totalBytesRead + "共有：" + responseBody.contentLength());
                    final int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                    if (mExecutor != null) {
                        mExecutor.execute(() -> downloadListener.onProgress(progress));
                    } else {
                        downloadListener.onProgress(progress);
                    }
                }
                return bytesRead;
            }
        };
    }

    /*
    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead: 0;
                if (null != downloadListener) {
                    if (bytesRead != -1) {
                        downloadListener.onProgress((int) (totalBytesRead));
                    }
                }
                return bytesRead;
            }
        };
    }
    */

}
