package com.example.testmvpapp.component.net.file.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 文件上传进度
 */
public class UploadProgressRequestBody extends RequestBody {


    private RequestBody mRequestBody;
    private ProgressRequestListener mUploadListener;
    private CountingSink mCountingSink;

    public UploadProgressRequestBody(RequestBody requestBody, ProgressRequestListener uploadListener) {
        mRequestBody = requestBody;
        mUploadListener = uploadListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink;

        mCountingSink = new CountingSink(sink);
        bufferedSink = Okio.buffer(mCountingSink);

        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            mUploadListener.onRequestProgress(bytesWritten, contentLength());
        }
    }
}

/*
    File file = new File(mPicPath);
    //是否需要压缩
    //实现上传进度监听
    UploadProgressRequestBody requestFile = new UploadProgressRequestBody(file, "image/*", new UploadProgressRequestBody.UploadCallbacks() {
        @Override
        public void onProgressUpdate(int percentage) {
            Log.e(TAG, "onProgressUpdate: " + percentage);
            mCircleProgress.setProgress(percentage);
        }

        @Override
        public void onError() {

        }

        @Override
        public void onFinish() {
        }
    });

    MultipartBody.Part body =
            MultipartBody.Part.createFormData("file", file.getName(), requestFile);
*/