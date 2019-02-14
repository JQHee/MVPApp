package com.example.testmvpapp.component.net.file.download;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testmvpapp.util.log.LatteLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;

public class DownloadFileAsync extends AsyncTask<Object, Integer, File> {

    private final DownloadListener LISTENER;

    //声明publishProgress的更新标记
    // 总长度
    private static final int PROGRESS_MAX = 0X1;
    private static final int UPDATE = 0X2;
    int contentLen;//声明要下载的文件总长

    public DownloadFileAsync(DownloadListener listener) {
        this.LISTENER = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected File doInBackground(Object... params) {

        File file = null;
        try {
            // params 对应 task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, file); 相应的参数
            String urlStr = (String) params[0];
            LatteLogger.d(urlStr);
            URL url = new URL(urlStr);
            file = (File) params[1];
            //获取连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            //获取下载文件的大小
            contentLen = connection.getContentLength();
            //根据下载文件大小设置进度条最大值（使用标记区别实时进度更新）
            publishProgress(PROGRESS_MAX, contentLen);
            //循环下载（边读取边存入）
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                bos.flush();
                //实时更新下载进度（使用标记区别最大值）
                publishProgress(UPDATE, len);
            }
            bos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 在publishProgress被调用后，UI thread会调用这个方法
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        switch (values[0]){
            case PROGRESS_MAX: // 文件最大值
                // LatteLogger.d("Max-", String.valueOf(values[1]));
                if (LISTENER != null) {
                    LISTENER.onStartDownload(contentLen);
                }
                break;
            case UPDATE:
                // 下载进度
                // progress.incrementProgressBy(values[1]);
                int i = (values[1]*100) / contentLen;
                // LatteLogger.d("Current-", String.valueOf(i));
                if (LISTENER != null) {
                    LISTENER.onProgress(i);
                }
                break;
        }
    }

    // doInBackground方法执行完后被UI thread执行
    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        // LatteLogger.d(file.getPath());
        if (LISTENER != null) {
            LISTENER.onDownloadFinish(file.getPath());
        }
    }
}
