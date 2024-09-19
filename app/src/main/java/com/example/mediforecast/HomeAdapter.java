package com.example.mediforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Home> list;

    public HomeAdapter(Context context, ArrayList<Home> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Home item = list.get(position);
        holder.textViewRhu.setText(item.getRhu());
        holder.textViewMessage.setText(item.getPostMessage());
        // Load the image using an image loading library like Glide or Picasso
        // Glide.with(context).load(item.getPostImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRhu, textViewMessage;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            textViewRhu = itemView.findViewById(R.id.textViewRhu);
//            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
