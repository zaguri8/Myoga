package com.example.myoga.ui.yoga;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myoga.R;
import com.example.myoga.VideoRecyclerViewAdapter;
import com.example.myoga.models.NetworkChangeReceiver;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.VideosDataSource;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class BreathingFragment extends Fragment implements Observer {
    BreathingViewModel bViewModel;
    RecyclerView.Adapter rvAdapter;
    RecyclerView rvBreathing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bViewModel = new ViewModelProvider(this).get(BreathingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yoga_breathing, container, false);
        rvBreathing = root.findViewById(R.id.BreathingRv);
        rvBreathing.setHasFixedSize(true);
        rvBreathing.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
            declareFragmentTransactions(root);
            return root;
        }
        bViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
            rvAdapter = new VideoRecyclerViewAdapter(getContext(), VideoRecyclerViewAdapter.RV_TYPE.BREATH, videos);
            rvBreathing.setAdapter(rvAdapter);
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
            Button breathingButton = view.findViewById(R.id.btnBreathing);
            Button posturesButton = view.findViewById(R.id.btnPostures);
            Button meditationButton = view.findViewById(R.id.btnMeditation);
            if (getActivity() == null) {
                return;
            }
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

            posturesButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_yoga_postures);
            });
            breathingButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_yoga_breathing);
            });
            meditationButton.setOnClickListener(v -> {
                navController.navigate(R.id.navigation_yoga_meditation);
            });
        }).start();
    }

    @Override
    public void update(Observable o, Object arg) {

        if (!NetworkUtils.isNetworkConnected(getContext())) {
            rvBreathing.setAdapter(null);
        } else {
            if (rvBreathing.getAdapter() == null) {
                Toast.makeText(getContext(), "Loading videos..", Toast.LENGTH_SHORT).show();
            }
            bViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
                rvAdapter = new VideoRecyclerViewAdapter(getContext(), VideoRecyclerViewAdapter.RV_TYPE.BREATH, videos);
                rvBreathing.setAdapter(rvAdapter);
            });
        }
    }
}