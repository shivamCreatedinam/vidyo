package com.createdinam.vidyo.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class GlobalInit {
    public static final String POST_URL = "http://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=298a504b67144bd6bb2438db3c7d70fb";
    public static final String MEMES_URL = "https://api.imgflip.com/get_memes";
    public static final String NEWS_URL = "http://newsapi.org/v2/top-headlines";
    private static GlobalInit globalInit = new GlobalInit();
    private static Context context;

    public static GlobalInit getInstance(Context context){
        context = context.getApplicationContext();
        return globalInit;
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}
