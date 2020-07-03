package com.createdinam.vidyo.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media2.MediaPlaylistAgent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    int id;
    String name, path;
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
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        holder.setDataPlayer(emails.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        if (emails != null) {
            return emails.size();
        }
        return 0;
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        CircleImageView post_title_images;
        ImageView share_btn,play_pause_btn;
        TextView title_view;
        VideoView Player;
        ProgressBar progressBar;
        MediaController mc;
        // Animation
        Animation animFadein;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            post_title_images = itemView.findViewById(R.id.post_title_images);
            share_btn = itemView.findViewById(R.id.share_btn);
            title_view = itemView.findViewById(R.id.title_view);
            Player = itemView.findViewById(R.id.post_player);
            progressBar = itemView.findViewById(R.id.progressbar);
            play_pause_btn = itemView.findViewById(R.id.play_pause_btn);
        }

        public void setPause() {
            play_pause_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        void setDataPlayer(final PostModel postModel, final Context mContext) {
            id = postModel.getId();
            name = postModel.getTitle();
            title_view.setText(name);
            Glide.with(mContext)
                    .load(postModel.getFeatured_image().getLarge()) // image url
                    .placeholder(R.drawable.country) // any placeholder to load at start
                    .error(R.drawable.country)  // any image in case of error
                    .centerCrop()
                    .into(post_title_images);  // resizing
            // rotate image
            RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(5000);
            rotate.setRepeatMode(Animation.INFINITE);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setInterpolator(new LinearInterpolator());
            post_title_images.startAnimation(rotate);
            // hide image view
            post_title_images.setVisibility(View.VISIBLE);
            // share btn
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Vidyo");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Application Link : " + postModel.getUpload_videos());
                    sendIntent.setType("text/plain");
                    Intent.createChooser(sendIntent, "Share via");
                    mContext.startActivity(sendIntent);
                }
            });
            // set player
            Player.setVideoPath(postModel.getUpload_videos());
            // set controller
            MediaController mediaController = new MediaController(mContext);
            mediaController.setAnchorView(Player);
            Player.setMediaController(mediaController);
            // set progress bar
            progressBar.setVisibility(View.VISIBLE);
            Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            mc = new MediaController(mContext);
                            Player.setMediaController(mc);
                            mc.setAnchorView(Player);
                            ((ViewGroup) mc.getParent()).removeView(mc);
                            mc.setVisibility(View.INVISIBLE);
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                    mp.start();
                }
            });
            Player.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    play_pause_btn.setVisibility(View.VISIBLE);
                    Player.pause();
                    return true;
                }
            });
            play_pause_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play_pause_btn.setVisibility(View.INVISIBLE);
                    Player.start();
                }
            });
            Player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            Player.setFocusable(true);
        }
    }
}
