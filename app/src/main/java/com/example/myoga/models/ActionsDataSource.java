package com.example.myoga.models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import static com.example.myoga.models.DatabaseConnection.getMyogaHomeLinks;

public class ActionsDataSource {
    private static List<Action> actions = new ArrayList<>();
    public static class Action {
        String image;
        int index;
        String url;

        Action() {
        }

        public Action(String image, int index, String url) {
            this.image = image;
            this.index = index;
            this.url = url;
        }

        public int getIndex() {
            return index;
        }

        public String getUrl() {
            return url;
        }

        public String getImage() {
            return image;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setImage(String imageURL) {
            this.image = imageURL;
        }

        public void setUrl(String addressUrl) {
            this.url = addressUrl;
        }

    }

    public static List<Action> getActions() {
        return actions;
    }

    public static void loadLinks() {
        new Thread(() -> {
            getMyogaHomeLinks().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Action act = snapshot.getValue(Action.class);
                    if (act != null) {
                        actions.add(act);
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }).start();
    }
}
