package com.example.myoga.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.myoga.AsanaRecyclerViewAdapter;
import com.example.myoga.R;
import com.google.firebase.FirebaseError;
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

import static com.example.myoga.models.DatabaseConnection.getMyogaFavorites;

public class YogaDataSource {
    private static ArrayList<YogaAsana> allAsanas;
    private static ArrayList<YogaAsana> FavAsanas;
    private Context context;
    private AsanaRecyclerViewAdapter.RV_TYPE type;

    public YogaDataSource(Context context, AsanaRecyclerViewAdapter.RV_TYPE type) {
        this.context = context;
        this.type = type;
        if (type == AsanaRecyclerViewAdapter.RV_TYPE.ALL) {
            allAsanas = new ArrayList<>();
        } else {
            FavAsanas = new ArrayList<>();
        }
    }

    public static class YogaAsana {
        private String sanskritName;
        private String englishName;
        private String imageExample;
        private boolean showImage = false;

        public YogaAsana(String sanskritName, String englishName, String imageExample) {
            this.sanskritName = sanskritName;
            this.englishName = englishName;
            this.imageExample = imageExample;
        }

        public YogaAsana() {

        }

        public String getSanskritName() {
            return sanskritName;
        }

        public String getEnglishName() {
            return englishName;
        }

        public String getImageExample() {
            return imageExample;
        }

        public boolean isShowImage() {
            return showImage;
        }

        public void setShowImage(boolean showImage) {
            this.showImage = showImage;
        }

        public void setSanskritName(String sanskritName) {
            this.sanskritName = sanskritName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public void setImageExample(String imageExample) {
            this.imageExample = imageExample;
        }

        @Override
        public String toString() {
            return "YogaAsana{" +
                    "sanskritName='" + sanskritName + '\'' +
                    ", englishName='" + englishName + '\'' +
                    ", imageExample='" + imageExample + '\'' +
                    '}';
        }
    }

    public void addItemsFromDatabase(MutableLiveData<ArrayList<YogaAsana>> callback) {
        new Thread(() -> {
            DatabaseReference ref = getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("asanas");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    callback.postValue(FavAsanas);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    YogaDataSource.YogaAsana asana = snapshot.getValue(YogaDataSource.YogaAsana.class);
                    FavAsanas.add(asana);
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

    public void addItemsFromJSON(MutableLiveData<ArrayList<YogaAsana>> callback) {
        new Thread(() -> {
            try {
                String jsonDataString = readJSONDataFromFile(context);
                JSONArray obj = new JSONArray(jsonDataString);
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject json_inside = obj.getJSONObject(i);
                    String sanskritName = json_inside.getString("sanskrit_name");
                    String englishName = json_inside.getString("english_name");
                    String exampleImage = json_inside.getString("img_url");
                    allAsanas.add(new YogaAsana(sanskritName, englishName, exampleImage));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.postValue(allAsanas);
        }).start();
    }


    public String readJSONDataFromFile(Context context) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonString = null;
            inputStream = context.getResources().openRawResource(R.raw.yoga_api);
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

    public ArrayList<YogaAsana> getAllAsanas() {
        return allAsanas;
    }

}
