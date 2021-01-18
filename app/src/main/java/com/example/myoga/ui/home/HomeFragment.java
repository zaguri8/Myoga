package com.example.myoga.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.R;
import com.example.myoga.models.DailyQuateDataSource;
import com.example.myoga.models.NetworkChangeReceiver;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.Utils;

import java.util.Observable;
import java.util.Observer;


public class HomeFragment extends Fragment implements Observer {

    private HomeViewModel homeViewModel;
    TextView dQuate;
    TextView dQuateAuthor;
    TextView dQuateDate;
    WebView dailyVideo1;
    WebView dailyVideo2;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dQuate = root.findViewById(R.id.dQuate);
        dQuateAuthor = root.findViewById(R.id.dQuateAuthor);
        dQuateDate = root.findViewById(R.id.dQuateDate);
        dailyVideo1 = root.findViewById(R.id.dailyVideoWV);
        dailyVideo2 = root.findViewById(R.id.dailyVideoWV2);
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            if (homeViewModel.getCachedQuate() != null) {
                dQuate.setText(homeViewModel.getCachedQuate().getText());
                dQuateAuthor.setText("- " + homeViewModel.getCachedQuate().getAuthor() + " ❤");
                dQuateDate.setText(homeViewModel.getCachedQuate().getDate());
            } else { // Default
                dQuate.setText("If you are a sensible human being, you will naturally be loving and inclusive.");
                dQuateAuthor.setText("- Sadghuru ❤");
                dQuateAuthor.setText("04 January 2021");
            }
        } else {
            homeViewModel.getDailyQuate().observe(getViewLifecycleOwner(), quate -> {
                dQuate.setText(quate.getText());
                dQuateAuthor.setText("- " + quate.getAuthor() + " ❤");
                dQuateDate.setText(quate.getDate());
            });
            homeViewModel.getDailyVideoUrl1().observe(getViewLifecycleOwner(), videoURL -> {
                Utils.loadEmbedVideo(videoURL, dailyVideo1, getContext(), 150, 100);
            });
            homeViewModel.getDailyVideoUrl2().observe(getViewLifecycleOwner(), videoURL -> {
                Utils.loadEmbedVideo(videoURL, dailyVideo2, getContext(), 150, 100);
            });

        }
        //        Utils.loadEmbedVideo(videoURL, holder.webView, context);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        NetworkChangeReceiver.getObservable().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkChangeReceiver.getObservable().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            homeViewModel.getDailyQuate().observe(getViewLifecycleOwner(), quate -> {
                dQuate.setText(quate.getText());
                dQuateAuthor.setText("- " + quate.getAuthor() + " ❤");
                dQuateDate.setText(quate.getDate());
            });
        } else {
            if (homeViewModel.getCachedQuate() != null) {
                dQuate.setText(homeViewModel.getCachedQuate().getText());
                dQuateAuthor.setText("- " + homeViewModel.getCachedQuate().getAuthor() + " ❤");
                dQuateDate.setText(homeViewModel.getCachedQuate().getDate());
            } else { // Default
                dQuate.setText("If you are a sensible human being, you will naturally be loving and inclusive.");
                dQuateAuthor.setText("- Sadghuru ❤");
                dQuateDate.setText("04 January 2021");
            }
        }
    }
}