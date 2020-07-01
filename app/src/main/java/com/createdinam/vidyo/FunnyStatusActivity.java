package com.createdinam.vidyo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.createdinam.vidyo.global.UnsafeOkHttpClient;
import com.createdinam.vidyo.model.GetPostFromWeb;
import com.createdinam.vidyo.model.PostModel;
import com.createdinam.vidyo.modelS.MediaObject;
import com.createdinam.vidyo.modelS.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FunnyStatusActivity extends AppCompatActivity {
    Retrofit retrofit;
    GetPostFromWeb getPostFromWeb;
    ViewPager2 videoviewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_status);

        videoviewpager = findViewById(R.id.video_view_pager);
        // get request
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://createdinam.com/wp-json/wl/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getPostFromWeb = retrofit.create(GetPostFromWeb.class);
        getVideoPost();
    }
    private void getVideoPost() {
        Call<List<PostModel>> call = getPostFromWeb.getPost();
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    Log.d("status",""+response);
                    List<PostModel> data = response.body();
                    videoviewpager.setAdapter(new VideoAdapter(data,FunnyStatusActivity.this));
                }else{
                    Log.d("error_inner", "" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("error_main", "" + t.getStackTrace());
            }
        });
    }
}