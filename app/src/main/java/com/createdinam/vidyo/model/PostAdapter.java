package com.createdinam.vidyo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createdinam.vidyo.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    int id;
    String name,path;
    List<PostModel> emails;
    Context mContext;

    public PostAdapter(List<PostModel> emails, Context mContext) {
        this.emails = emails;
        this.mContext = mContext;
    }

    public void setPostAdapter(List<PostModel> emails) {
        this.emails = emails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_items, parent, false);
        return new PostHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        id = emails.get(position).getId();
        name = emails.get(position).getTitle();
        holder.title_view.setText(name);
        Glide.with(mContext)
                .load(emails.get(position).getFeatured_image().getLarge()) // image url
                .placeholder(R.drawable.country) // any placeholder to load at start
                .error(R.drawable.country)  // any image in case of error
                .centerCrop()
                .into(holder.post_title_images);  // resizing
    }

    @Override
    public int getItemCount() {
        if (emails != null) {
            return emails.size();
        }
        return 0;
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView post_title_images;
        TextView title_view;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            post_title_images = itemView.findViewById(R.id.post_title_images);
            title_view = itemView.findViewById(R.id.title_view);
        }
    }
}
