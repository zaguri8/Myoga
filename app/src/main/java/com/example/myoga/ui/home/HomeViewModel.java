package com.example.myoga.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myoga.models.DailyQuateDataSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import static com.example.myoga.models.DatabaseConnection.getMyogaDVideo;
import static com.example.myoga.models.DatabaseConnection.getMyogaDVideo2;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<DailyQuateDataSource.Quate> dailyQuate;
    private MutableLiveData<String> dailyVideoUrl;
    private MutableLiveData<String> dailyVideoUrl2;
    public static DailyQuateDataSource.Quate cachedQuate;

    public HomeViewModel() {
        dailyQuate = new MutableLiveData<>();
        dailyVideoUrl = new MutableLiveData<>();
        dailyVideoUrl2 = new MutableLiveData<>();
        loadHomeDailyVideos(dailyVideoUrl, dailyVideoUrl2);
        DailyQuateDataSource.loadDailyQuate(dailyQuate);
    }

    public MutableLiveData<DailyQuateDataSource.Quate> getDailyQuate() {
        return dailyQuate;
    }

    public MutableLiveData<String> getDailyVideoUrl1() {
        return dailyVideoUrl;
    }

    public MutableLiveData<String> getDailyVideoUrl2() {
        return dailyVideoUrl2;
    }

    public DailyQuateDataSource.Quate getCachedQuate() {
        return cachedQuate;
    }

    public static void setCachedQuate(DailyQuateDataSource.Quate quate) {
        cachedQuate = quate;
    }

    public static void loadHomeDailyVideos(MutableLiveData<String> videoCallBack, MutableLiveData<String> audioCallback) {
        new Thread(() -> {
            getMyogaDVideo().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String videoUrl = (String) snapshot.getValue();
                    videoCallBack.postValue(videoUrl);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            getMyogaDVideo2().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String videoUrl = (String) snapshot.getValue();
                    audioCallback.postValue(videoUrl);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }).start();
    }
}