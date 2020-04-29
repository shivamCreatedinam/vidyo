package com.createdinam.vidyo.global;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSlider extends RecyclerView.Adapter {
    Context mContext;
    List<SliderModel.DataBean.MemesBean> memesBean;

    public AdapterSlider(Context mContext, List<SliderModel.DataBean.MemesBean> memesBean) {
        this.mContext = mContext;
        this.memesBean = memesBean;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
