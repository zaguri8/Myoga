package com.example.myoga.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.BookRecyclerViewAdapter;
import com.example.myoga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.myoga.models.DatabaseConnection.getMyogaFavorites;

public class BooksSourceData {
    ArrayList<Book> books;
    ArrayList<Book> FavBooks;
    private Context context;
    private BookRecyclerViewAdapter.RV_TYPE type;

    public BooksSourceData(Context context, BookRecyclerViewAdapter.RV_TYPE type) {
        this.context = context;
        this.type = type;
        if (type == BookRecyclerViewAdapter.RV_TYPE.ALL) {
            books = new ArrayList<>();
        } else {
            FavBooks = new ArrayList<>();
        }
    }

    public static class Book {
        String author;
        String country;
        int bookImage;
        String language;
        String link;
        String pages;
        String title;
        String year;

        public Book(String author, String country, int bookImageRes, String languge, String link, String pages, String title, String year) {
            this.author = author;
            this.country = country;
            this.bookImage = bookImageRes;
            this.language = languge;
            this.link = link;
            this.pages = pages;
            this.title = title;
            this.year = year;
        }

        public Book() {

        }


        public void setAuthor(String author) {
            this.author = author;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setBookImage(int bookImage) {
            this.bookImage = bookImage;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getAuthor() {
            return author;
        }

        public String getCountry() {
            return country;
        }

        public int getBookImage() {
            return bookImage;
        }

        public String getLanguage() {
            return language;
        }

        public String getLink() {
            return link;
        }

        public String getPages() {
            return pages;
        }

        public String getTitle() {
            return title;
        }

        public String getYear() {
            return year;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "author='" + author + '\'' +
                    ", country='" + country + '\'' +
                    ", bookImage=" + bookImage +
                    ", language='" + language + '\'' +
                    ", link='" + link + '\'' +
                    ", pages='" + pages + '\'' +
                    ", title='" + title + '\'' +
                    ", year='" + year + '\'' +
                    '}';
        }
    }

    public void addItemsFromDatabase(MutableLiveData<ArrayList<BooksSourceData.Book>> callback) {
        switch (type) {
            case ALL:
                books.clear();
                break;
            case FAV:
                FavBooks.clear();
                break;
        }

        new Thread(() -> {
            DatabaseReference ref = getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("books");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    callback.postValue(FavBooks);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    BooksSourceData.Book book = snapshot.getValue(BooksSourceData.Book.class);
                    FavBooks.add(book);
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
                    Toast.makeText(context, "Opss.. Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public void addItemsFromJSON(MutableLiveData<ArrayList<Book>> callback) {
        new Thread(() -> {
            try {
                String jsonDataString = readJSONDataFromFile(context);
                JSONArray obj = new JSONArray(jsonDataString);
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject json_inside = obj.getJSONObject(i);
                    String author = json_inside.getString("author");
                    String country = json_inside.getString("country");
                    String bookImage = json_inside.getString("imageLink").replace("images/", "").replace(".jpg", "").replaceAll("-", "");
                    int bookImageRes = context.getResources().getIdentifier(bookImage, "drawable", context.getPackageName());
                    String language = json_inside.getString("language");
                    String link = json_inside.getString("link");
                    String pages = String.valueOf(json_inside.getInt("pages"));
                    String title = json_inside.getString("title");
                    String year = String.valueOf(json_inside.getInt("year"));
                    books.add(new Book(author, country, bookImageRes, language, link, pages, title, year));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.postValue(books);
        }).start();
    }


    public String readJSONDataFromFile(Context context) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonString = null;
            inputStream = context.getResources().openRawResource(R.raw.books_api);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

}
