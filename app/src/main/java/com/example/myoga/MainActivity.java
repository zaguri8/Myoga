package com.example.myoga;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.myoga.models.DatabaseConnection;
import com.example.myoga.models.NetworkChangeReceiver;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.ServiceManager;
import com.example.myoga.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static com.example.myoga.models.ActionsDataSource.getActions;
import static com.example.myoga.models.Utils.openWebPage;


public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    User user;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_wisdom, R.id.navigation_yoga, R.id.navigation_yoga_breathing, R.id.navigation_yoga_postures, R.id.navigation_yoga_meditation,
                R.id.navigation_wisdom_listen, R.id.navigation_wisdom_watch, R.id.navigation_wisdom_read, R.id.navigation_favorites, R.id.navigation_favorites_books, R.id.navigation_favorites_postures)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        new Thread(() -> {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(new NetworkChangeReceiver(), intentFilter);
            if (firebaseUser != null) {
                user = new User();
            }
            // Changes status bar & action bar
            onCreateViewCustomized();
        }).start();
    }

    public void homeAction1(View view) {
        if (getActions().size() == 0) {
            Toast.makeText(getApplicationContext(), "Loading links.. Please make sure you are connected to a internet network", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = openWebPage(getApplicationContext(), getActions().get(0).getUrl());
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    public void homeAction2(View view) {
        if (getActions().size() < 2) {
            Toast.makeText(getApplicationContext(), "Loading links.. Please make sure you are connected to a internet network", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = openWebPage(getApplicationContext(), getActions().get(1).getUrl());
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    public void homeAction3(View view) {
        if (getActions().size() < 3) {
            Toast.makeText(getApplicationContext(), "Loading links.. Please make sure you are connected to a internet network", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = openWebPage(getApplicationContext(), getActions().get(2).getUrl());
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @SuppressLint("WrongConstant")
    public void onCreateViewCustomized() {
        // Sets Status bar icons to black
        // Sets custom Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
        // Changes Phones Stauts bar Icons to black
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Changes Satatus bar BG color
        Window window = getWindow();
        int statusBarColor = Color.parseColor("#FAF1E7");
        window.setStatusBarColor(statusBarColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(statusBarColor));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Changes menu ICON
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.addAuthStateListener(firebaseAuth -> {

            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser == null) {
                //go login
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                //update ui (say hello to the user)
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_favorites) {
            navController.navigate(R.id.navigation_favorites_books);
            return true;
        }
        if (id == R.id.action_logout) {
            //logout clicked.
            FirebaseAuth.getInstance().signOut();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}