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

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.R;

public class PosturesFragment extends Fragment {
    RecyclerView.Adapter rvAdapter;
    RecyclerView rvAsanas;
    PosturesViewModel posturesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        posturesViewModel = new ViewModelProvider(this).get(PosturesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yoga_postures, container, false);
        rvAsanas = root.findViewById(R.id.AsanasRv);
        rvAsanas.setHasFixedSize(true);
        rvAsanas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Toast.makeText(getContext(), "Loading postures..", Toast.LENGTH_SHORT).show();
        posturesViewModel.getAllAsanas().observe(getViewLifecycleOwner(), yogaAsanas -> {
            rvAdapter = new AsanaRecyclerViewAdapter(getContext(), yogaAsanas, AsanaRecyclerViewAdapter.RV_TYPE.ALL);
            rvAsanas.setAdapter(rvAdapter);
        });
        declareFragmentTransactions(root);
        return root;
    }

    private void declareFragmentTransactions(View view) {
        new Thread(() -> {
            Button breathingButton = view.findViewById(R.id.btnBreathing);
            Button posturesButton = view.findViewById(R.id.btnPostures);
            Button meditationButton = view.findViewById(R.id.btnMeditation);
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
}