package com.conways.download;

public interface IEngine {

    void start(String path, String fileName, String url, DownLoadCallback downLoadCallback);

    void stop();

}
