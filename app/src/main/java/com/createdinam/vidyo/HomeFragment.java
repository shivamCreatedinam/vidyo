package com.createdinam.vidyo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.customloder.CustomLoader;
import com.createdinam.vidyo.global.Meme;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.util.Log.d;


public class HomeFragment extends Fragment implements Filterable, View.OnClickListener {
    RequestQueue requestQueue;
    Context mContext;
    FloatingActionButton feedBtn;
    ArrayList<Meme> memes = new ArrayList<Meme>();
    JSONObject obj = null;
    ListView mListView;
    private CustomLoader mCustomLoader;
    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set layout
        final View view = inflater.inflate(R.layout.home_layout, container, false);
        final FragmentActivity activity = getActivity();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mCustomLoader = new CustomLoader(activity);
        feedBtn = view.findViewById(R.id.upload_new_feed);

        mListView = (ListView)view.findViewById(R.id.listView_items);
        // request data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        mListView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.upload_new_feed:
                Log.d("clicked","feed btn");
                break;
        }
    }
}
