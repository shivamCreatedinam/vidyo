package com.createdinam.vidyo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.customloder.CustomLoader;
import com.createdinam.vidyo.global.UnsafeOkHttpClient;
import com.createdinam.vidyo.model.GetPostFromWeb;
import com.createdinam.vidyo.model.PostAdapter;
import com.createdinam.vidyo.model.PostModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static String user_type;
    private static Boolean status;
    private static final String API_URL = "https://createdinam.com/wp-json/wl/v1/";
    private static final String URL = "https://createdinam.com/wp-json/wl/v1/service/";
    RequestQueue requestQueue;
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    SweetAlertDialog sweetAlertDialog;
    private CustomLoader mCustomLoader;
    ViewPager2 video_list_items;
    Retrofit retrofit;
    List<PostModel> mList;
    GetPostFromWeb getPostFromWeb;
    PostAdapter postAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // view fullscreen with status bar
//        Window window = getWindow();
//        WindowManager.LayoutParams winParams = window.getAttributes();
//        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        window.setAttributes(winParams);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // init navigation
        SmoothBottomBar bottomNavigation = findViewById(R.id.bottomBar);
        bottomNavigation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 1) {
                    startActivity(new Intent(MainActivity.this, FunnyStatusActivity.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            }
        });
        // init
        swipeRefreshLayout = findViewById(R.id.pull_down_to_refresh);
        video_list_items = findViewById(R.id.video_list_items);
        postAdapter = new PostAdapter(mList, this);
        // setup
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        status = sharedPreferences.getBoolean("status", true);
        user_type = sharedPreferences.getString("user_type", "");
        if (status) {
            // status
            //getVideoPostByVolley();
            setupTokenForNotification("DcpkdGEBlXZCAkW3zhLA5faUmKD2");
        }
        // loader
        mCustomLoader = new CustomLoader(MainActivity.this);
        mCustomLoader.startLoadingAlertBox();
        // request data
        requestQueue = Volley.newRequestQueue(MainActivity.this.getApplicationContext());
        // get request
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // start loading
        getPostFromWeb = retrofit.create(GetPostFromWeb.class);
        getVideoPost();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVideoPost();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showSnackBar() {

    }

    private void getVideoPost() {
        Call<List<PostModel>> call = getPostFromWeb.getPost();
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    Log.d("status", "" + response);
                    List<PostModel> data = response.body();
                    postAdapter.setPostAdapter(data);
                    video_list_items.setAdapter(postAdapter);
                    mCustomLoader.setCancelAlertDailog();
                } else {
                    Log.d("error_inner", "" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("error_main", " " + t.getMessage());
                Log.d("error_main", " " + call.request());
                Log.d("error_main", " " + t.getStackTrace());
                Log.d("error_main", " " + t.getSuppressed() + " | " + t.getLocalizedMessage());
            }
        });
    }

    private void getVideoPostByVolley() {
        StringRequest tokenRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data", "" + response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error.getLocalizedMessage());
                Log.d("error", "" + error.getMessage());
                Log.d("error", "" + error.getNetworkTimeMs());
                Log.d("error", "" + error.getStackTrace());
            }
        });
        requestQueue.add(tokenRequest);
    }

    private void setupTokenForNotification(final String token) {
        final String API_SECRET_KEY = "^SQMht!VMQZ0%v%oMp%23@7XJd";
        String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        final String versionRelease = Build.VERSION.RELEASE;

//        StringRequest tokenRequest = new StringRequest(Request.Method.POST, "https://createdinam.com/wp-json/pd/fcm/subscribe/", new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("data",""+response);
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("notification",""+error.getLocalizedMessage() +" | "+error.getMessage());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("api_secret_key",API_SECRET_KEY);
//                param.put("user_email","sonishivam457@gmail.com");
//                param.put("device_token",token);
//                param.put("subscribed","Letest News");
//                param.put("device_name",model);
//                param.put("os_version", versionRelease);
//                return param;
//            }
//        };

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("api_secret_key", API_SECRET_KEY);
            object.put("user_email", "sonishivam457@gmail.com");
            object.put("device_token", token);
            object.put("subscribed", "Letest News");
            object.put("device_name", model);
            object.put("os_version", versionRelease);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest JsonToken = new JsonObjectRequest(Request.Method.POST, "https://createdinam.com/wp-json/pd/fcm/subscribe/", object, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("data", "" + response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("notification", "" + error.getLocalizedMessage() + " | " + error.getMessage());
            }
        });
        requestQueue.add(JsonToken);
    }

    private void setNotificationEnable() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        Call<ResponseBody> call = getPostFromWeb.notificationSetup("createdinam@gmail.com", "917487541105", "personal_user", model, "" + version);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "response :" + response.isSuccessful(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEventInFloatingAction() {
        Toast.makeText(this, "You Clicked!", Toast.LENGTH_SHORT).show();
    }

    public void showNotification(String title, String message) {
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
