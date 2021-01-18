package com.example.myoga.ui.wisdom;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.R;
import com.example.myoga.VideoRecyclerViewAdapter;
import com.example.myoga.models.NetworkChangeReceiver;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.VideosDataSource;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.example.myoga.models.VideosDataSource.loadVideos;

public class WatchFragment extends Fragment implements Observer {
    WatchViewModel bViewModel;
    RecyclerView.Adapter rvAdapter;
    RecyclerView WatchRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bViewModel = new ViewModelProvider(this).get(WatchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wisdom_watch, container, false);
        WatchRv = root.findViewById(R.id.WatchRv);
        WatchRv.setHasFixedSize(true);
        WatchRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
            declareFragmentTransactions(root);
            return root;
        }
        bViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
            rvAdapter = new VideoRecyclerViewAdapter(getContext(), VideoRecyclerViewAdapter.RV_TYPE.WATCH, videos);
            WatchRv.setAdapter(rvAdapter);
        });
        declareFragmentTransactions(root);
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

    private void declareFragmentTransactions(View view) {
        new Thread(() -> {
            Button watchButton = view.findViewById(R.id.btnWatch);
            Button listenButton = view.findViewById(R.id.btnListen);
            Button readButton = view.findViewById(R.id.btnRead);
            if (getActivity() == null) {
                return;
            }
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            watchButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_wisdom_watch);
            });
            listenButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_wisdom_listen);
            });
            readButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_wisdom_read);
            });
        }).start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            WatchRv.setAdapter(null);
        } else {
            if (WatchRv.getAdapter() == null) {
                Toast.makeText(getContext(), "Loading videos..", Toast.LENGTH_SHORT).show();
            }
            bViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
                rvAdapter = new VideoRecyclerViewAdapter(getContext(), VideoRecyclerViewAdapter.RV_TYPE.WATCH, videos);
                WatchRv.setAdapter(rvAdapter);
            });
        }
    }
}