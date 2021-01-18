package com.example.myoga;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.models.BooksSourceData;
import com.example.myoga.models.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import static com.example.myoga.models.DatabaseConnection.getMyogaFavorites;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {
    private ArrayList<BooksSourceData.Book> allBooks;
    private ArrayList<BooksSourceData.Book> FavBooks;
    private final Context context;
    private final RV_TYPE type;

    public enum RV_TYPE {
        ALL(0), FAV(1);
        private final int numVal;

        RV_TYPE(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public BookRecyclerViewAdapter(Context context, ArrayList<BooksSourceData.Book> books, RV_TYPE type) {
        this.type = type;
        this.context = context;
        this.FavBooks = books;
        if (type == RV_TYPE.ALL) {
            allBooks = books;
        } else if (type == RV_TYPE.FAV) {
            FavBooks = books;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We need: View v =  How to inflate XMl file.
        // Create new ViewHolder.
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.book_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (type) {
            case ALL: {
                if (allBooks == null) {
                    Toast.makeText(context, "Unexpected error occured, please try to relog", Toast.LENGTH_LONG).show();
                    return;
                }
                String author = allBooks.get(position).getAuthor();
                String country = allBooks.get(position).getCountry();
                int imageLink = allBooks.get(position).getBookImage();
                String language = allBooks.get(position).getLanguage();
                String pages = allBooks.get(position).getPages();
                String link = allBooks.get(position).getLink();
                String title = allBooks.get(position).getTitle();
                String year = allBooks.get(position).getYear();
                BooksSourceData.Book book = new BooksSourceData.Book(author, country, imageLink, language, link, pages, title, year);
                holder.addtoFavorites.setOnClickListener(v -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (NetworkUtils.isNetworkConnected(context)) {
                            getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("books").push().setValue(book);
                            Toast.makeText(context, book.getTitle() + " added to favorites", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Unknown error has occured, please try to re-log", Toast.LENGTH_LONG).show();
                    }
                });
                holder.bookImage.setBackground(null);
                holder.bookImage.setImageResource(imageLink);
                holder.Title.setText(title);
                holder.Author.setText("Author: " + author);
                holder.Country.setText("Country: " + country);
                holder.Language.setText("Language: " + language);
                holder.Pages.setText("Pages: " + pages);
                holder.Year.setText("Year: " + year);
                break;
            }
            case FAV: {
                if (FavBooks == null) {
                    Toast.makeText(context, "Unexpected error occured, please try to relog", Toast.LENGTH_LONG).show();
                    return;
                }
                String author = FavBooks.get(position).getAuthor();
                String country = FavBooks.get(position).getCountry();
                int imageLink = FavBooks.get(position).getBookImage();
                String language = FavBooks.get(position).getLanguage();
                String pages = FavBooks.get(position).getPages();
                String link = FavBooks.get(position).getLink();
                String title = FavBooks.get(position).getTitle();
                String year = FavBooks.get(position).getYear();
                holder.addtoFavorites.setOnClickListener(v -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("books").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                BooksSourceData.Book b = snapshot.getValue(BooksSourceData.Book.class);
                                if (b.getTitle().equals(title) && position <= FavBooks.size()) {
                                    Toast.makeText(context, title + " has been removed from Favorites.", Toast.LENGTH_SHORT).show();
                                    snapshot.getRef().removeValue();
                                    deleteItem(position);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        Toast.makeText(context, "Unknown error has occured, please try to re-log", Toast.LENGTH_LONG).show();
                    }
                });
                holder.addtoFavorites.setImageResource(R.drawable.yoga_avatar_remove);
                holder.bookImage.setBackground(null);
                holder.bookImage.setImageResource(imageLink);
                holder.Title.setText(title);
                holder.Author.setText("Author: " + author);
                holder.Country.setText("Country: " + country);
                holder.Language.setText("Language: " + language);
                holder.Pages.setText("Pages: " + pages);
                holder.Year.setText("Year: " + year);
                break;
            }
        }
    }

    private void deleteItem(int position) {
        FavBooks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, FavBooks.size());
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return type == RV_TYPE.FAV ? FavBooks.size() : allBooks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Author;
        ImageView addtoFavorites;
        TextView Country;
        TextView Language;
        TextView Pages;
        TextView Title;
        TextView Year;
        ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Author = itemView.findViewById(R.id.bookAuthor);
            Country = itemView.findViewById(R.id.bookCountry);
            Language = itemView.findViewById(R.id.bookLanguage);
            Pages = itemView.findViewById(R.id.bookPages);
            Title = itemView.findViewById(R.id.bookTitle);
            Year = itemView.findViewById(R.id.bookYear);
            bookImage = itemView.findViewById(R.id.tvBookImage);
            addtoFavorites = itemView.findViewById(R.id.bookFav);
        }
    }

}
