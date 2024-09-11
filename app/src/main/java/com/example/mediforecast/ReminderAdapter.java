package com.example.mediforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reminder> reminderList;


    public ReminderAdapter(Context context, ArrayList<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the reminder at the current position
        Reminder reminder = reminderList.get(position);
        // Bind the reminder data to the view holder's views
        holder.medicineName.setText(reminder.getMedicineName());
        holder.medicineDosage.setText(reminder.getMedicineDosage());
        holder.startDate.setText(reminder.getStartDate());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medicineName, medicineDosage, startDate, startDay,startMonth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicinenameTV);
            medicineDosage = itemView.findViewById(R.id.dosage);
            startDate = itemView.findViewById(R.id.date);
            startDay = itemView.findViewById(R.id.day);
            startMonth = itemView.findViewById(R.id.month);
        }
    }
}
