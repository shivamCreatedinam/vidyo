package com.createdinam.vidyo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;

import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements Filterable, View.OnClickListener {
    RequestQueue requestQueue;
    Context mContext;
    RecyclerView recyclerView;
    ArrayList<Meme> memes = new ArrayList<Meme>();
    JSONObject obj = null;
    private CustomLoader mCustomLoader;
    // sinch clint
    // button
    Button btnCalling;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set layout
        final View view = inflater.inflate(R.layout.home_layout, container, false);
        final FragmentActivity activity = getActivity();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        mCustomLoader = new CustomLoader(activity);
        recyclerView = view.findViewById(R.id.slider_list_view);
        btnCalling = view.findViewById(R.id.calling_me);
        btnCalling.setOnClickListener(this);
        recyclerView.setLayoutManager(manager);
        // request data
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //onCreateNewsFileds();
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    public void onCreateNewsFileds() {
        // Instantiate a SinchClient using the SinchClientBuilder.
        android.content.Context context = getActivity().getApplicationContext();
        final SinchClient sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey("1cc9f561-d2c4-4a1b-bad8-d60f30e4fa5a")
                .applicationSecret("8Z3QZ8Y6w06cll00HE0NeQ==")
                .environmentHost("clientapi.sinch.com")
                .userId("151899")
                .build();
        // Specify the client capabilities.
        sinchClient.setSupportCalling(true);
        CallClient callClient = sinchClient.getCallClient();
        callClient.callPhoneNumber("+919999870918");
    }

    private void callingClient(){

    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calling_me:
                Toast.makeText(mContext, "This is called", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
