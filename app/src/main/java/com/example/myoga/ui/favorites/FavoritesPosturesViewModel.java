package com.example.myoga.ui.favorites;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.models.YogaDataSource;

import java.util.ArrayList;

public class FavoritesPosturesViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<YogaDataSource.YogaAsana>> FavAsanas;

    public FavoritesPosturesViewModel(Application application) {
        super(application);
        FavAsanas = new MutableLiveData<>();
        YogaDataSource sourceData = new YogaDataSource(application.getApplicationContext(), AsanaRecyclerViewAdapter.RV_TYPE.FAV);
        sourceData.addItemsFromDatabase(FavAsanas);
    }

    public LiveData<ArrayList<YogaDataSource.YogaAsana>> getFavAsanas() {
        return FavAsanas;
    }

}