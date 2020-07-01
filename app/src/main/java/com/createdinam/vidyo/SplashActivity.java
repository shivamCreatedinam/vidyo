package com.createdinam.vidyo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.createdinam.vidyo.global.GlobalInit;

public class SplashActivity extends AppCompatActivity {
    private static String user_type;
    private static Boolean status;
    SharedPreferences sharedPreferences;
    GlobalInit globalInit;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        status = sharedPreferences.getBoolean("status", false);
        user_type = sharedPreferences.getString("user_type", "");
        Log.d("status", "" + status);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (globalInit.getInstance(getApplicationContext()).isNetworkAvaliable(SplashActivity.this)) {
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
        }, SPLASH_DISPLAY_LENGTH);
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
