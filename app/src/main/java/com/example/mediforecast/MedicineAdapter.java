package com.example.mediforecast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicines;
    private MedicineRepository medicineRepository;

    public MedicineAdapter(Context context, List<Medicine> medicines, MedicineRepository medicineRepository) {
        this.context = context;
        this.medicines = medicines;
        this.medicineRepository = medicineRepository;
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
        holder.time.setText(medicine.getTime1());

        String combDosage = "Take " + medicine.getDose1() + "(" + medicine.getMedicineType() + ")";
        holder.dosage.setText(combDosage);

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

                if (itemId == R.id.action_delete) {
                    confirmDelete(medicine, position);
                    return true;
                } else if (itemId == R.id.menuUpdate) {
                    int medicineId = medicine.getId();
                    updateMedicine(medicineId);
                    return true;
                } else if(itemId == R.id.menuView){
                    int medicineId = medicine.getId();
                    viewMedicine(medicineId);
                    return true;
                }else {
                    return false;
                }
            });
            popupMenu.show();
        });
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
}
