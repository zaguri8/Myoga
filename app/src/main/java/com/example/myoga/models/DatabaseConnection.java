package com.example.myoga.models;


import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myoga.models.ActionsDataSource.loadLinks;
import static com.example.myoga.models.VideosDataSource.loadVideos;

public class DatabaseConnection {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference dbRef = database.getReference();
    private static DatabaseReference myogaUsers = dbRef.child("users");
    private static DatabaseReference myogaFavorites = dbRef.child("favorites");
    private static DatabaseReference myogaHomeLinks = dbRef.child("links");
    private static DatabaseReference myogaVideos = dbRef.child("videos");
    private static DatabaseReference myogaQuate = dbRef.child("dailyQuate");
    private static DatabaseReference myogaDVideo = dbRef.child("dVideo");
    private static DatabaseReference myogaDVideo2 = dbRef.child("dVideo2");

    public static DatabaseReference getDbRef() {
        return dbRef;
    }

    public static DatabaseReference getMyogaFavorites() {
        return myogaFavorites;
    }

    public static DatabaseReference getMyogaQuate() {
        return myogaQuate;
    }

    public static DatabaseReference getMyogaDVideo() {
        return myogaDVideo;
    }

    public static DatabaseReference getMyogaDVideo2() {
        return myogaDVideo2;
    }

    public static DatabaseReference getMyogaVideos() {
        return myogaVideos;
    }

    public static DatabaseReference getMyogaHomeLinks() {
        return myogaHomeLinks;
    }

    public static DatabaseReference getMyogaUsers() {
        return myogaUsers;
    }

    public static void loadData(Context context) {
        new Thread(ActionsDataSource::loadLinks).start();
    }
}

