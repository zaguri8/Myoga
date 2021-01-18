package com.example.myoga.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.VideoRecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import static com.example.myoga.models.DatabaseConnection.getMyogaVideos;

public class VideosDataSource {

    public VideosDataSource() {

    }

    public static class Video {
        String url;
        String videoName;

        public Video(String url, String videoName) {
            this.url = url;
            this.videoName = videoName;
        }

        public Video() {
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getUrl() {
            return url;
        }

        public String getVideoName() {
            return videoName;
        }
    }

    public static void loadVideos(VideoRecyclerViewAdapter.RV_TYPE type, MutableLiveData<ArrayList<Video>> callback, Context context) {
        // User has connection! -> load videos from database
        if (NetworkUtils.isNetworkConnected(context)) {
            switch (type) {
                case WATCH:
                    ArrayList<Video> watchVideos = new ArrayList<>();
                    getMyogaVideos().child("WATCH").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            callback.postValue(watchVideos);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    getMyogaVideos().child("WATCH").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Video v = snapshot.getValue(Video.class);
                            watchVideos.add(v);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case BREATH:
                    ArrayList<Video> breathVideos = new ArrayList<>();
                    getMyogaVideos().child("BREATH").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            callback.postValue(breathVideos);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    getMyogaVideos().child("BREATH").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Video v = snapshot.getValue(Video.class);
                            breathVideos.add(v);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
            }
        } else {
            Toast.makeText(context, "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
        }
    }
}