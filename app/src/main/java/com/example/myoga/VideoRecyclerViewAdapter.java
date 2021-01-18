package com.example.myoga;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.models.Utils;
import com.example.myoga.models.VideosDataSource;

import java.util.ArrayList;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final RV_TYPE RvType;
    private ArrayList<VideosDataSource.Video> watchVideos;
    private ArrayList<VideosDataSource.Video> breathVideos;

    public enum RV_TYPE {
        BREATH("BREATH"),
        WATCH("WATCH");
        private final String stringVal;

        RV_TYPE(String stringVal) {
            this.stringVal = stringVal;
        }

        public String getStringVal() {
            return stringVal;
        }
    }

    public VideoRecyclerViewAdapter(Context context, RV_TYPE type, ArrayList<VideosDataSource.Video> videos) {
        this.context = context;
        this.RvType = type;
        switch (type) {
            case WATCH:
                watchVideos = videos;
                break;
            case BREATH:
                breathVideos = videos;
                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We need: View v =  How to inflate XMl file.
        // Create new ViewHolder.
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.video_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String videoName = "";
        String videoURL = "";
        switch (RvType) {
            case BREATH:
                videoName = breathVideos.get(position).getVideoName();
                videoURL = breathVideos.get(position).getUrl();
                break;
            case WATCH:
                videoName = watchVideos.get(position).getVideoName();
                videoURL = watchVideos.get(position).getUrl();
                break;
        }
        holder.webView_text.setText(videoName);
        Utils.loadEmbedVideo(videoURL, holder.webView, context, 300, 200);
    }

    public int getItemCount() {
        switch (RvType) {
            case BREATH:
                return Math.max(breathVideos.size(), 0);
            case WATCH:
                return Math.max(watchVideos.size(), 0);
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView webView_text;
        WebView webView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            webView_text = itemView.findViewById(R.id.webview_text);
            webView = itemView.findViewById(R.id.webview);
        }
    }

}
