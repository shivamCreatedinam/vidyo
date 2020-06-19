package com.createdinam.vidyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.customloder.CustomLoader;
import com.createdinam.vidyo.global.CurvedBottomNavigationView;
import com.createdinam.vidyo.global.Meme;
import com.createdinam.vidyo.global.SliderAdapter;
import com.createdinam.vidyo.global.UnsafeOkHttpClient;
import com.createdinam.vidyo.model.GetPostFromWeb;
import com.createdinam.vidyo.model.PostAdapter;
import com.createdinam.vidyo.model.PostModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static SharedPreferences sharedPreferences;
    private static String user_type;
    private static Boolean status;
    RequestQueue requestQueue;
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    SweetAlertDialog sweetAlertDialog;
    private CustomLoader mCustomLoader;
    FloatingActionButton mFloatingActionButton;
    RecyclerView video_list_items;
    Retrofit retrofit;
    List<PostModel> mList;
    GetPostFromWeb getPostFromWeb;
    PostAdapter postAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init navigation
        CurvedBottomNavigationView mView = findViewById(R.id.customBottomBar);
        mView.inflateMenu(R.menu.bottom_navigation_items);
        //mView.setSelectedItemId(R.id.action_schedules);
        mView.setOnNavigationItemSelectedListener(MainActivity.this);
        // init
        swipeRefreshLayout = findViewById(R.id.pull_down_to_refresh);
        mFloatingActionButton = findViewById(R.id.upload_new_feed);
        video_list_items = findViewById(R.id.video_list_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        video_list_items.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(mList,this);
        // setup
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        status = sharedPreferences.getBoolean("status", true);
        user_type = sharedPreferences.getString("user_type", "");
        if (status){
            // status
        }
        // loader
        mCustomLoader = new CustomLoader(MainActivity.this);
        // request data
        requestQueue = Volley.newRequestQueue(MainActivity.this.getApplicationContext());
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEventInFloationgAction();
            }
        });
        // get request
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://createdinam.com/wp-json/wl/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // start loading
        mCustomLoader.startLoadingAlertBox();
        getPostFromWeb = retrofit.create(GetPostFromWeb.class);
        getVideoPost();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getVideoPost() {
        Call<List<PostModel>> call = getPostFromWeb.getPost();
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    Log.d("status",""+response);
                    List<PostModel> data = response.body();
                    postAdapter.setPostAdapter(data);
                    video_list_items.setAdapter(postAdapter);
                    //setNotificationEnable();
                    mCustomLoader.setCancelAlertDailog();
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

    private void setNotificationEnable() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        Call<ResponseBody> call = getPostFromWeb.notificationSetup("createdinam@gmail.com","917487541105","personal_user",model,""+version);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "response :"+response.isSuccessful(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEventInFloationgAction() {
        Toast.makeText(this, "You Clicked!", Toast.LENGTH_SHORT).show();
    }

    public void showNotification(String title,String message){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(title)
                        .setContentText(message);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void getLogoutActivity() {
        sweetAlertDialog = new SweetAlertDialog(Objects.requireNonNull(MainActivity.this), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Close Application");
        sweetAlertDialog.setContentText("Do you really want to Close ?");
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                finish();
            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.text_color));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.services:
                Toast.makeText(this, "Service", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
