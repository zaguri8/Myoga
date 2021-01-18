package com.example.myoga.ui.wisdom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.VideoRecyclerViewAdapter;
import com.example.myoga.models.VideosDataSource;

import java.util.ArrayList;

public class WatchViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<VideosDataSource.Video>> videos;

    public WatchViewModel(Application application) {
        super(application);
        videos = new MutableLiveData<>();
        VideosDataSource.loadVideos(VideoRecyclerViewAdapter.RV_TYPE.WATCH, videos, application.getApplicationContext());
    }

    public MutableLiveData<ArrayList<VideosDataSource.Video>> getVideos() {
        return videos;
    }
}