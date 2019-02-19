package com.conways.download;

/**
 * Created by Conways on 2019/2/18
 * Describe:
 */
public class DownLoadTask implements Runnable {

    private String path;
    private String fileName;
    private String url;
    private DownLoadCallback downLoadCallback;
    private DownLoadEngine downLoadEngine;


    public DownLoadTask(String path, String fileName, String url, DownLoadCallback downLoadCallback) {
        this.path = path;
        this.fileName = fileName;
        this.url = url;
        this.downLoadCallback = downLoadCallback;
        downLoadEngine = new DownLoadEngine();
    }

    @Override
    public void run() {
        downLoadEngine.start(path, fileName, url, downLoadCallback);
    }

    public void stop() {
        downLoadEngine.stop();
    }


}
