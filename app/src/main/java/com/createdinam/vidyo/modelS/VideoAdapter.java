package com.createdinam.vidyo.modelS;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.vidyo.MainActivity;
import com.createdinam.vidyo.R;
import com.createdinam.vidyo.model.PostModel;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {

    List<PostModel> videoList;
    Context mContext;
    public VideoAdapter(List<PostModel> videoList,Context context) {
        this.videoList = videoList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new videoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {
        holder.setVideoData(videoList.get(position),mContext);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class videoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView videotitle;
        ProgressBar progressBar;
        MediaController mc;
        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            videotitle = itemView.findViewById(R.id.video_name);
            progressBar = itemView.findViewById(R.id.progressbar);
        }

        void setVideoData(PostModel mediaObject, final Context mContext) {
            videotitle.setText(mediaObject.getTitle());
            videoView.setVideoPath(mediaObject.getUpload_videos());
            videoView.setMediaController(new MediaController(mContext));
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            mc = new MediaController(mContext);
                            videoView.setMediaController(mc);
                            mc.setAnchorView(videoView);

                            ((ViewGroup) mc.getParent()).removeView(mc);
                            mc.setVisibility(View.INVISIBLE);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    mp.start();
//                    float videoratio = mp.getVideoWidth() / (float) mp.getVideoHeight();
//                    float screenratioheight = videoView.getWidth() / (float) videoView.getHeight();
//                    float scale = videoratio / screenratioheight;
//                    if (scale >= 1f) {
//                        videoView.setScaleX(scale);
//                    } else {
//                        videoView.setScaleY(1f / scale);
//                    }
                }
            });

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mc != null) {
                        mc.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mc.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                    }
                    return false;
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }
}
