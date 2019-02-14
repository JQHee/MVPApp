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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;

public class DownloadFileAsync extends AsyncTask<String, Integer, File> {

    private final DownloadListener LISTENER;
    private final File FILE;

    //声明publishProgress的更新标记
    // 总长度
    private static final int PROGRESS_MAX = 0X1;
    private static final int UPDATE = 0X2;
    int contentLen;//声明要下载的文件总长

    public DownloadFileAsync(DownloadListener listener, File file) {
        this.LISTENER = listener;
        this.FILE = file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected File doInBackground(String... params) {

        URL url;
        HttpURLConnection conn;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;

        try {
            url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            contentLen = conn.getContentLength();
            // publishProgress(UPDATE, contentLen);
            bis = new BufferedInputStream(conn.getInputStream());
            fos = new FileOutputStream(FILE);
            byte data[] = new byte[4 * 1024];
            long total = 0;
            int count;
            while ((count = bis.read(data)) != -1) {
                total += count;
                publishProgress(UPDATE, (int) (total * 100 / contentLen));
                fos.write(data, 0, count);
                fos.flush();
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FILE;
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
                int i = values[1];
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
        if (LISTENER != null) {
            LISTENER.onDownloadFinish(file.getPath());
        }
    }
}
