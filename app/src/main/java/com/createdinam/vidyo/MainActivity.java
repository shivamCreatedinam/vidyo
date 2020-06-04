package com.createdinam.vidyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.customloder.CustomLoader;
import com.createdinam.vidyo.global.Meme;
import com.createdinam.vidyo.global.SliderAdapter;
import com.createdinam.vidyo.model.ServicesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static String user_type;
    private static Boolean status;
    RequestQueue requestQueue;
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    SweetAlertDialog sweetAlertDialog;
    BottomNavigationView mBottomNavigationView;
    SliderAdapter sliderAdapter;
    ArrayList<Meme> memes = new ArrayList<Meme>();
    JSONObject obj = null;
    private CustomLoader mCustomLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mBottomNavigationView =  findViewById(R.id.bottom_layout_navigation);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.services:
                        break;
                    case R.id.contact:
                        selectedFragment = new NewsFragment();
                        break;
                    case R.id.about:
                        selectedFragment = new SettingFragment();
                        break;
                }
                return true;
            }
        });
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        status = sharedPreferences.getBoolean("status", true);
        user_type = sharedPreferences.getString("user_type", "");
        if (status){
            // status
        }
        mCustomLoader = new CustomLoader(MainActivity.this);
        // request data
        requestQueue = Volley.newRequestQueue(MainActivity.this.getApplicationContext());
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
                        getLogoutActivity();
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

   /* private void onCreateNewsFileds(String post_url) {
        mCustomLoader.startLoadingAlertBox();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, post_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    obj = new JSONObject(response);
                    int maxLogSize = 1000;
                    for (int i = 0; i <= response.length() / maxLogSize; i++) {
                        int start1 = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > response.length() ? response.length() : end;
                        //Log.d("data", response.substring(start1, end));
                    }
                    if (obj.get("success").toString().matches("true")) {
                        memes.clear();
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("memes");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Meme m = new Meme();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                m.setId(jsonObject.getString("id"));
                                m.setName(jsonObject.getString("name"));
                                m.setUrl(jsonObject.getString("url"));
                                m.setWidth(Integer.valueOf(jsonObject.getString("width")));
                                m.setHeight(Integer.valueOf(jsonObject.getString("height")));
                                m.setBoxCount(Integer.valueOf(jsonObject.getString("box_count")));

                                memes.add(m);
                            }
                            if (memes.size() > 0) {
                                Log.d("total_memes",""+memes.size());
                                sliderAdapter = new SliderAdapter(MainActivity.this,memes);
                                sliderAdapter.notifyDataSetChanged();
                                mCustomLoader.setCancelAlertDailog();
                            }else{
                                Log.d("total_memes",""+memes.size());
                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.d("error-", ex.getMessage());
                }

                try {

                } catch (Exception ex) {
                    Log.d("error_", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error-response ", "" + error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    } */
}
