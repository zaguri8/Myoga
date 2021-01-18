package com.example.myoga.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myoga.models.DailyQuateDataSource;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<DailyQuateDataSource.Quate> dailyQuate;
    public static DailyQuateDataSource.Quate cachedQuate;

    public HomeViewModel() {
        dailyQuate = new MutableLiveData<>();
        DailyQuateDataSource.loadDailyQuate(dailyQuate);
    }

    public MutableLiveData<DailyQuateDataSource.Quate> getDailyQuate() {
        return dailyQuate;
    }

    public DailyQuateDataSource.Quate getCachedQuate() {
        return cachedQuate;
    }

    public static void setCachedQuate(DailyQuateDataSource.Quate quate) {
        cachedQuate = quate;
    }
}