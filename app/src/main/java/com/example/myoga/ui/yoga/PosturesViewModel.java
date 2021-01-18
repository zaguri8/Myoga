package com.example.myoga.ui.yoga;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.models.YogaDataSource;

import java.util.ArrayList;

public class PosturesViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<YogaDataSource.YogaAsana>> allAsanas;

    public PosturesViewModel(Application application) {
        super(application);
        allAsanas = new MutableLiveData<>();
        YogaDataSource sourceData = new YogaDataSource(application.getApplicationContext(), AsanaRecyclerViewAdapter.RV_TYPE.ALL);
        sourceData.addItemsFromJSON(allAsanas);
    }

    public LiveData<ArrayList<YogaDataSource.YogaAsana>> getAllAsanas() {
        return allAsanas;
    }

}