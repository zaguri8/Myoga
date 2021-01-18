package com.example.myoga.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myoga.R;

public class FavoritesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_favorites, container, false);
        declareFragmentTransactions(rootView);
        return rootView;
    }

    private void declareFragmentTransactions(View view) {
        new Thread(() -> {
        Button myBooksButton = view.findViewById(R.id.btnMyBooks);
        Button myAsanasButton = view.findViewById(R.id.btnMyAsanas);
        if (getActivity() == null) {
            return;
        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        myBooksButton.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_favorites_books);
        });
        myAsanasButton.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_favorites_postures);
        });
        }).start();
    }
}

