package com.example.myoga.ui.wisdom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myoga.R;

public class WisdomFragment extends Fragment {

    private WisdomViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(WisdomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wisdom, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        declareFragmentTransactions(root);
        return root;
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
}