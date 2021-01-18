package com.example.myoga.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.ui.home.HomeViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.myoga.models.DatabaseConnection.getMyogaQuate;

public class DailyQuateDataSource {
    public static class Quate {
        String author;
        String text;
        String date;

        public Quate(String author, String date, String text) {
            this.author = author;
            this.date = date;
            this.text = text;
        }

        public Quate() {

        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAuthor() {
            return author;
        }

        public String getText() {
            return text;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static void loadDailyQuate(MutableLiveData<Quate> callback) {
        new Thread(() -> {
            getMyogaQuate().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DailyQuateDataSource.Quate q = snapshot.getValue(DailyQuateDataSource.Quate.class);
                    HomeViewModel.setCachedQuate(q);
                    callback.postValue(q);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }).start();
    }
}
