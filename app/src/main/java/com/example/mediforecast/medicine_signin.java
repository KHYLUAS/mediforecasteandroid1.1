package com.example.mediforecast;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class medicine_signin extends AppCompatActivity {

    private AutoCompleteTextView reminderMedicineType, reminderUnitType;
    private EditText medicinenameET;
    private TextView medicinestartdate, medicineenddate;
    private MultiAutoCompleteTextView showSchedule;


    String[] unitType = {"IU", "ampoule(s)","application(s)","application(s)", "capsule(s)","drop(s)","gram(s)",
            "injection(s)", "milligram(s)", "milliliter(s)", "mm", "packet(s)", "packet(s)", "patch(es)",
            "pessary(ies)", "piece(s)","pill(s)", "portion(s)", "puff(s)", "spray(s)","suppository(ies)",
            "tablespoon(s)", "teaspoon(s)", "unit(s)", "vaginal capsule(s)", "vaginal insert(s)",
            "vaginal tablet(s)", "µg"};
    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection", "Inhaler", "Drop", "Suppositories", "Tropical Medicines"};
    ArrayAdapter<String> medicineTypeAdapter;
    ArrayAdapter<String> reminderUnitTypeAdapter;
    private Button buttonAdd;
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, schedule;
    private CheckBox everyDayCheckBox;
    // List to store selected days
    private ArrayList<String> selectedDays;
    private ImageView medicinearrow;
    private MedicineRepository medicineRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_signin);


        // Initialize repository
        medicineRepository = new MedicineRepository(getApplication());

        medicinestartdate = findViewById(R.id.medicinestartdate);
        buttonAdd = findViewById(R.id.buttonadd);
        reminderMedicineType = findViewById(R.id.reminderMedicineType);
        medicinenameET = findViewById(R.id.medicinename);
        medicineenddate = findViewById(R.id.medicineenddate);
        schedule = findViewById(R.id.schedule);
        medicinearrow = findViewById(R.id.medicinearrow);
        reminderUnitType = findViewById(R.id.unit);
        everyDayCheckBox = findViewById(R.id.every_day);
        showSchedule = findViewById(R.id.showSchedule);

        everyDayCheckBox.setChecked(false);
        StringBuilder scheduleDetails = new StringBuilder();

        Intent intents = getIntent();
       if(intents.hasExtra("onceDaily")){
           String frequency = intents.getStringExtra("onceDaily");
           String dose1 = intents.getStringExtra("dose1");
           String time1 = intents.getStringExtra("time1");
            schedule.setText(frequency);
           scheduleDetails.append("<b>First Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ")
                   .append(time1)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose1);
           showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
       }else if(intents.hasExtra("twiceDaily")){
           String time1 = intents.getStringExtra("time1");
           String time2 = intents.getStringExtra("time2");
           String dose1 = intents.getStringExtra("dose1");
           String dose2 = intents.getStringExtra("dose2");
           String frequency = intents.getStringExtra("twiceDaily");
           schedule.setText(frequency);
           scheduleDetails.append("<b>First Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ").append(time1)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose1)
                   .append("<br>")
                   .append("<br>")
                   .append("<b>Second Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ").append(time2)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose2);
           showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
       }else if(intents.hasExtra("thriceDaily")){
           String time1 = intents.getStringExtra("time1");
           String time2 = intents.getStringExtra("time2");
           String time3 = intents.getStringExtra("time3");
           String dose1 = intents.getStringExtra("dose1");
           String dose2 = intents.getStringExtra("dose2");
           String dose3 = intents.getStringExtra("dose3");
           String frequency = intents.getStringExtra("thriceDaily");
           schedule.setText(frequency);
           scheduleDetails.append("<b>First Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ").append(time1)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose1)
                   .append("<br>")
                   .append("<br>")
                   .append("<b>Second Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ").append(time2)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose2)
                   .append("<br>")
                   .append("<br>")
                   .append("<b>Third Take</b>")
                   .append("<br>")
                   .append("<b>Time:</b> ").append(time3)
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose3);
           showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
       }
        showSchedule.setText(scheduleDetails.toString());
        showSchedule.setText(Html.fromHtml(scheduleDetails.toString(), Html.FROM_HTML_MODE_COMPACT));

        // Initialize ArrayAdapter for unit types
        reminderUnitTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, unitType);
        reminderUnitType.setAdapter(reminderUnitTypeAdapter);
        reminderUnitType.setOnClickListener(v-> reminderUnitType.showDropDown());
        reminderUnitType.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedMedicineUnitType = (String) adapterView.getItemAtPosition(position);
            Toast.makeText(medicine_signin.this, "Medicine Unit Type: " + selectedMedicineUnitType, Toast.LENGTH_SHORT).show();
            getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("SelectedSchedule", selectedMedicineUnitType)
                    .apply();
        });

        // Initialize ArrayAdapter for medicine types
        medicineTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, medicineType);
        reminderMedicineType.setAdapter(medicineTypeAdapter);
        // Set OnClickListener to show dropdown on click
        reminderMedicineType.setOnClickListener(v -> {
            reminderMedicineType.requestFocus();
            reminderMedicineType.showDropDown();
        });
        reminderMedicineType.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedMedicineType = (String) adapterView.getItemAtPosition(position);
            Toast.makeText(medicine_signin.this, "Medicine Type: " + selectedMedicineType, Toast.LENGTH_SHORT).show();
        });

        medicinestartdate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        medicineenddate.setOnClickListener(v->{
            showDatePickerDialogEnd();
        });


        // Day TextViews
        sunday = findViewById(R.id.dv_sunday);
        monday = findViewById(R.id.dv_monday);
        tuesday = findViewById(R.id.dv_tuesday);
        wednesday = findViewById(R.id.dv_wednesday);
        thursday = findViewById(R.id.dv_thursday);
        friday = findViewById(R.id.dv_friday);
        saturday = findViewById(R.id.dv_saturday);

        // Initialize selectedDays list
        selectedDays = new ArrayList<>();

        // Add listeners to each day
        setupDayClickListeners();

        // Handling the "Every day" checkbox
        everyDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Select all days if "Every day" is checked
                selectAllDays();
            } else {
                // Deselect all days if "Every day" is unchecked
                deselectAllDays();
            }
        });
        buttonAdd.setOnClickListener(v -> {
            // Get values from the form fields
            String medicineName = medicinenameET.getText().toString();
            String unitType = reminderUnitType.getText().toString();
            String medicineType = reminderMedicineType.getText().toString();
            String startDate = medicinestartdate.getText().toString();
            String endDate = medicineenddate.getText().toString();
            String schedules = schedule.getText().toString();
            String SelectedDays = String.join(", ", selectedDays); // Join selected days

            String time1 = intents.getStringExtra("time1"); // Mandatory
            String dose1 = intents.getStringExtra("dose1"); // Mandatory

            // Optional values
            String time2 = intents.hasExtra("time2") ? intents.getStringExtra("time2") : null;
            String dose2 = intents.hasExtra("dose2") ? intents.getStringExtra("dose2") : null;
            String time3 = intents.hasExtra("time3") ? intents.getStringExtra("time3") : null;
            String dose3 = intents.hasExtra("dose3") ? intents.getStringExtra("dose3") : null;

            if (medicineName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Medicine object
            Medicine medicine = new Medicine(medicineName, startDate, endDate, medicineType, unitType, schedules, SelectedDays,
                    time1, dose1, time2, dose2, time3, dose3);

            // Insert into the database
            medicineRepository.insert(medicine);

            Toast.makeText(this, "Medicine added successfully!", Toast.LENGTH_SHORT).show();
            setMedicineReminder(medicineName, time1, time2, time3, startDate, endDate, SelectedDays); // Pass value to schedule reminder
            finish(); // Finish and go back to the previous activity
            // Optional: clear form fields
            clearForm();

            Intent intent = new Intent(medicine_signin.this, splashscreenaddalarm.class);
            startActivity(intent);
        });
        schedule.setOnClickListener(v-> {

            Intent intent = new Intent(medicine_signin.this, medicineschedule.class);
            startActivity(intent);
            finish();
        });
        medicinearrow.setOnClickListener(v->{
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            clearForm();
            // Confirm clearing
            Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(medicine_signin.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "SIGNIN");
            startActivity(intent);
            finish();
        });

    }
    private void clearForm() {
        medicinenameET.setText("");
        medicinestartdate.setText("");
        medicineenddate.setText("");
        reminderMedicineType.setText("");
        reminderUnitType.setText("");
        schedule.setText("");
        selectedDays.clear();
        deselectAllDays();
        everyDayCheckBox.setChecked(false);

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("medicineName", medicinenameET.getText().toString().trim());
        editor.putString("medicineType", reminderMedicineType.getText().toString().trim());
        editor.putString("medicineStartDate", medicinestartdate.getText().toString().trim());
        editor.putString("medicineEndDate", medicineenddate.getText().toString().trim());
        editor.putString("selectedUnitType", reminderUnitType.getText().toString().trim());
        editor.putBoolean("isEveryDay", everyDayCheckBox.isChecked());
        editor.putStringSet("selectedDays", new HashSet<>(selectedDays));
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Restore the values from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        medicinenameET.setText(preferences.getString("medicineName", ""));
        reminderMedicineType.setText(preferences.getString("medicineType", ""), false);
        reminderUnitType.setText(preferences.getString("selectedUnitType", ""), false);
        medicinestartdate.setText(preferences.getString("medicineStartDate", ""));
        medicineenddate.setText(preferences.getString("medicineEndDate", ""));
        everyDayCheckBox.setChecked(preferences.getBoolean("isEveryDay", false));

        // Initialize the adapters again to bind data to dropdowns
        reminderUnitTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, unitType);
        reminderUnitType.setAdapter(reminderUnitTypeAdapter);

        medicineTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, medicineType);
        reminderMedicineType.setAdapter(medicineTypeAdapter);

        // Set the previously selected days
        Set<String> selectedDaysSet = preferences.getStringSet("selectedDays", new HashSet<>());
        selectedDays.clear();
        selectedDays.addAll(selectedDaysSet);

        // Update the UI for selected days
        for (String day : selectedDays) {
            if (day.equals("Sunday")) toggleDayViewState(sunday, true);
            if (day.equals("Monday")) toggleDayViewState(monday, true);
            if (day.equals("Tuesday")) toggleDayViewState(tuesday, true);
            if (day.equals("Wednesday")) toggleDayViewState(wednesday, true);
            if (day.equals("Thursday")) toggleDayViewState(thursday, true);
            if (day.equals("Friday")) toggleDayViewState(friday, true);
            if (day.equals("Saturday")) toggleDayViewState(saturday, true);
        }
    }
    // Method to handle day selection click listeners
    private void setupDayClickListeners() {
        sunday.setOnClickListener(v -> toggleDaySelection(sunday, "Sunday"));
        monday.setOnClickListener(v -> toggleDaySelection(monday, "Monday"));
        tuesday.setOnClickListener(v -> toggleDaySelection(tuesday, "Tuesday"));
        wednesday.setOnClickListener(v -> toggleDaySelection(wednesday, "Wednesday"));
        thursday.setOnClickListener(v -> toggleDaySelection(thursday, "Thursday"));
        friday.setOnClickListener(v -> toggleDaySelection(friday, "Friday"));
        saturday.setOnClickListener(v -> toggleDaySelection(saturday, "Saturday"));
    }

    // Method to toggle day selection
    private void toggleDaySelection(TextView dayView, String dayName) {
        if (selectedDays.contains(dayName)) {
            // Deselect the day
            selectedDays.remove(dayName);
            dayView.setBackgroundColor(Color.TRANSPARENT); // Or set to original background
            dayView.setTextColor(getResources().getColor(R.color.primaryColor)); // Reset text color
        } else {
            // Select the day
            selectedDays.add(dayName);
            dayView.setBackgroundResource(R.drawable.rounded_selected_day);
            dayView.setTextColor(Color.WHITE); // Set text color to indicate selection
        }
    }

    // Method to select all days (when "Every day" checkbox is checked)
    private void selectAllDays() {
        selectedDays.clear();
        selectedDays.add("Sunday");
        selectedDays.add("Monday");
        selectedDays.add("Tuesday");
        selectedDays.add("Wednesday");
        selectedDays.add("Thursday");
        selectedDays.add("Friday");
        selectedDays.add("Saturday");

        // Set all TextViews as selected
        toggleDayViewState(sunday, true);
        toggleDayViewState(monday, true);
        toggleDayViewState(tuesday, true);
        toggleDayViewState(wednesday, true);
        toggleDayViewState(thursday, true);
        toggleDayViewState(friday, true);
        toggleDayViewState(saturday, true);
    }

    // Method to deselect all days (when "Every day" checkbox is unchecked)
    private void deselectAllDays() {

        // Set all TextViews as unselected
        toggleDayViewState(sunday, false);
        toggleDayViewState(monday, false);
        toggleDayViewState(tuesday, false);
        toggleDayViewState(wednesday, false);
        toggleDayViewState(thursday, false);
        toggleDayViewState(friday, false);
        toggleDayViewState(saturday, false);


    }

    // Helper method to set the day view selected/unselected state
    private void toggleDayViewState(TextView dayView, boolean isSelected) {
        if (isSelected) {
            dayView.setBackgroundResource(R.drawable.rounded_selected_day);
            dayView.setTextColor(Color.WHITE);
        } else {
            dayView.setBackgroundColor(Color.TRANSPARENT);
            dayView.setTextColor(getResources().getColor(R.color.primaryColor));
        }
    }

    //start date picker
    private void showDatePickerDialog() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                medicine_signin.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    medicinestartdate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    //end date picker
    private void showDatePickerDialogEnd() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                medicine_signin.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    medicineenddate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setMedicineReminder(String medicineName, String time1, String time2, String time3, String startDate, String endDate, String SelectedDays) {
        scheduleAlarmForTime(medicineName, time1, "time1", startDate, endDate, SelectedDays);
        scheduleAlarmForTime(medicineName, time2, "time2", startDate, endDate, SelectedDays);
        scheduleAlarmForTime(medicineName, time3, "time3", startDate, endDate, SelectedDays);

    }
    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAlarmForTime(String medicineName, String time, String timeLabel, String startDate, String endDate, String SelectedDays){
        if (time != null && !time.isEmpty()) {
            try {
                // Log the time format being passed
                Log.d("MedicineReminder", "Received " + timeLabel + ": " + time);

                // Parse the input time string (with AM/PM if present)
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                Date date = inputFormat.parse(time); // Parsing "12:36 PM" or "01:30 PM"

                // Log the parsed Date object
                Log.d("MedicineReminder", "Parsed Date (" + timeLabel + "): " + date.toString());

                // Convert to 24-hour format
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = outputFormat.format(date); // e.g., "15:30"

                // Log the formatted time (24-hour format)
                Log.d("MedicineReminder", "Formatted Time (" + timeLabel + "): " + formattedTime);

                // Split the formatted time into hours and minutes
                String[] timeParts = formattedTime.split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);

                // Set up the calendar object with the desired time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);



                // Log the final calendar time
                Log.d("MedicineReminder", "Alarm set for " + timeLabel + ": " + calendar.getTime().toString());

                // Create the intent for the AlarmReceiver
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("medicine_name", medicineName);
                intent.putExtra("start_date", startDate);
                intent.putExtra("end_date", endDate);
                intent.putExtra("selected_days", SelectedDays);

                // Generate a unique request code using current time (or any other unique method)
                int requestCode = (int) System.currentTimeMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // Set up the alarm
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                }
//            } catch (ParseException e) {
//                // Handle parsing error and log it
//                Log.e("MedicineReminder", "ParseException: " + e.getMessage());
//                Toast.makeText(this, "Invalid time format: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Handle any other exceptions and log it
                Log.e("MedicineReminder", "Error: " + e.getMessage());
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
//        else {
//            // Handle the case where time1 is null or empty
//            Log.e("MedicineReminder", "Time string is null or empty");
//            Toast.makeText(this, "Invalid time for reminder", Toast.LENGTH_SHORT).show();
//        }
    }
}