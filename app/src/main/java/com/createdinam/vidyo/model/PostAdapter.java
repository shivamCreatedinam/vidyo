package com.createdinam.vidyo.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createdinam.vidyo.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> implements Player.EventListener {
    int id;
    String name,path;
    List<PostModel> emails;
    Context mContext;
    SimpleExoPlayer exoPlayer;
    public PostAdapter(List<PostModel> emails, Context mContext) {
        this.emails = emails;
        this.mContext = mContext;
        initPlayer();
    }

    private void initPlayer() {

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
        // hide image view
        holder.post_title_images.setVisibility(View.GONE);
        // share btn
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,name);
                sendIntent.setType("text/plain");
                Intent.createChooser(sendIntent,"Share via");
                mContext.startActivity(sendIntent);
            }
        });
        // set player
        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        holder.simpleExoPlayerView.setShowBuffering(true);
        exoPlayer.addListener(this);
        holder.simpleExoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        if (exoPlayer.isLoading()){
            Toast.makeText(mContext, "Loading..", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Playing..", Toast.LENGTH_SHORT).show();
        }
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "VideoPlayer"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(emails.get(position).getUpload_videos()));
        exoPlayer.prepare(videoSource);

    }

    @Override
    public int getItemCount() {
        if (emails != null) {
            return emails.size();
        }
        return 0;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                break;
            case Player.STATE_ENDED:
                Log.d(TAG, "onPlayerStateChanged: Video ended.");
                break;
            case Player.STATE_IDLE:
                Log.d(TAG, "onPlayerStateChanged: Video Idle state.");
                break;
            case Player.STATE_READY:
                Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView post_title_images,share_btn;
        TextView title_view;
        PlayerView simpleExoPlayerView;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            post_title_images = itemView.findViewById(R.id.post_title_images);
            share_btn = itemView.findViewById(R.id.share_btn);
            title_view = itemView.findViewById(R.id.title_view);
            simpleExoPlayerView = itemView.findViewById(R.id.post_player);
        }
    }
}
