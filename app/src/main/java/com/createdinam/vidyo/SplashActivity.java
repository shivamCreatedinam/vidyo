package com.createdinam.vidyo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.createdinam.vidyo.global.GlobalInit;

public class SplashActivity extends AppCompatActivity {
    private static String user_type;
    private static Boolean status;
    SharedPreferences sharedPreferences;
    GlobalInit globalInit;
    VideoView mVideoView;
    private static Uri video_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mVideoView = findViewById(R.id.splash_view_video);
        video_link = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
        mVideoView.setVideoURI(video_link);
        mVideoView.start();
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        status = sharedPreferences.getBoolean("status", false);
        user_type = sharedPreferences.getString("user_type", "");
        Log.d("status",""+status);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (globalInit.getInstance(getApplicationContext()).isNetworkAvaliable(SplashActivity.this)) {
//                    Toast.makeText(SplashActivity.this, "You are online!!!!", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(SplashActivity.this, "Internet Connection Found!", Toast.LENGTH_SHORT).show();
                    if (status) {
                        startDashboard();
                    } else {
                        startLoginAgain();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "You are not online!!!!", Toast.LENGTH_SHORT).show();
                    Log.v("Home", "############################You are not online!!!!");
                }
            }
        });
    }

    private void startLoginAgain() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private void startDashboard() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
