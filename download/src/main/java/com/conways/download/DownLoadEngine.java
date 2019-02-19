package com.conways.download;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadEngine implements IEngine {

    public Handler handler;


    public DownLoadEngine() {
        handler = new Handler(Looper.getMainLooper());
    }

    private HttpURLConnection conn;
    private InputStream read;
    private FileOutputStream write;


    @Override
    public void start(final String path, final String fileName, final String url, final DownLoadCallback downLoadCallback) {
        load(path, fileName, url, downLoadCallback);
    }

    @Override
    public void stop() {
        try {
            if (null != read) {
                read.close();
            }
            if (null != write) {
                write.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != conn) {
            conn.disconnect();
        }
    }

    private void load(String path, final String fileName, String url, final DownLoadCallback downLoadCallback) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    downLoadCallback.onStart();
                }
            });
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(15 * 1000);
            conn.setReadTimeout(15 * 1000);
            conn.connect();

            read = conn.getInputStream();
            final long length = conn.getContentLength();

            File apkFile = new File(path + "/" + fileName);
            write = new FileOutputStream(apkFile);

            long count = 0;
            byte[] buffer = new byte[1024];
            while (count < length) {
                int numread = read.read(buffer);
                count += numread;
                write.write(buffer, 0, numread);
                final long finalCount = count;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        downLoadCallback.onProgress(finalCount, length);
                    }
                });
                if (count == length) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            downLoadCallback.onFinish();
                        }
                    });
                    break;
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    downLoadCallback.onFailed(-1, e.getMessage());
                }
            });
            return;

        }
    }
}