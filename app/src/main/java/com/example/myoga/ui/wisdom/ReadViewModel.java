package com.example.myoga.ui.wisdom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.models.BooksSourceData;

import java.util.ArrayList;

public class ReadViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<BooksSourceData.Book>> allBooks;

    public ReadViewModel(Application application) {
        super(application);
        allBooks = new MutableLiveData<>();
        BooksSourceData sourceData = new BooksSourceData(application.getApplicationContext(), BookRecyclerViewAdapter.RV_TYPE.ALL);
        sourceData.addItemsFromJSON(allBooks);
    }

    public LiveData<ArrayList<BooksSourceData.Book>> getAllBooks() {
        return allBooks;
    }

}