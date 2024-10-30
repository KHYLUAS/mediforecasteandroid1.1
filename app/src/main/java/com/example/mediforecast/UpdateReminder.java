package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateReminder extends AppCompatActivity {

    private MedicineDatabase db;
    private MedicineDao medicineDao;
    private ExecutorService executorService;

    private AutoCompleteTextView reminderMedicineType, reminderUnitType;
    private EditText medicinenameET;
    private TextView medicinestartdate, medicineenddate;
    private MultiAutoCompleteTextView showSchedule;
    ArrayAdapter<String> reminderUnitTypeAdapter;
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, schedule, title, subtitle;
    private CheckBox everyDayCheckBox;
    // List to store selected days
    private ArrayList<String> selectedDays;
    private ImageView medicinearrow;
    private Button buttonUpdate;

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
        }


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
            Intent intent = new Intent(UpdateReminder.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "UPDATEREMINDER");
            startActivity(intent);
            finish();
        });
        buttonUpdate.setOnClickListener(v -> {
            updateMedicineDetails();
        });
        StringBuilder scheduleDetails = new StringBuilder();
        Intent intents = getIntent();
        if (intents.hasExtra("onceDaily")) {
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
        } else if (intents.hasExtra("twiceDaily")) {
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
        } else if (intents.hasExtra("thriceDaily")) {
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

                    StringBuilder scheduleDetails = new StringBuilder();
                    if (medicine.getSchedule().equals("Once Daily")) {
                        scheduleDetails.append("<b>First Take</b>")
                                .append("<br>")
                                .append("<b>Time:</b> ")
                                .append(medicine.getTime1())
                                .append("<br>")
                                .append("<b>Dose:</b> ").append(medicine.getDose1());
                        showSchedule.setVisibility(MultiAutoCompleteTextView.VISIBLE);
                    } else if (medicine.getSchedule().equals("Twice Daily")) {
                        scheduleDetails.append("<b>First Take</b>")
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
                        scheduleDetails.append("<b>First Take</b>")
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
                    }
                    showSchedule.setText(scheduleDetails.toString());
                    showSchedule.setText(Html.fromHtml(scheduleDetails.toString(), Html.FROM_HTML_MODE_COMPACT));



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

    //    // Method to update the medicine details in the database
    private void updateMedicineDetails() {
        String updatedMedicineName = medicinenameET.getText().toString();
        String updatedStartDate = medicinestartdate.getText().toString();
        String updatedEndDate = medicineenddate.getText().toString();
        String updatedMedicineType = reminderMedicineType.getText().toString();
        String updatedUnitType = reminderUnitType.getText().toString();
        String updatedSchedule = schedule.getText().toString();
        String updatedSelectedDays = String.join(",", selectedDays);

        Intent intents = getIntent();
        int medicineId = intents.getIntExtra("medicineId", -1);
        // Check if the medicine ID is valid
        if (medicineId == -1) {
            Toast.makeText(this, "Error: Medicine ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Medicine existingMedicine = medicineDao.getMedicineById(medicineId); // Assuming this method exists
        if (existingMedicine == null) {
            Toast.makeText(this, "Error: Medicine not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize time and dose variables
        String updateTime1 = null;
        String updateDose1 = null;
        String updateTime2 = null;
        String updateDose2 = null;
        String updateTime3 = null;
        String updateDose3 = null;

        // Fetch time and dose details based on the frequency from the Intent
        if (intents.hasExtra("onceDaily")) {
            updateTime1 = intents.getStringExtra("time1");
            updateDose1 = intents.getStringExtra("dose1");
        } else if (intents.hasExtra("twiceDaily")) {
            updateTime1 = intents.getStringExtra("time1");
            updateDose1 = intents.getStringExtra("dose1");
            updateTime2 = intents.getStringExtra("time2");
            updateDose2 = intents.getStringExtra("dose2");
        } else if (intents.hasExtra("thriceDaily")) {
            updateTime1 = intents.getStringExtra("time1");
            updateDose1 = intents.getStringExtra("dose1");
            updateTime2 = intents.getStringExtra("time2");
            updateDose2 = intents.getStringExtra("dose2");
            updateTime3 = intents.getStringExtra("time3");
            updateDose3 = intents.getStringExtra("dose3");
        }


        Medicine updatedMedicine = new Medicine(updatedMedicineName, updatedStartDate,
                updatedEndDate, updatedMedicineType, updatedUnitType, updatedSchedule, updatedSelectedDays,
                updateTime1, updateDose1, updateTime2, updateDose2, updateTime3, updateDose3); // Assuming Medicine has this constructor

        executorService.execute(() -> {
            medicineDao.update(updatedMedicine); // Assuming this method exists
            runOnUiThread(() -> {
                Toast.makeText(UpdateReminder.this, "Successfully Update Reminder", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateReminder.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        });
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

    private void clearForm() {
        medicinenameET.setText("");
        medicinestartdate.setText("");
        medicineenddate.setText("");
        reminderMedicineType.setText("");
        reminderUnitType.setText("");
        schedule.setText("");
        selectedDays.clear();
        deselectAllDays();

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
}