package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Prayerincccc";
    private static final int REQUST_CODE_CAMERA_AUDIO_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TakePictureActivity.class));
        });

        findViewById(R.id.btn_camera).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
        });

        findViewById(R.id.btn_custom).setOnClickListener(v -> {
            //todo 在这里申请相机、麦克风、存储的权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO},
                        REQUST_CODE_CAMERA_AUDIO_STORAGE
                );
            }else{
                startActivity(new Intent(MainActivity.this, CustomCameraActivity.class));
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions.length == 0 || grantResults.length == 0) {
            return;
        }

        switch (requestCode) {
            case REQUST_CODE_CAMERA_AUDIO_STORAGE: {
                for (int i = 0; i < grantResults.length - 1; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "granted");
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "denied");
                    }
                }
                break;
            }
        }
    }
}
