package com.example.myoga.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.R;
import com.example.myoga.VideoRecyclerViewAdapter;
import com.example.myoga.models.BooksSourceData;

import com.example.myoga.models.NetworkChangeReceiver;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.VideosDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.example.myoga.models.DatabaseConnection.getDbRef;
import static com.example.myoga.models.DatabaseConnection.getMyogaFavorites;

public class FavoritesBooksFragment extends Fragment implements Observer {
    RecyclerView ReadRvFav;
    RecyclerView.Adapter ReadRvAdapter;
    ArrayList<BooksSourceData.Book> FavBooks;
    DatabaseReference reference;
    FavoritesBooksViewModel booksViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        booksViewModel = new ViewModelProvider(this).get(FavoritesBooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_books_favorites, container, false);
        ReadRvFav = root.findViewById(R.id.ReadRvFav);
        ReadRvFav.setHasFixedSize(true);
        ReadRvFav.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FavBooks = new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "Opss.. Something went wrong, try to relog", Toast.LENGTH_SHORT).show();
            declareFragmentTransactions(root);
            return root;
        }
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
            declareFragmentTransactions(root);
            return root;
        }

        booksViewModel.getFavBooks().observe(getViewLifecycleOwner(), books -> {
            ReadRvAdapter = new BookRecyclerViewAdapter(getContext(), books, BookRecyclerViewAdapter.RV_TYPE.FAV);
            ReadRvFav.setAdapter(ReadRvAdapter);
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

    @Override
    public void update(Observable o, Object arg) {
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            ReadRvFav.setAdapter(null);
        } else {
            if (ReadRvFav.getAdapter() == null) {
                Toast.makeText(getContext(), "Network Found, Loading Favorites..", Toast.LENGTH_SHORT).show();
            }
            booksViewModel.getFavBooks().observe(getViewLifecycleOwner(), books -> {
                ReadRvAdapter = new BookRecyclerViewAdapter(getContext(), books, BookRecyclerViewAdapter.RV_TYPE.FAV);
                ReadRvFav.setAdapter(ReadRvAdapter);
            });
        }
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