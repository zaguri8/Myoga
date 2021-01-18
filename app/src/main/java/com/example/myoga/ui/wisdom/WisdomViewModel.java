package com.example.myoga.ui.wisdom;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.models.YogaDataSource;

import java.util.ArrayList;

public class WisdomViewModel extends AndroidViewModel {
    private MutableLiveData<String> mText;

    public WisdomViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is Wisdom fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}