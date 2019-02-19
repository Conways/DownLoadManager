package com.conways.download;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoadManager {

    private static final int ERROR_URL_IS_EMPTY = 0x00001;
    private static final int ERROR_URL_IS_EXSIT = 0x00002;
    private static final String MSG_DOWNLOAD_URL_IS_EMPTY = "下载地址为空";
    private static final String MSG_DOWNLOAD_URL_IS_EXSIT = "下载任务已经存在";


    private static DownLoadManager ourInstance;
    private HashMap<String, DownLoadTask> downLoadTasks;
    private ExecutorService downLoadPool;


    public static DownLoadManager getInstance() {
        if (null == ourInstance) {
            synchronized (DownLoadManager.class) {
                ourInstance = new DownLoadManager();
            }
        }
        return ourInstance;

    }

    private DownLoadManager() {
        downLoadTasks = new HashMap<>();
        downLoadPool = Executors.newFixedThreadPool(5);
    }


    public void startDownLoad(final String downLoadPath, String fileName, final String downLoadurl, @NonNull final DownLoadCallback downLoadCallback) {
        if (TextUtils.isEmpty(downLoadurl)) {
            downLoadCallback.onFailed(ERROR_URL_IS_EMPTY, MSG_DOWNLOAD_URL_IS_EMPTY);
            return;
        }
        if (downLoadTasks.containsKey(downLoadurl)) {
            downLoadCallback.onFailed(ERROR_URL_IS_EXSIT, MSG_DOWNLOAD_URL_IS_EXSIT);
            return;
        }
        DownLoadTask downLoadTask = new DownLoadTask(downLoadPath, fileName, downLoadurl, downLoadCallback);
        downLoadTasks.put(downLoadurl, downLoadTask);
        downLoadPool.submit(downLoadTask);
    }

    /**
     * 停止指定的下载任务
     *
     * @param url
     */
    public void stop(String url) {
        if (downLoadTasks.containsKey(url)) {
            downLoadTasks.get(url).stop();
            downLoadTasks.remove(url);
        }
    }

    /**
     * 停止所有的下载任务
     */
    public void stopAll() {
        for (Map.Entry<String, DownLoadTask> entry : downLoadTasks.entrySet()) {
            entry.getValue().stop();
        }
        downLoadTasks.clear();
    }

    /**
     * 停止所有的下载任务并释放下载线程池
     */
    public void release() {
        stopAll();
        downLoadPool.shutdown();
        ourInstance = null;
    }
}
