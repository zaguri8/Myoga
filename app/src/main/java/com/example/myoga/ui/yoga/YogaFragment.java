package com.example.myoga.ui.yoga;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.MainActivity;
import com.example.myoga.R;

public class YogaFragment extends Fragment {

    private YogaViewModel yogaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yogaViewModel =
                new ViewModelProvider(this).get(YogaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yoga, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}