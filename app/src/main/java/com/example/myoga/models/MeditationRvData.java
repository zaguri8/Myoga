package com.example.myoga.models;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public class MeditationRvData {
    private String title;
    private String desc;
    private Fragment destination;
    private Drawable itemImage;

    public MeditationRvData(String title, String desc, Fragment destination, Drawable itemImage) {
        this.title = title;
        this.desc = desc;
        this.destination = destination;
        this.itemImage = itemImage;
    }

    public String getTitle() {
        return  title;
    }
    public String getDesc(){
        return desc;
    }
    public Fragment getDestination() {
        return destination;
    }
    public Drawable getItemImage() {
        return itemImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDestination(Fragment destination) {
        this.destination = destination;
    }

    public void setItemImage(Drawable itemImage) {
        this.itemImage = itemImage;
    }

    @Override
    public String toString() {
        return "MeditationRvData{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", destination=" + destination +
                ", itemImage=" + itemImage +
                '}';
    }
}
