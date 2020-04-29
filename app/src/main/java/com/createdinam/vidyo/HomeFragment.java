package com.createdinam.vidyo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.customloder.CustomLoader;
import com.createdinam.vidyo.global.GlobalInit;
import com.createdinam.vidyo.global.Meme;
import com.createdinam.vidyo.global.SliderAdapter;
import com.createdinam.vidyo.global.SliderModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment implements Filterable {
    RequestQueue requestQueue;
    Context context;
    RecyclerView recyclerView;
    ArrayList<Meme> memes = new ArrayList<Meme>();
    JSONObject obj = null;
    private CustomLoader mCustomLoader;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set layout
        final View view = inflater.inflate(R.layout.home_layout, container, false);
        final FragmentActivity activity = getActivity();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mCustomLoader = new CustomLoader(activity);
        recyclerView = view.findViewById(R.id.slider_list_view);
        recyclerView.setLayoutManager(manager);
        // request data
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //onCreateNewsFileds(GlobalInit.MEMES_URL);
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    private void onCreateNewsFileds(String post_url) {
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
                                recyclerView.setAdapter(new SliderAdapter(getActivity(), memes));
                                mCustomLoader.setCancelAlertDailog();
                            }else{
                                Log.d("total_memes",""+memes.size());
                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.d("error ", ex.getMessage());
                }

                try {

                } catch (Exception ex) {
                    Log.d("error ", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error-response ", "" + error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
