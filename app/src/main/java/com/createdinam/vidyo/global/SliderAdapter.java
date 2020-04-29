package com.createdinam.vidyo.global;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.createdinam.vidyo.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MemeHolder> {

    private Context mContext;
    private ArrayList<Meme> mBeans = new ArrayList<Meme>();

    public SliderAdapter(Context mContext,ArrayList<Meme> Beans) {
        this.mContext = mContext;
        this.mBeans = Beans;
    }

    @NonNull
    @Override
    public MemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View mView = inflater.inflate(R.layout.slider_list_items,parent,false);
        return new MemeHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeHolder holder, int position) {
        String name = mBeans.get(position).getName();
        String images = mBeans.get(position).getUrl();
        Log.d("memes_name",""+name);
        // set data
        holder.memeName.setText(name);
        Glide.with(holder.memeImage).load(images).into(holder.memeImage).getView();
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    public class MemeHolder extends RecyclerView.ViewHolder{
        ImageView memeImage;
        TextView memeName;
        public MemeHolder(@NonNull View itemView) {
            super(itemView);
            memeImage = itemView.findViewById(R.id.slider_images);
            memeName = itemView.findViewById(R.id.slider_name);
        }
    }

}
