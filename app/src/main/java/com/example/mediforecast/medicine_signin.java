package com.example.mediforecast;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class medicine_signin extends AppCompatActivity {

    private AutoCompleteTextView reminderMedicineType, reminderUnitType;
    private EditText medicinenameET;
    private TextView medicinestartdate, medicineenddate, validationMedName, validationUnType, validationMedType, validationSched, validationStart, validationEnd, validationDays;
    private MultiAutoCompleteTextView showSchedule;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    String[] unitType = {"IU", "ampoule(s)","application(s)", "capsule(s)","drop(s)","gram(s)",
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
        validationMedName = findViewById(R.id.validationMedName);
        validationUnType= findViewById(R.id.validationUnType);
        validationMedType = findViewById(R.id.validationMedType);
        validationSched = findViewById(R.id.validationSched);
        validationStart = findViewById(R.id.validationStart);
        validationEnd = findViewById(R.id.validationEnd);
        validationDays = findViewById(R.id.validationDays);

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
       }else if(intents.hasExtra("everyHours")){
           // I Change this with update 1:23 AM nov 10
           String time1 = intents.getStringExtra("time1");
           String dose1 = intents.getStringExtra("dose1");
           String frequency = intents.getStringExtra("everyHours");
           schedule.setText(frequency);
           scheduleDetails.append("<b>Takes</b>")
                   .append("<br>")
                   .append("<b>Time: </b> Every ")
                   .append(time1)
                   .append(" Hour(s)")
                   .append("<br>")
                   .append("<b>Dose:</b> ").append(dose1);
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
            // I Change this with update 1:23 AM nov 10
            if(selectedMedicineUnitType != null){
                validationSched.setVisibility(View.GONE);
            }else{
                validationSched.setVisibility(View.VISIBLE);
            }
        });

        // Initialize ArrayAdapter for medicine types
        medicineTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, medicineType);
        reminderMedicineType.setAdapter(medicineTypeAdapter);

        // Check and request permission for scheduling alarms on Android 12 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // Check if the app has permission to schedule exact alarms
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                // If permission is not granted, request it
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_PERMISSION);
            }
//            else {
//                // Permission already granted
//                Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//            }
        }
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION_PERMISSION);
            }
//            else {
//                // Permission already granted
//                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
//            }
        } else {
            // On lower versions, permission is not required, you can send notifications directly
            Toast.makeText(this, "Notification permission not required", Toast.LENGTH_SHORT).show();
        }

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

            if (medicineName.isEmpty()) {
                String message = "medicineName is required";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Log.d("ValidationError", message);
                validationMedName.setVisibility(View.VISIBLE);
                return;
            }
            if (unitType.isEmpty()) {
            String message = "unitType is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            schedule.setEnabled(false);
            validationUnType.setVisibility(View.VISIBLE);
            return;
         }
            if (medicineType.isEmpty()) {
            String message = "medicineType is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            validationMedType.setVisibility(View.VISIBLE);
            return;
           }
            if (startDate.isEmpty()) {
            String message = "startDate is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            validationStart.setVisibility(View.VISIBLE);
            return;
          }
         if (endDate.isEmpty()) {
            String message = "endDate is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            validationEnd.setVisibility(View.VISIBLE);
            return;
         }
         if (schedules.equals("Choose Schedule")) {
            String message = "schedules is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            validationSched.setText("Schedule is required!");
            validationSched.setVisibility(View.VISIBLE);
              return;
         }
         if (time1 == null) {
            String message = "time1 is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            return;
         }
         if (dose1 == null) {
            String message = "dose1 is required";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d("ValidationError", message);
            return;
         }
            if (!isValidDate(startDate) || !isDateInFutureOrToday(startDate)) {
                showToastAndLog("startDate must be today or in the future (dd/MM/yyyy)");
                validationStart.setText("Start Date must today or in the future");
                validationStart.setVisibility(View.VISIBLE);
                return;
            }
          if (!isValidDate(endDate) || !isDateInFutureOrToday(endDate)) {
                showToastAndLog("endDate must be today or in the future (dd/MM/yyyy)");
              validationEnd.setText("End Date must today or in the future");
              validationEnd.setVisibility(View.VISIBLE);
                return;
          }
          if(SelectedDays.equals(", ")){
              validationDays.setVisibility(View.VISIBLE);
          }


            // Create a new Medicine object
            Medicine medicine = new Medicine(medicineName, startDate, endDate, medicineType, unitType, schedules, SelectedDays,
                    time1, dose1, time2, dose2, time3, dose3);

            // Insert into the database
            medicineRepository.insert(medicine);
            String everyHours = intents.getStringExtra("everyHours");
            String everyHoursVal = intents.getStringExtra("everyHoursVal");

            Toast.makeText(this, "Medicine added successfully!", Toast.LENGTH_SHORT).show();
            setMedicineReminder(medicineName, time1, time2, time3, startDate, endDate, SelectedDays, everyHoursVal); // Pass value to schedule reminder

            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            clearForm();

            showSchedule.setText("");
            showSchedule.setVisibility(View.GONE);

            Intent intent = new Intent(medicine_signin.this, splashscreenaddalarm.class);
            startActivity(intent);
            finish();
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
        schedule.setEnabled(!reminderUnitType.getText().toString().isEmpty());
        reminderUnitType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                schedule.setEnabled(!s.toString().isEmpty());

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (resultCode == RESULT_OK) {
                // Recheck permission status
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    // Check if we can schedule exact alarms
                    if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
                        // Permission granted
                        Toast.makeText(this, "Permission granted to schedule alarms", Toast.LENGTH_SHORT).show();
                    } else {
                        // Permission still denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Handle case when the user does not grant the permission
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            Log.d("DateValidation", "Parsed date: " + parsedDate);
            return true;
        } catch (DateTimeParseException e) {
            Log.d("DateValidation", "Invalid date format: " + date);
            return false;
        }
    }

    private boolean isDateInFutureOrToday(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        LocalDate today = LocalDate.now();
        Log.d("DateValidation", "Parsed date: " + parsedDate + ", Today: " + today);
        return !parsedDate.isBefore(today);
    }
    private void showToastAndLog(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d("ValidationError", message);
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
        showSchedule.setText("");
        showSchedule.setVisibility(MultiAutoCompleteTextView.GONE);

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

        Intent intent = getIntent();
        if(intent.hasExtra("onceDaily")){
            editor.putString("frequency", "onceDaily");
            editor.putString("dose1", intent.getStringExtra("dose1"));
            editor.putString("time1", intent.getStringExtra("time1"));
        }else if (intent.hasExtra("twiceDaily")) {
            editor.putString("frequency", "twiceDaily");
            editor.putString("dose1", intent.getStringExtra("dose1"));
            editor.putString("time1", intent.getStringExtra("time1"));
            editor.putString("dose2", intent.getStringExtra("dose2"));
            editor.putString("time2", intent.getStringExtra("time2"));
        } else if (intent.hasExtra("thriceDaily")) {
            editor.putString("frequency", "thriceDaily");
            editor.putString("dose1", intent.getStringExtra("dose1"));
            editor.putString("time1", intent.getStringExtra("time1"));
            editor.putString("dose2", intent.getStringExtra("dose2"));
            editor.putString("time2", intent.getStringExtra("time2"));
            editor.putString("dose3", intent.getStringExtra("dose3"));
            editor.putString("time3", intent.getStringExtra("time3"));
        } else if (intent.hasExtra("everyHours")) {
            // I Change this with update 1:23 AM nov 10
            editor.putString("frequency", "everyHours");
            editor.putString("dose1", intent.getStringExtra("dose1"));
            editor.putString("time1", intent.getStringExtra("time1"));
        }
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
        // Restore frequency-related data and update UI
        String frequency = preferences.getString("frequency", "");
        StringBuilder scheduleDetails = new StringBuilder();

        if ("onceDaily".equals(frequency)) {
            scheduleDetails.append("<b>First Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time1", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose1", ""));
        } else if ("twiceDaily".equals(frequency)) {
            scheduleDetails.append("<b>First Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time1", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose1", ""))
                    .append("<br><br>")
                    .append("<b>Second Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time2", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose2", ""));
        } else if ("thriceDaily".equals(frequency)) {
            scheduleDetails.append("<b>First Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time1", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose1", ""))
                    .append("<br><br>")
                    .append("<b>Second Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time2", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose2", ""))
                    .append("<br><br>")
                    .append("<b>Third Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(preferences.getString("time3", ""))
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose3", ""));
        }else if ("everyHours".equals(frequency)){
            // I Change this with update 1:23 AM nov 10
            scheduleDetails.append("<b>Takes</b>")
                    .append("<br>")
                    .append("<b>Time: </b> Every ")
                    .append(preferences.getString("time1", ""))
                    .append(" Hour(s)")
                    .append("<br>")
                    .append("<b>Dose:</b> ")
                    .append(preferences.getString("dose1", ""));
        }

        // Set the schedule text if there is data to display
//        if (scheduleDetails.length() > 0) {
//            showSchedule.setText(Html.fromHtml(scheduleDetails.toString(), Html.FROM_HTML_MODE_COMPACT));
//            showSchedule.setVisibility(View.VISIBLE);
//        }
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
    private void setMedicineReminder(String medicineName, String time1, String time2, String time3, String startDate, String endDate, String selectedDays, String everyHoursVal) {
        scheduleAlarmForTime(medicineName, time1, "time1", startDate, endDate, selectedDays);
        scheduleAlarmForTime(medicineName, time2, "time2", startDate, endDate, selectedDays);
        scheduleAlarmForTime(medicineName, time3, "time3", startDate, endDate, selectedDays);

        // Check if repeating interval (everyHours) is valid and schedule repeating alarm
        if (everyHoursVal != null && !everyHoursVal.isEmpty()) {
            try {
                // Ensure the value is numeric and parse it as an integer
                int intervalHours = Integer.parseInt(everyHoursVal);

                if (intervalHours > 0) {
                    // Schedule repeating alarm
                    scheduleRepeatingAlarm(medicineName, intervalHours, startDate, endDate, selectedDays);
                } else {
                    Log.e("MedicineReminder", "Invalid interval: must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                // Log the error if it's not a valid integer
                Log.e("MedicineReminder", "Invalid interval format: " + everyHoursVal, e);
                Toast.makeText(this, "Invalid hourly interval format", Toast.LENGTH_SHORT).show();
            }
        }
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
                int requestCode = (medicineName + timeLabel).hashCode();
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
                // Save the requestCode to SharedPreferences for later cancellation
                SharedPreferences prefs = getSharedPreferences("MedicinePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(medicineName + "_requestCode", requestCode);
                editor.apply();

                Log.d("MedicineReminder", "Alarm set with requestCode: " + requestCode);
            } catch (Exception e) {
                // Handle any other exceptions and log it
                Log.e("MedicineReminder", "Error: " + e.getMessage());
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleRepeatingAlarm(String medicineName, int intervalHours, String startDate, String endDate, String selectedDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        try {
            startCal.setTime(dateFormat.parse(startDate));
            endCal.setTime(dateFormat.parse(endDate));
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            Log.e("MedicineReminder", "Unparseable date format. Expected format is d/M/yyyy.", e);
            return;
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medicine_name", medicineName);
        intent.putExtra("interval_hours", intervalHours);
        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);

        int requestCode = (medicineName + "_repeat").hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            Calendar currentCal = Calendar.getInstance();
            Calendar nextTrigger = (Calendar) startCal.clone();
            nextTrigger.set(Calendar.HOUR_OF_DAY, currentCal.get(Calendar.HOUR_OF_DAY));
            nextTrigger.set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE));
            nextTrigger.set(Calendar.SECOND, 0);

            if (nextTrigger.before(currentCal)) {
                nextTrigger.add(Calendar.HOUR_OF_DAY, intervalHours); // Skip past current time if start time has passed
            }

            // Loop to set alarms from startDate until endDate
            while (nextTrigger.before(endCal)) {
                long triggerTime = nextTrigger.getTimeInMillis();

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                Log.d("MedicineReminder", "Repeating alarm set for " + nextTrigger.getTime() + " (every " + intervalHours + " hours)");

                nextTrigger.add(Calendar.HOUR_OF_DAY, intervalHours); // Move to the next interval
            }
        }
    }

//@SuppressLint("ScheduleExactAlarm")
//private void scheduleRepeatingAlarm(String medicineName, int intervalHours, String startDate, String endDate, String selectedDays) {
//    SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
//    Calendar startCal = Calendar.getInstance();
//    Calendar endCal = Calendar.getInstance();
//
//    try {
//        startCal.setTime(dateFormat.parse(startDate));
//        endCal.setTime(dateFormat.parse(endDate));
//        endCal.set(Calendar.HOUR_OF_DAY, 23);
//        endCal.set(Calendar.MINUTE, 59);
//        endCal.set(Calendar.SECOND, 59);
//    } catch (ParseException e) {
//        Log.e("MedicineReminder", "Unparseable date format. Expected format is d/M/yyyy.");
//        return;
//    }
//
//    Intent intent = new Intent(this, AlarmReceiver.class);
//    intent.putExtra("medicine_name", medicineName);
//    intent.putExtra("interval_hours", intervalHours);
//    intent.putExtra("start_date", startDate);
//    intent.putExtra("end_date", endDate);
//
//    int requestCode = (medicineName + "_repeat").hashCode();
//    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//    if (alarmManager != null) {
//        Calendar nextTrigger = (Calendar) startCal.clone(); // Start with the start time
//
//        // Ensure the start date has the correct time components (hour, minute, second)
//        nextTrigger.set(Calendar.HOUR_OF_DAY, 0); // Set to midnight of the start day to avoid issues
//        nextTrigger.set(Calendar.MINUTE, 0);
//        nextTrigger.set(Calendar.SECOND, 0);
//
//        // Loop to set alarms from startDate until endDate
//        while (nextTrigger.before(endCal)) {
//            long triggerTime = nextTrigger.getTimeInMillis();
//
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
//            Log.d("MedicineReminder", "Repeating alarm set for " + nextTrigger.getTime() + " (every " + intervalHours + " hours)");
//
//            nextTrigger.add(Calendar.HOUR_OF_DAY, intervalHours); // Move to the next interval
//        }
//    }
//}


}