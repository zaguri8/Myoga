package com.example.myoga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoga.models.MeditationRvData;
import com.example.myoga.ui.yoga.MeditationFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MeditationRvAdapter extends RecyclerView.Adapter<MeditationRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MeditationRvData> meditationRvData;

    public MeditationRvAdapter(Context context) {
        this.context = context;
        this.meditationRvData = new ArrayList<>();
        this.meditationRvData.add(new MeditationRvData("Isha Kriya", "Guided Meditations", null, ContextCompat.getDrawable(context,R.drawable.ashatnga))); // 1
        this.meditationRvData.add(new MeditationRvData("Infinity Meditation", "Guided Meditations", null, ContextCompat.getDrawable(context,R.drawable.tibetian))); //2
        this.meditationRvData.add(new MeditationRvData("Sadghuru", "Daily 6:20 PM", null, ContextCompat.getDrawable(context,R.drawable.sad))); //3
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.meditation_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(meditationRvData.get(position).getTitle());
        holder.desc.setText(meditationRvData.get(position).getDesc());
        holder.itemView.findViewById(R.id.itemMeditationLayout).setBackground(meditationRvData.get(position).getItemImage());
    }

    @Override
    public int getItemCount() {
        return meditationRvData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemMeditationTitle);
            desc = itemView.findViewById(R.id.itemMeditationDesc);
        }
    }
}
