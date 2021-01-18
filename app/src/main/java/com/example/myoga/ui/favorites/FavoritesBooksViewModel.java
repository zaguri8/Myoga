package com.example.myoga.ui.favorites;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.models.BooksSourceData;
import com.example.myoga.models.YogaDataSource;

import java.util.ArrayList;

public class FavoritesBooksViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<BooksSourceData.Book>> FavBooks;

    public FavoritesBooksViewModel(Application application) {
        super(application);
        FavBooks = new MutableLiveData<>();
        BooksSourceData sourceData = new BooksSourceData(application.getApplicationContext(), BookRecyclerViewAdapter.RV_TYPE.FAV);
        sourceData.addItemsFromDatabase(FavBooks);
    }
    public LiveData<ArrayList<BooksSourceData.Book>> getFavBooks() {
        return FavBooks;
    }

}