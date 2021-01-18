package com.example.myoga.ui.yoga;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.VideoRecyclerViewAdapter;
import com.example.myoga.models.VideosDataSource;

import java.util.ArrayList;

public class BreathingViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<VideosDataSource.Video>> videos;

    public BreathingViewModel(Application application) {
        super(application);
        videos = new MutableLiveData<>();
        VideosDataSource.loadVideos(VideoRecyclerViewAdapter.RV_TYPE.BREATH, videos, application.getApplicationContext());
    }
    public MutableLiveData<ArrayList<VideosDataSource.Video>> getVideos() {
        return videos;
    }
}