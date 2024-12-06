package com.example.mediforecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<History> historyList;
    private OnDeleteClickListener deleteClickListener;
    private OnRowClickListener rowClickListener;

    // Add OnRowClickListener interface
    public interface OnRowClickListener {
        void onRowClick(int position);
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public TableAdapter(List<History> historyList, OnDeleteClickListener listener, OnRowClickListener rowListener) {
        this.historyList = historyList;
        this.deleteClickListener = listener;
        this.rowClickListener = rowListener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        History history = historyList.get(position);

        // Bind data to the views
        holder.dateTextView.setText(history.getDate());
        holder.painLocationTextView.setText(history.getPainLocation());

        // Set delete button click listener
        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(position));

        holder.itemView.setOnClickListener(v -> rowClickListener.onRowClick(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, painLocationTextView;
        ImageButton deleteButton;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.column1);
            painLocationTextView = itemView.findViewById(R.id.column2);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void updateData(List<History> newHistoryList) {
        this.historyList = newHistoryList;
        notifyDataSetChanged();
    }
}