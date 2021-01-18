package com.example.myoga.ui.wisdom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.MeditationRvAdapter;
import com.example.myoga.R;

public class ListenFragment extends Fragment {
    RecyclerView.Adapter ListenRvAdapter;
    RecyclerView ListenRv;
    ListenViewModel listenViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listenViewModel = new ViewModelProvider(this).get(ListenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wisdom_listen, container, false);
        ListenRv = root.findViewById(R.id.ListenRv);
        ListenRv.setHasFixedSize(true);
        ListenRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ListenRvAdapter = new MeditationRvAdapter(getContext());
        ListenRv.setAdapter(ListenRvAdapter);
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


