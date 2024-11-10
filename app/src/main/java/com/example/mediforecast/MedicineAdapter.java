package com.example.mediforecast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicines;
    private MedicineRepository medicineRepository;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer;


    public MedicineAdapter(Context context, List<Medicine> medicines, MedicineRepository medicineRepository) {
        this.context = context;
        this.medicines = medicines;
        this.medicineRepository = medicineRepository;
        startUpdating();
    }

    private void startUpdating() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Update the UI on the main thread
                handler.post(() -> notifyDataSetChanged());
            }
        }, 0, 10000); // Repeat every minute
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.nameTextView.setText(medicine.getMedicineName());
//        holder.time.setText(medicine.getTime1());
//
//        String combDosage = "Take " + medicine.getDose1() + "(" + medicine.getMedicineType() + ")";
//        holder.dosage.setText(combDosage);
        updateMedicineDisplay(holder, medicine);

        Log.d("MedicineAdapter", "Medicine at position " + position + ": " + medicine.toString());

//        String formattedStartDate = medicine.getFormattedStartDate();
//        Log.d("ReminderAdapter", "Formatted reminder startDate: " + formattedStartDate);

        holder.day.setText(medicine.getDay());
        holder.date.setText(medicine.getDate());
        holder.month.setText(medicine.getMonth());

        // Option(view, update, delete)
        holder.optionMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.optionMenu);
            popupMenu.inflate(R.menu.menu_reminder);
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                Log.d("MedicineAdapter", "Menu item clicked: " + itemId);
                if (itemId == R.id.action_delete) {
                    confirmDelete(medicine, position);
                    return true;
                } else if (itemId == R.id.menuUpdate) {
                    int medicineId = medicine.getId();
                    updateMedicine(medicineId);
                    Log.d("MedicineAdapter", "Selected Medicine ID for Update: " + medicineId);
                    return true;
                } else if(itemId == R.id.menuView){
                    int medicineId = medicine.getId();
                    viewMedicine(medicineId);
                    Log.d("MedicineAdapter", "Selected Medicine ID for View: " + medicineId);
                    return true;
                }else {
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    private void updateMedicineDisplay(MedicineViewHolder holder, Medicine medicine) {
        String time1Single = medicine.getTime1();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTimeNow = sdf.format(calendar.getTime());

        // Handle single digit time calculation
        if (time1Single.length() == 1 && Character.isDigit(time1Single.charAt(0))) {
            try {
                Date currentDate = sdf.parse(currentTimeNow);
                if (currentDate != null) {
                    calendar.setTime(currentDate);
                    int timeToAdd = Integer.parseInt(time1Single);
                    calendar.add(Calendar.HOUR, timeToAdd);

                    String nextDoseTime = sdf.format(calendar.getTime());

                    // Check if current time matches next dose time
                    if (currentTimeNow.equals(nextDoseTime.toString())) {
                        // Only update nextDoseTime when currentTime matches
                        calendar.add(Calendar.HOUR, timeToAdd);
                        nextDoseTime = sdf.format(calendar.getTime());

                        // Update the UI with new dose time

                    }
                    holder.time.setText(nextDoseTime);
                    holder.dosage.setText("Take " + medicine.getDose1() + " (" + medicine.getMedicineType() + ")");
                }
            } catch (ParseException e) {
                Log.e("TimeCheck", "Error parsing time: " + currentTimeNow, e);
            }
        }
        else {
            Log.d("TimeCheck", "Not a single digit time: " + time1Single);
            long currentTime = System.currentTimeMillis();

            long time1End = getTimeEnd(medicine.getTime1());
            long time2End = getTimeEnd(medicine.getTime2());
            long time3End = getTimeEnd(medicine.getTime3());
            if (currentTime < time1End) {
                holder.time.setText(medicine.getTime1());
                holder.dosage.setText("Take " + medicine.getDose1() + " (" + medicine.getMedicineType() + ")");
            } else if (time2End != Long.MAX_VALUE && currentTime < time2End) { // Check if time2 is valid
                holder.time.setText(medicine.getTime2());
                holder.dosage.setText("Take " + medicine.getDose2() + " (" + medicine.getMedicineType() + ")");
            } else if (time3End != Long.MAX_VALUE && currentTime < time3End) { // Check if time3 is valid
                holder.time.setText(medicine.getTime3());
                holder.dosage.setText("Take " + medicine.getDose3() + " (" + medicine.getMedicineType() + ")");
            } else {
                holder.time.setText(medicine.getTime1());
                holder.dosage.setText("Take " + medicine.getDose1() + " (" + medicine.getMedicineType() + ")");
            }
        }

    }


    private long getTimeEnd(String time) {
        if (time == null || time.isEmpty()) {
            return Long.MAX_VALUE; // Treat null or empty as if the time has already passed
        }
        return convertToMillis(time); // Don't add an hour if not needed
    }

    private long convertToMillis(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf.parse(time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Calendar now = Calendar.getInstance();
            calendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            Log.e("MedicineAdapter", "Error parsing time: " + time, e);
            return 0; // Return 0 in case of error
        }
    }


    @Override
    public int getItemCount() {
        return medicines != null ? medicines.size() : 0;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }
    private void confirmDelete(Medicine medicine, int position){
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.warning_ic)
                .setTitle("Delete Reminder Confirmation")
                .setMessage("Are you sure you want to delete this reminder?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMedicine(medicine, position);
                        Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(context, "Deletion canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    private void updateMedicine(int medicineId){
        Intent intent = new Intent(context, UpdateReminder.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        Log.d("UpdateMedicine", "Intent Passed with Medicine ID: " + medicineId);
        context.startActivity(intent);
    }
    private void viewMedicine(int medicineId){
        Intent intent = new Intent(context, UpdateReminder.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        intent.putExtra("MEDICINE_VIEW", true);
        context.startActivity(intent);
    }
    private void deleteMedicine(Medicine medicine, int position) {
        // Delete medicine from database
        medicineRepository.delete(medicine);

        // Remove medicine from the list and notify adapter
        medicines.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, medicines.size());
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, day, date, month, time, dosage;
        ImageView optionMenu;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicinenameTV);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            time = itemView.findViewById(R.id.time);
            dosage = itemView.findViewById(R.id.dosage);
        }
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (timer != null) {
            timer.cancel(); // Stop the timer
        }
    }
}
