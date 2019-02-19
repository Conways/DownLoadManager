package com.conways.downloadmanager;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.conways.download.DownLoadCallback;
import com.conways.download.DownLoadManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "http://cdn12.down.apk.gfan.net.cn/Pfiles/2018/05/15/1139548_a1c27539-b4b2-49cf-bfcc-d9519195eb82.apk";

    private Button start;
    private Button stop;
    private ProgressBar progressBar;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress);
        tv = findViewById(R.id.tv);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

    }


    private String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1001);
        } else {
            startDown();
        }
    }

    private void startDown() {
        DownLoadManager.getInstance().startDownLoad(Environment.getExternalStorageDirectory().getPath(), "test.apk", URL, new DownLoadCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long currentProgress, long total) {
                progressBar.setProgress((int) (currentProgress * 100 / total));
                tv.setText(currentProgress * 100 / total + "%");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                Log.e("zzzz", "onFailed: " + errorMsg);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            startDown();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                DownLoadManager.getInstance().stop(URL);
                break;
            case R.id.start:
                checkPermission();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownLoadManager.getInstance().release();
    }
}