package com.conways.download;

public interface DownLoadCallback {
    //开始下载
    void onStart();

    //下载过程
    void onProgress(long currentProgress, long total);

    //下载成功并结束
    void onFinish();

    //下载失败
    void onFailed(int code, String errorMsg);


}
