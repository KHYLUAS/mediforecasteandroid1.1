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

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateReminder extends AppCompatActivity {
    // To do: after sending even not changing time and dose it becoming null
    // validation

    private MedicineDatabase db;
    private MedicineDao medicineDao;
    private ExecutorService executorService;

    private AutoCompleteTextView reminderMedicineType, reminderUnitType;
    private EditText medicinenameET;
    private TextView medicinestartdate, medicineenddate, validationMedName, validationUnType, validationMedType, validationSched, validationStart, validationEnd, validationDays;
    private MultiAutoCompleteTextView showSchedule, updateShowSchedule;
    ArrayAdapter<String> reminderUnitTypeAdapter;
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, schedule, title, subtitle;
    private CheckBox everyDayCheckBox;
    // List to store selected days
    private ArrayList<String> selectedDays;
    private ImageView medicinearrow;
    private Button buttonUpdate;
    private int forUpdate;
    String[] unitType = {"IU", "ampoule(s)", "application(s)", "application(s)", "capsule(s)", "drop(s)", "gram(s)",
            "injection(s)", "milligram(s)", "milliliter(s)", "mm", "packet(s)", "packet(s)", "patch(es)",
            "pessary(ies)", "piece(s)", "pill(s)", "portion(s)", "puff(s)", "spray(s)", "suppository(ies)",
            "tablespoon(s)", "teaspoon(s)", "unit(s)", "vaginal capsule(s)", "vaginal insert(s)",
            "vaginal tablet(s)", "µg"};
    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection", "Inhaler", "Drop", "Suppositories", "Tropical Medicines"};
    ArrayAdapter<String> medicineTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reminder);

        db = MedicineDatabase.getInstance(this);
        medicineDao = db.medicineDao();
        executorService = Executors.newSingleThreadExecutor();

        medicinestartdate = findViewById(R.id.medicinestartdate);
        buttonUpdate = findViewById(R.id.buttonupdate);
        reminderMedicineType = findViewById(R.id.reminderMedicineType);
        medicinenameET = findViewById(R.id.medicinename);
        medicineenddate = findViewById(R.id.medicineenddate);
        schedule = findViewById(R.id.schedule);
        medicinearrow = findViewById(R.id.medicinearrow);
        reminderUnitType = findViewById(R.id.unit);
        everyDayCheckBox = findViewById(R.id.every_day);
        showSchedule = findViewById(R.id.showSchedule);
        subtitle = findViewById(R.id.subTitle);
        title = findViewById(R.id.Header);
        validationMedName = findViewById(R.id.validationMedName);
        validationUnType= findViewById(R.id.validationUnType);
        validationMedType = findViewById(R.id.validationMedType);
        validationSched = findViewById(R.id.validationSched);
        validationStart = findViewById(R.id.validationStart);
        validationEnd = findViewById(R.id.validationEnd);
        validationDays = findViewById(R.id.validationDays);
        updateShowSchedule = findViewById(R.id.updateShowSchedule);

        //bind days
        sunday = findViewById(R.id.dv_sunday);
        monday = findViewById(R.id.dv_monday);
        tuesday = findViewById(R.id.dv_tuesday);
        wednesday = findViewById(R.id.dv_wednesday);
        thursday = findViewById(R.id.dv_thursday);
        friday = findViewById(R.id.dv_friday);
        saturday = findViewById(R.id.dv_saturday);

        // Setup initial selectedDays
        selectedDays = new ArrayList<>();

        //For Viewing Only
        boolean viewMedicine = getIntent().getBooleanExtra("MEDICINE_VIEW", false);
        if (viewMedicine) {
            setupViewOnlyMode();

        }

        //id intent from MedicineAdapter
       int medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);
        if (medicineId != -1) {
            fetchMedicineReminder(medicineId);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsMedId", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("MEDICINE_ID", medicineId);
            editor.apply();  // Apply the changes
        }
        Log.d("UpdateReminder", "Medicine ID on Create: " + medicineId);


        setupDayClickListeners();

        everyDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Select all days if "Every day" is checked
                selectAllDays();
            } else {
                // Deselect all days if "Every day" is unchecked
                deselectAllDays();
            }
        });
        // Setup adapters for unit types and medicine types
        setupAdapters();

        schedule.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateReminder.this, medicineschedule.class);
            intent.putExtra("isUpdate", true);
            intent.putExtra("unitType", reminderUnitType.getText().toString().trim());
            startActivity(intent);
            finish();
        });
        medicinestartdate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        medicineenddate.setOnClickListener(v -> {
            showDatePickerDialogEnd();
        });
        medicinearrow.setOnClickListener(v -> {
            clearForm();
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsMedId", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("MEDICINE_ID");  // Remove the medicine ID
            editor.apply();
            Intent intent = new Intent(UpdateReminder.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "UPDATEREMINDER");
            startActivity(intent);
            finish();
        });
        buttonUpdate.setOnClickListener(v -> {
            updateMedicineDetails();
        });

        Intent intent = getIntent();
        boolean updating = intent.getBooleanExtra("updating", false);
        if(updating){
           setUpdateShowSchedule();
        }else{
            Log.d("MedicineSchedule", "Your Fake");
        }

    }


    private void fetchMedicineReminder(int id) {
        executorService.execute(() -> {
            Medicine medicine = medicineDao.getMedicineById(id);
            runOnUiThread(() -> {
                if (medicine != null) {
                    // Clear previously selected days
                    selectedDays.clear();
                    clearDayShades(); // Clear previous shades

                    medicinenameET.setText(medicine.getMedicineName());
                    reminderMedicineType.setText(medicine.getMedicineType(), false);
                    reminderUnitType.setText(medicine.getUnitType(), false);
                    medicinestartdate.setText(medicine.getStartDate());
                    medicineenddate.setText(medicine.getEndDate());
                    schedule.setText(medicine.getSchedule());

                    StringBuilder scheduleDetailss = new StringBuilder();

                    if (medicine.getSchedule().equals("Once Daily")) {
                        scheduleDetailss.append("<b>First Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ")
                                .append(medicine.getTime1())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose1());
                        showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
                    } else if (medicine.getSchedule().equals("Twice Daily")) {
                        scheduleDetailss.append("<b>First Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ").append(medicine.getTime1())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose1())
                                .append("<br>")
                                .append("<br>")
                                .append("<b>Second Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ").append(medicine.getTime2())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose2());
                        showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
                    } else if (medicine.getSchedule().equals("Thrice Daily")) {
                        scheduleDetailss.append("<b>First Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ").append(medicine.getTime1())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose1())
                                .append("<br>")
                                .append("<br>")
                                .append("<b>Second Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ").append(medicine.getTime2())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose2())
                                .append("<br>")
                                .append("<br>")
                                .append("<b>Third Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ").append(medicine.getTime3())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose3());
                        showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
                    }else if(medicine.getSchedule().equals("Every Hours")){
                        scheduleDetailss.append("<b>Takes</b>")
                                .append("<br>")
                                .append("<b>Time: </b> Every ")
                                .append(medicine.getTime1())
                                .append(" Hour(s)")
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose1());
                        showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
                    }
                    showSchedule.setText(scheduleDetailss.toString());
                    showSchedule.setText(Html.fromHtml(scheduleDetailss.toString(), Html.FROM_HTML_MODE_COMPACT));



                    String[] selectedDaysArray = medicine.getSelectedDays().split(", ");
                    boolean allDaysSelected = true; // Assume all are selected initially
                    List<String> daysOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

                    // Shade selected days
                    for (String day : daysOfWeek) {
                        if (Arrays.asList(selectedDaysArray).contains(day)) {
                            selectedDays.add(day);
                            shadeDay(day); // Shade the selected day
                        } else {
                            allDaysSelected = false; // If any day is not selected, set to false
                        }
                    }

                    // Update checkbox based on whether all days are selected
                    everyDayCheckBox.setChecked(allDaysSelected);

                    // If all days are selected, shade all days
                    if (allDaysSelected) {
                        shadeAllDays();
                    }
                }
            });

        });
    }

    private void updateMedicineDetails() {
        // Retrieve updated details from input fields
        String updatedMedicineName = medicinenameET.getText().toString();
        String updatedStartDate = medicinestartdate.getText().toString();
        String updatedEndDate = medicineenddate.getText().toString();
        String updatedMedicineType = reminderMedicineType.getText().toString();
        String updatedUnitType = reminderUnitType.getText().toString();
        String updatedSchedule = schedule.getText().toString();
        String updatedSelectedDays = String.join(", ", selectedDays);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsMedId", MODE_PRIVATE);
       int medicineId = sharedPreferences.getInt("MEDICINE_ID", -1);
        Log.d("MedicineID", "Retrieved medicineId onCreate: " + medicineId);
//        int medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);
//        Log.d("UpdateMedicineDetails", "Medicine ID during update: " + medicineId);
//        Log.d("UpdateMedicineDetails", "Passing Medicine ID during update: " + forUpdate);
        Intent intent = getIntent();
        boolean updating = intent.getBooleanExtra("updating", false);
        boolean isViewMode = intent.getBooleanExtra("MEDICINE_VIEW", false);
        Log.d("UpdateMedicine", "Is View Mode: " + isViewMode);
        if (medicineId == -1) {
            Log.e("UpdateReminder", "Error: No medicine ID passed.");
            Toast.makeText(this, "No medicine ID found.", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("UpdateMedicineDetails", "Intent extras: " + getIntent().getExtras());



        // Perform database query in the background
        executorService.execute(() -> {
            String updateTime1, updateDose1, updateTime2, updateDose2, updateTime3, updateDose3;
            if (updating) {
                updateTime1 = intent.getStringExtra("time1");
                updateDose1 = intent.getStringExtra("dose1");
                updateTime2 = intent.getStringExtra("time2");
                updateDose2 = intent.getStringExtra("dose2");
                updateTime3 = intent.getStringExtra("time3");
                updateDose3 = intent.getStringExtra("dose3");
            }else{
                // If not updating, fetch the medicine from the database
                Medicine medicine = medicineDao.getMedicineById(medicineId);

                // Ensure the medicine exists before proceeding
                if (medicine == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateReminder.this, "Medicine not found", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                // Fetch time and dose details from the database
                updateTime1 = medicine.getTime1();
                updateDose1 = medicine.getDose1();
                updateTime2 = medicine.getTime2();
                updateDose2 = medicine.getDose2();
                updateTime3 = medicine.getTime3();
                updateDose3 = medicine.getDose3();
            }

            if (updatedMedicineName.isEmpty()) {
                showToastAndLog("medicineName is required");
                validationMedName.setVisibility(View.VISIBLE);
                return;
            }
            if (updatedUnitType.isEmpty()) {
                showToastAndLog("unitType is required");
                validationUnType.setVisibility(View.VISIBLE);
                return;
            }
            if (updatedMedicineType.isEmpty()) {
                showToastAndLog("medicineType is required");
                validationMedType.setVisibility(View.VISIBLE);
                return;
            }
            if (updatedStartDate.isEmpty()) {
                showToastAndLog("startDate is required");
                validationStart.setVisibility(View.VISIBLE);
                return;
            }
            if (updatedEndDate.isEmpty()) {
                showToastAndLog("endDate is required");
                validationEnd.setVisibility(View.VISIBLE);
                return;
            }
            if (updatedSchedule.equals("Choose Schedule")) {
                showToastAndLog("schedules is required");
                validationSched.setVisibility(View.VISIBLE);
                return;
            }

            // Perform date validation
            if (!isValidDate(updatedStartDate) || !isDateInFutureOrToday(updatedStartDate)) {
                showToastAndLog("startDate must be today or in the future (d/M/yyyy)");
                validationStart.setText("Start Date must today or in the future");
                validationStart.setVisibility(View.VISIBLE);
                return;
            }
            if (!isValidDate(updatedEndDate) || !isDateInFutureOrToday(updatedEndDate)) {
                showToastAndLog("endDate must be today or in the future (d/M/yyyy)");
                validationEnd.setText("End Date must today or in the future");
                validationEnd.setVisibility(View.VISIBLE);
                return;
            }

            // Update medicine in database
            Medicine updatedMedicine = new Medicine(
                    medicineId, updatedMedicineName,updatedStartDate, updatedEndDate, updatedMedicineType,
                    updatedUnitType, updatedSchedule, updatedSelectedDays,
                    updateTime1, updateDose1, updateTime2, updateDose2, updateTime3, updateDose3);

                // Update in database
                medicineDao.update(updatedMedicine);
                rescheduleAlarm(updatedMedicine);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("MEDICINE_ID");  // Remove the medicine ID
                editor.apply();
                // Success feedback
                runOnUiThread(() -> {
                    Intent intents = new Intent(UpdateReminder.this , Menubar.class);
                    intents.putExtra("EXTRA_FRAGMENT", "REMINDERDB");
                    Toast.makeText(UpdateReminder.this, "Medicine updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
        });
    }
    private void setUpdateShowSchedule(){
        Log.d("MedicineSchedule", "Updating Now");
        StringBuilder scheduleDetail = new StringBuilder();
        Intent intents = getIntent();
        if (intents.hasExtra("onceDaily")) {
            String frequency = intents.getStringExtra("onceDaily");
            String dose1 = intents.getStringExtra("dose1");
            String time1 = intents.getStringExtra("time1");
            schedule.setText(frequency);
            scheduleDetail.append("<b>First Take</b>")
                    .append("<br>")
                    .append("<b>Time:</b> ")
                    .append(time1)
                    .append("<br>")
                    .append("<b>Dose:</b> ").append(dose1);
            showSchedule.setVisibility(MultiAutoCompleteTextView.GONE);
            updateShowSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
        } else if (intents.hasExtra("twiceDaily")) {
            String time1 = intents.getStringExtra("time1");
            String time2 = intents.getStringExtra("time2");
            String dose1 = intents.getStringExtra("dose1");
            String dose2 = intents.getStringExtra("dose2");
            String frequency = intents.getStringExtra("twiceDaily");
            schedule.setText(frequency);
            scheduleDetail.append("<b>First Take</b>")
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
            showSchedule.setVisibility(MultiAutoCompleteTextView.GONE);
            updateShowSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
        } else if (intents.hasExtra("thriceDaily")) {
            String time1 = intents.getStringExtra("time1");
            String time2 = intents.getStringExtra("time2");
            String time3 = intents.getStringExtra("time3");
            String dose1 = intents.getStringExtra("dose1");
            String dose2 = intents.getStringExtra("dose2");
            String dose3 = intents.getStringExtra("dose3");
            String frequency = intents.getStringExtra("thriceDaily");
            schedule.setText(frequency);
            scheduleDetail.append("<b>First Take</b>")
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
            showSchedule.setVisibility(MultiAutoCompleteTextView.GONE);
            updateShowSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
        }
        updateShowSchedule.setText(scheduleDetail.toString());
        updateShowSchedule.setText(Html.fromHtml(scheduleDetail.toString(), Html.FROM_HTML_MODE_COMPACT));
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

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("Updating", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("medicineName", medicinenameET.getText().toString().trim());
        editor.putString("medicineType", reminderMedicineType.getText().toString().trim());
        editor.putString("medicineStartDate", medicinestartdate.getText().toString().trim());
        editor.putString("medicineEndDate", medicineenddate.getText().toString().trim());
        editor.putString("selectedUnitType", reminderUnitType.getText().toString().trim());
        editor.putBoolean("isEveryDay", everyDayCheckBox.isChecked());
        editor.putStringSet("selectedDays", new HashSet<>(selectedDays));

//        Intent intent = getIntent();
//        if(intent.hasExtra("onceDaily")){
//            editor.putString("frequency", "onceDaily");
//            editor.putString("dose1", intent.getStringExtra("dose1"));
//            editor.putString("time1", intent.getStringExtra("time1"));
//        }else if (intent.hasExtra("twiceDaily")) {
//            editor.putString("frequency", "twiceDaily");
//            editor.putString("dose1", intent.getStringExtra("dose1"));
//            editor.putString("time1", intent.getStringExtra("time1"));
//            editor.putString("dose2", intent.getStringExtra("dose2"));
//            editor.putString("time2", intent.getStringExtra("time2"));
//        } else if (intent.hasExtra("thriceDaily")) {
//            editor.putString("frequency", "thriceDaily");
//            editor.putString("dose1", intent.getStringExtra("dose1"));
//            editor.putString("time1", intent.getStringExtra("time1"));
//            editor.putString("dose2", intent.getStringExtra("dose2"));
//            editor.putString("time2", intent.getStringExtra("time2"));
//            editor.putString("dose3", intent.getStringExtra("dose3"));
//            editor.putString("time3", intent.getStringExtra("time3"));
//        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Change MyPrefs
        // Restore the values from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Updating", MODE_PRIVATE);
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
//        String frequency = preferences.getString("frequency", "");
//        StringBuilder scheduleDetails = new StringBuilder();
//
//        if ("onceDaily".equals(frequency)) {
//            scheduleDetails.append("<b>First Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time1", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose1", ""));
//        } else if ("twiceDaily".equals(frequency)) {
//            scheduleDetails.append("<b>First Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time1", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose1", ""))
//                    .append("<br><br>")
//                    .append("<b>Second Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time2", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose2", ""));
//        } else if ("thriceDaily".equals(frequency)) {
//            scheduleDetails.append("<b>First Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time1", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose1", ""))
//                    .append("<br><br>")
//                    .append("<b>Second Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time2", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose2", ""))
//                    .append("<br><br>")
//                    .append("<b>Third Take</b>")
//                    .append("<br>")
//                    .append("<b>Time:</b> ")
//                    .append(preferences.getString("time3", ""))
//                    .append("<br>")
//                    .append("<b>Dose:</b> ")
//                    .append(preferences.getString("dose3", ""));
//        }
//
//        // Set the schedule text if there is data to display
//        if (scheduleDetails.length() > 0) {
//            showSchedule.setText(Html.fromHtml(scheduleDetails.toString(), Html.FROM_HTML_MODE_COMPACT));
//            showSchedule.setVisibility(View.VISIBLE);
//            updateShowSchedule.setVisibility(View.GONE);
//        } else {
//            showSchedule.setVisibility(View.GONE);
//            updateShowSchedule.setVisibility(View.VISIBLE);
//        }
    }

    private void showDatePickerDialog() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateReminder.this,
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
                UpdateReminder.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    medicineenddate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }
    private void displayUpdatedScheduleDetails() {
        StringBuilder scheduleDetails = new StringBuilder();
        Intent intent = getIntent();

        // Check for schedule type and build the display details accordingly
        if (intent.hasExtra("onceDaily")) {
            scheduleDetails.append("<b>First Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time1"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose1"));
        } else if (intent.hasExtra("twiceDaily")) {
            scheduleDetails.append("<b>First Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time1"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose1"))
                    .append("<br><br><b>Second Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time2"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose2"));
        } else if (intent.hasExtra("thriceDaily")) {
            scheduleDetails.append("<b>First Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time1"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose1"))
                    .append("<br><br><b>Second Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time2"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose2"))
                    .append("<br><br><b>Third Take</b><br>")
                    .append("<b>Time:</b> ").append(intent.getStringExtra("time3"))
                    .append("<br><b>Dose:</b> ").append(intent.getStringExtra("dose3"));
        }

        // Set the HTML-formatted text to updateShowSchedule
        updateShowSchedule.setText(Html.fromHtml(scheduleDetails.toString(), Html.FROM_HTML_MODE_COMPACT));
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

    private void setupViewOnlyMode() {
        buttonUpdate.setVisibility(View.GONE);
        subtitle.setVisibility(View.GONE);
        title.setText("View a Reminder");
        medicinenameET.setFocusable(false);
        reminderMedicineType.setInputType(View.AUTOFILL_TYPE_NONE);
        medicinestartdate.setEnabled(false);
        medicineenddate.setEnabled(false);
        schedule.setEnabled(false);
        reminderUnitType.setInputType(View.AUTOFILL_TYPE_NONE);
        everyDayCheckBox.setFocusable(false);
        validationSched.setVisibility(View.GONE);
    }

    private void setupAdapters() {
        // Initialize ArrayAdapter for unit types
        reminderUnitTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, unitType);
        reminderUnitType.setAdapter(reminderUnitTypeAdapter);
        reminderUnitType.setOnClickListener(v -> reminderUnitType.showDropDown());
        reminderUnitType.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedMedicineUnitType = (String) adapterView.getItemAtPosition(position);
            Toast.makeText(UpdateReminder.this, "Medicine Unit Type: " + selectedMedicineUnitType, Toast.LENGTH_SHORT).show();
        });

        // Initialize ArrayAdapter for medicine types
        medicineTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, medicineType);
        reminderMedicineType.setAdapter(medicineTypeAdapter);
    }
    // Method to clear previous shades
    private void clearDayShades() {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : daysOfWeek) {
            unshadeDay(day); // Unshade all days initially
        }
    }

    // Method to shade all days (if needed)
    private void shadeAllDays() {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : daysOfWeek) {
            shadeDay(day);
        }
    }

    // Method to shade the selected day
    private void shadeDay(String dayName) {
        switch (dayName) {
            case "Sunday":
                sunday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Monday":
                monday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Tuesday":
                tuesday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Wednesday":
                wednesday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Thursday":
                thursday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Friday":
                friday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
            case "Saturday":
                saturday.setBackgroundResource(R.drawable.rounded_selected_day);
                break;
        }
    }

    // Method to unshade the day
    private void unshadeDay(String dayName) {
        switch (dayName) {
            case "Sunday":
                sunday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Monday":
                monday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Tuesday":
                tuesday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Wednesday":
                wednesday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Thursday":
                thursday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Friday":
                friday.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "Saturday":
                saturday.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
    }

    //Still need to check if its working
    @SuppressLint("ScheduleExactAlarm")
    private void rescheduleAlarm(Medicine updatedMedicine) {
        // Ensure the medicine name and times are valid
        String medicineName = updatedMedicine.getMedicineName();
        String startDate = updatedMedicine.getStartDate();
        String endDate = updatedMedicine.getEndDate();
        String selectedDays = String.join(", ", updatedMedicine.getSelectedDays());

        // Retrieve all times and doses
        String[] times = {updatedMedicine.getTime1(), updatedMedicine.getTime2(), updatedMedicine.getTime3()};
        String[] doses = {updatedMedicine.getDose1(), updatedMedicine.getDose2(), updatedMedicine.getDose3()};

        if (medicineName != null && times != null) {
            for (int i = 0; i < times.length; i++) {
                String timeString = times[i];
                String dose = doses[i];

                if (timeString != null && !timeString.isEmpty()) {
                    try {
                        String timeLabel = "Dose Time " + (i + 1);

                        // Log the received time
                        Log.d("MedicineReminder", "Received " + timeLabel + ": " + timeString);

                        // Parse the input time string (with AM/PM if present)
                        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        Date date = inputFormat.parse(timeString); // Parsing "12:36 PM" or "01:30 PM"

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

                        // Log the updated calendar time
                        Log.d("MedicineReminder", "Updated Alarm Calendar Time (" + timeLabel + "): " + calendar.getTime().toString());

                        // Retrieve the requestCode from SharedPreferences for this medicine and time
                        SharedPreferences prefs = getSharedPreferences("MedicinePrefs", MODE_PRIVATE);
                        int requestCode = prefs.getInt(medicineName + "_requestCode_" + i, -1); // Save with index to distinguish between times

                        if (requestCode != -1) {
                            // Cancel the existing alarm using the saved requestCode
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            if (alarmManager != null) {
                                Intent intent = new Intent(this, AlarmReceiver.class);
                                intent.putExtra("medicine_name", medicineName);
                                intent.putExtra("dose", dose);
                                intent.putExtra("time", timeString);
                                intent.putExtra("start_date", startDate);
                                intent.putExtra("end_date", endDate);
                                intent.putExtra("selected_days", selectedDays);

                                // Use the stored requestCode to identify the pending intent
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                                // Cancel the previous alarm
                                alarmManager.cancel(pendingIntent);
                                Log.d("MedicineReminder", "Previous alarm canceled for requestCode: " + requestCode);

                                // Set the new alarm
                                alarmManager.setExactAndAllowWhileIdle(
                                        AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis(),
                                        pendingIntent
                                );

                                Log.d("MedicineReminder", "New alarm scheduled for: " + calendar.getTime().toString());
                            } else {
                                Log.e("MedicineReminder", "AlarmManager is null, cannot reschedule");
                            }
                        } else {
                            Log.e("MedicineReminder", "No requestCode found for medicine: " + medicineName + " at time index " + i);
                        }
                    } catch (ParseException e) {
                        Log.e("MedicineReminder", "Error parsing the time string: " + e.getMessage(), e);
                    }
                } else {
                    Log.e("MedicineReminder", "Error: Time string " + (i + 1) + " is null or empty");
                }
            }
        } else {
            Log.e("MedicineReminder", "Error: Medicine name or times array is null");
        }
    }


}