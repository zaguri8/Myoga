package com.example.myoga.ui.yoga;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myoga.MeditationRvAdapter;
import com.example.myoga.R;

public class MeditationFragment extends Fragment {

    private MeditationViewModel mViewModel;

    public static MeditationFragment newInstance() {
        return new MeditationFragment();
    }
    RecyclerView rvMeditation;
    RecyclerView.Adapter rvMeditationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MeditationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yoga_meditation, container, false);
        declareFragmentTransactions(root);
        rvMeditation = root.findViewById(R.id.rvMeditation);
        rvMeditation.setHasFixedSize(true);
        rvMeditation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvMeditationAdapter = new MeditationRvAdapter(getContext());
        rvMeditation.setAdapter(rvMeditationAdapter);
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