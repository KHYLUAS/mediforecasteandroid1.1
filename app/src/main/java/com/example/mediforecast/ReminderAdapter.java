package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        holder.medicineName.setText(reminder.getMedicineName());
        String dosageWithType = reminder.getMedicineDosage() + " " + reminder.getMedicineType() + "(s)";
        holder.medicineDosage.setText(dosageWithType);
        holder.twice.setText(reminder.getAlarmTime());
        holder.status.setText(reminder.isStatus() ? "COMPLETED" : "UPCOMING");

        String startDate = reminder.getStartDate();
        Log.d("ReminderAdapter", "Setting reminder startDate: " + startDate);

        if (startDate == null || startDate.isEmpty()) {
            holder.day.setText("");
            holder.date.setText("");
            holder.month.setText("");
        } else {
            // Use the newly created methods to set the day, date, and month
            holder.day.setText(reminder.getDay());
            holder.date.setText(reminder.getDate());
            holder.month.setText(reminder.getMonth());
        }

        // Handle options menu for each item
        holder.optionMenu.setOnClickListener(v -> {
            // Create a PopupMenu
            PopupMenu popupMenu = new PopupMenu(context, holder.optionMenu);
            popupMenu.inflate(R.menu.menu_reminder); // Inflate your menu XML file

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.menuDelete) {
                    String medicineName = reminder.getMedicineName();
                    FirebaseFirestore.getInstance().collection("MedicineReminder")
                            .whereEqualTo("medicineName", medicineName)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                    reminderList.remove(position);
                                    notifyItemRemoved(position);
                                }
                            });
                    return true;
                } else if (id == R.id.menuView) {
                    // Handle view action, e.g., navigate to detail activity
                    Intent intent = new Intent(context, view_medicine_reminder.class);
                    intent.putExtra("medicineName", reminder.getMedicineName());
                    context.startActivity(intent);
                    return true;
                } else if (id == R.id.menuUpdate) {
                    // Handle update action
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

    }



    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medicineName, medicineDosage, day, date, month, twice, status;
        private ImageView optionMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicinenameTV);
            medicineDosage = itemView.findViewById(R.id.dosage);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            twice = itemView.findViewById(R.id.twice);
            status = itemView.findViewById(R.id.status);
        }
    }
}
