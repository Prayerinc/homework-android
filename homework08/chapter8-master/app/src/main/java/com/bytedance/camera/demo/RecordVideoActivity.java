package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

public class RecordVideoActivity extends AppCompatActivity {

    private VideoView videoView;

    private static final String TAG = "Prayerincccc";
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoView = findViewById(R.id.img);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()){
                    videoView.pause();
                }else {
                    videoView.start();
                }
                return false;
            }
        });
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(RecordVideoActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                //todo 在这里申请相机、存储的权限

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_CAMERA
                );

            } else {
                //todo 打开相机拍摄
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,REQUEST_VIDEO_CAPTURE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //todo 播放刚才录制的视频
            Uri uri = intent.getData();
            videoView.setVideoURI(uri);
            videoView.start();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_CAMERA: {
                for(int i = 0;i<2;i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,"granted");
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG,"denied");
                    }
                }
                break;
            }
        }
    }
}
