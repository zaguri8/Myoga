package com.example.myoga.models;


import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class User implements Serializable {
    String email;
    String userId;

    public User() {
        try {
            this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            this.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            this.userId = "NULL"; // TODO: Make this more efficient
            this.email = "NULL@gmail.com"; // TODO: Make this more efficient
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
