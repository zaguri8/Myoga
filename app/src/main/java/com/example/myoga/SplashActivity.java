package com.example.myoga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends Activity {

    /**
     * Duration of wait
     **/
    private TextView welcome;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.addAuthStateListener(firebaseAuth -> {

            FirebaseUser currentUser = auth.getCurrentUser();
            welcome = findViewById(R.id.welcometv);
            if (currentUser == null) {
                //go login
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                //update ui (say hello to the user)
                welcome.setText("Welcome, " + currentUser.getEmail());
            }
        });
    }
}