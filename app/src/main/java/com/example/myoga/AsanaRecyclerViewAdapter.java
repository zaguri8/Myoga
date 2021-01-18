package com.example.myoga;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.models.BooksSourceData;
import com.example.myoga.models.NetworkUtils;
import com.example.myoga.models.YogaDataSource;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import static com.example.myoga.models.DatabaseConnection.getMyogaFavorites;

public class AsanaRecyclerViewAdapter extends RecyclerView.Adapter<AsanaRecyclerViewAdapter.ViewHolder> {
    private ArrayList<YogaDataSource.YogaAsana> allAsanas;
    private ArrayList<YogaDataSource.YogaAsana> FavAsanas;
    private Context context;
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

    public AsanaRecyclerViewAdapter(Context context, ArrayList<YogaDataSource.YogaAsana> asanas, RV_TYPE type) {
        this.type = type;
        this.context = context;
        if (type == RV_TYPE.ALL) {
            this.allAsanas = asanas;
        } else {
            this.FavAsanas = asanas;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We need: View v =  How to inflate XMl file.
        // Create new ViewHolder.
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.asana_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == RV_TYPE.ALL) {
            String sanskritName = allAsanas.get(position).getSanskritName();
            String englishName = allAsanas.get(position).getEnglishName();
            String imageExample = allAsanas.get(position).getImageExample();
            Uri imageExampleUri = Uri.parse(imageExample);
            Activity contextActivity = (Activity) context;
            holder.tvExampleImage.setBackground(null);
            GlideToVectorYou.justLoadImage(contextActivity, imageExampleUri, holder.tvExampleImage);
            holder.tvAsanaSanskritName.setText(sanskritName);
            holder.tvAsanaEnglishName.setText(englishName);
            holder.asanaGeneral.setImageResource(R.drawable.yoga_avatar);
            YogaDataSource.YogaAsana asana = new YogaDataSource.YogaAsana(sanskritName, englishName, imageExample);
            holder.asanaGeneral.setOnClickListener(v -> {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (NetworkUtils.isNetworkConnected(context)) {
                        getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("asanas").push().setValue(asana);
                        Toast.makeText(context, asana.getEnglishName() + " added to favorites", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Opss.. Seems like you do not have internet connection..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unknown error has occured, please try to re-log", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            if (FavAsanas == null) {
                Toast.makeText(context, "Unexpected error occured, please try to relog", Toast.LENGTH_LONG).show();
                return;
            }
            String sanskritName = FavAsanas.get(position).getSanskritName();
            String englishName = FavAsanas.get(position).getEnglishName();
            String imageExample = FavAsanas.get(position).getImageExample();
            holder.asanaGeneral.setOnClickListener(v -> {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    getMyogaFavorites().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("asanas").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            YogaDataSource.YogaAsana asana = snapshot.getValue(YogaDataSource.YogaAsana.class);
                            if (asana.getEnglishName().equals(englishName) && position <= FavAsanas.size()) {
                                Toast.makeText(context, englishName + " has been removed from Favorites.", Toast.LENGTH_SHORT).show();
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
            Uri imageExampleUri = Uri.parse(imageExample);
            Activity contextActivity = (Activity) context;
            holder.tvExampleImage.setBackground(null);
            GlideToVectorYou.justLoadImage(contextActivity, imageExampleUri, holder.tvExampleImage);
            holder.tvAsanaSanskritName.setText(sanskritName);
            holder.tvAsanaEnglishName.setText(englishName);
            holder.asanaGeneral.setImageResource(R.drawable.yoga_avatar_remove);
        }
    }

    private void deleteItem(int position) {
        FavAsanas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, FavAsanas.size());
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return type == RV_TYPE.FAV ? FavAsanas.size() : allAsanas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAsanaSanskritName;
        TextView tvAsanaEnglishName;
        ImageButton tvExampleImage;
        ImageView asanaGeneral;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAsanaSanskritName = itemView.findViewById(R.id.bookTitle);
            tvAsanaEnglishName = itemView.findViewById(R.id.bookAuthor);
            asanaGeneral = itemView.findViewById(R.id.asanaGeneral);
            tvExampleImage = itemView.findViewById(R.id.tvBookImage);
        }
    }


}
