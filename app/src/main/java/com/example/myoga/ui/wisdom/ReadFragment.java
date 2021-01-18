package com.example.myoga.ui.wisdom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.MeditationRvAdapter;
import com.example.myoga.R;
import com.example.myoga.ui.yoga.PosturesViewModel;

public class ReadFragment extends Fragment {

    private ReadViewModel mViewModel;

    public static ReadFragment newInstance() {
        return new ReadFragment();
    }
    RecyclerView ReadRv;
    RecyclerView.Adapter ReadRvAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ReadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wisdom_read, container, false);
        ReadRv = root.findViewById(R.id.ReadRv);
        ReadRv.setHasFixedSize(true);
        ReadRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            ReadRvAdapter = new BookRecyclerViewAdapter(getContext(), books, BookRecyclerViewAdapter.RV_TYPE.ALL);
            ReadRv.setAdapter(ReadRvAdapter);
        });
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