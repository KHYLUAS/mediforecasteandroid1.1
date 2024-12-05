package com.example.mediforecast;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SymptomActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private TextView chooseAnswerText, chooseAnswerTexts, questionTextView;
    private CheckBox headCheckbox, bodyCheckbox, stomachCheckbox, chestCheckbox, lowerBodyCheckbox, backCheckbox;
    private MaterialButton submitButton, noButton, yesButton, tryButton, saveButton, discardButton;
    private String[] questions;
    private String[] answers = new String[7]; // Storing answers
    private int questionIndex = 0;
    private String painLocation = "";
    private LinearLayout interactionLayout;
    private LinearLayout questionLayout, saveOrDiscardLayout, tryLayout;
    private int countPain = 0;
    private ImageView medicinearrow;
    private FirebaseFirestore firestore;
    private String diagnosis = "";
    private String advice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        submitButton = findViewById(R.id.submitButton);
        chooseAnswerText = findViewById(R.id.chooseAnswerText);
        headCheckbox = findViewById(R.id.headCheckbox);
        bodyCheckbox = findViewById(R.id.bodyCheckbox);
        stomachCheckbox = findViewById(R.id.stomachCheckbox);
        chestCheckbox = findViewById(R.id.chestCheckbox);
        lowerBodyCheckbox = findViewById(R.id.lowerBodyCheckbox);
        backCheckbox = findViewById(R.id.backCheckbox);
        questionLayout = findViewById(R.id.questionLayout);
        saveOrDiscardLayout = findViewById(R.id.saveOrDiscardLayout);
        chooseAnswerTexts = findViewById(R.id.chooseAnswerTexts);
        tryLayout = findViewById(R.id.tryLayout);
        tryButton = findViewById(R.id.tryButton);
        medicinearrow = findViewById(R.id.medicinearrow);
        saveButton = findViewById(R.id.saveButton);
        discardButton = findViewById(R.id.discardButton);


        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Add initial messages
        chatMessages.add(new ChatMessage("Get Started!", true)); // User message
        chatMessages.add(new ChatMessage("Where do you feel pain?", false)); // System message
        chatAdapter.notifyDataSetChanged();

        Intent viewingIntent = getIntent();
        boolean isViewing = viewingIntent.getBooleanExtra("isViewing", false);
        String documentId = viewingIntent.getStringExtra("documentId");

        if(isViewing){
            saveOrDiscardLayout.setVisibility(View.GONE);
            questionLayout.setVisibility(View.GONE);
            tryLayout.setVisibility(View.GONE);
            headCheckbox.setVisibility(View.GONE);
            bodyCheckbox.setVisibility(View.GONE);
            stomachCheckbox.setVisibility(View.GONE);
            chestCheckbox.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            lowerBodyCheckbox.setVisibility(View.GONE);
            backCheckbox.setVisibility(View.GONE);
            chooseAnswerText.setVisibility(View.GONE);
            firestore.collection("SymptomAnalyzer")
                    .document(documentId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        List<String> questions = (List<String>) documentSnapshot.get("questions");
                        List<String> answers = (List<String>) documentSnapshot.get("answers");
                        String fetchedAdvice = documentSnapshot.getString("advice");
                        String fetchedDiagnosis = documentSnapshot.getString("diagnosis");
                        String fetchedPainLocation = documentSnapshot.getString("painLocation");

                        if (questions != null && answers != null) {
                            displayChatMessages(questions, answers, fetchedDiagnosis, fetchedAdvice, fetchedPainLocation);
                        } else {
                            Toast.makeText(this, "No questions or answers found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Document not found.", Toast.LENGTH_SHORT).show();
                    }
            })
                    .addOnFailureListener(e->{
                        Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

        // Set up submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedSymptoms = new ArrayList<>();

                // Check which checkboxes are selected
                if (headCheckbox.isChecked()) selectedSymptoms.add("Head");
                if (bodyCheckbox.isChecked()) selectedSymptoms.add("Body");
                if (stomachCheckbox.isChecked()) selectedSymptoms.add("Stomach");
                if (chestCheckbox.isChecked()) selectedSymptoms.add("Chest");
                if (lowerBodyCheckbox.isChecked()) selectedSymptoms.add("Lower Body");
                if (backCheckbox.isChecked()) selectedSymptoms.add("Back");
                if (selectedSymptoms.isEmpty()) {
                    Toast.makeText(SymptomActivity.this, "Please select at least one symptom", Toast.LENGTH_SHORT).show();
                    return;
                }
                String selectedText = TextUtils.join(", ", selectedSymptoms);

                chatMessages.add(new ChatMessage("I feel pain in " + selectedText, true));

                System.out.println("Selected Symptoms: " + selectedSymptoms);
                // Set the pain location for further questions
//                painLocation = selectedSymptoms.get(0);
                countPain = selectedSymptoms.size();

                painLocation = selectedText;
                // Show the first question based on the selected symptom
                showQuestion(selectedText);

                // Hide checkboxes and submit button
                headCheckbox.setVisibility(View.GONE);
                bodyCheckbox.setVisibility(View.GONE);
                stomachCheckbox.setVisibility(View.GONE);
                chestCheckbox.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                lowerBodyCheckbox.setVisibility(View.GONE);
                backCheckbox.setVisibility(View.GONE);
                chooseAnswerText.setVisibility(View.GONE);
                questionLayout.setVisibility(View.VISIBLE);
                // Notify adapter of data change
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to bottom
            }
        });
        medicinearrow.setOnClickListener(V->{
            if(isViewing){
                Intent intent = new Intent(SymptomActivity.this, Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "");
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SymptomActivity.this, Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "ANALYZER");
                startActivity(intent);
                finish();
            }

        });
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SymptomActivity.this, SymptomActivity.class);
                startActivity(intent);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmSaveDialog();
            }
        });
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDiscardDialog();
            }
        });
        // Set up Yes/No buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected("Yes");
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected("No");
            }
        });
    }
    private void showQuestion(String selectedSymptom) {
        System.out.println("Selected Symptom: " + selectedSymptom);

        // Logic for specific combinations of symptoms
        if (selectedSymptom.equals("Head, Chest") || selectedSymptom.equals("Chest, Head")) {
            questions = new String[]{
                    "Do you have a mild headache?",
                    "Do you feel lightheaded or dizzy?",
                    "Do you feel slight chest discomfort?",
                    "Are you feeling tired or fatigued?",
                    "Do you have a mild cough?",
                    "Does the discomfort improve with rest?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        else if(selectedSymptom.equals("Head, Stomach") || selectedSymptom.equals("Stomach, Head")){
            questions = new String[]{
                    "Do you have a mild headache?",
                    "Do you feel nauseous?",
                    "Do you feel bloated?",
                    "Have you experienced mild stomach pain?",
                    "Do you feel fatigued?",
                    "Does eating worsen your symptoms?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        else if(selectedSymptom.equals("Chest, Stomach") || selectedSymptom.equals("Stomach, Chest")){
            questions = new String[]{
                    "Do you feel slight chest discomfort?",
                    "Do you have mild stomach pain?",
                    "Do you feel bloated?",
                    "Do you have heartburn?",
                    "Do you feel fatigued?",
                    "Does rest improve your symptoms?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        else if(selectedSymptom.equals("Back, Lower Body") || selectedSymptom.equals("Lower Body, Back")){
            questions = new String[]{
                    "Do you have mild lower back pain?",
                    "Do you feel stiffness in your back?",
                    "Do you have mild leg pain?",
                    "Do you experience muscle cramps in your lower body?",
                    "Does rest improve your symptoms?",
                    "Does physical activity worsen the pain?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        else if(selectedSymptom.equals("Body, Stomach") || selectedSymptom.equals("Stomach, Body")){
            questions = new String[]{
                    "Do you feel fatigued?",
                    "Do you have mild muscle pain?",
                    "Have you experienced mild stomach pain?",
                    "Do you feel bloated?",
                    "Do you have mild chills?",
                    "Does rest improve your symptoms?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        else if(selectedSymptom.equals("Head, Back") || selectedSymptom.equals("Back, Head")){
            questions = new String[]{
                    "Do you have a mild headache?",
                    "Do you feel stiffness in your neck or back?",
                    "Are you feeling fatigued?",
                    "Do you feel lightheaded or dizzy?",
                    "Does rest improve your symptoms?",
                    "Does physical activity worsen the pain?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
        // Logic for individual symptoms
        else if (selectedSymptom.equals("Head")) {
            questions = new String[]{
                    "Do you have a headache?",
                    "Are you feeling dizzy?",
                    "Do you have sensitivity to light?",
                    "Do you feel nauseous?",
                    "Have you had any vision problems?",
                    "Have you recently experienced head trauma?", // Severe cases
                    "Is the headache worsening as time goes on?" // Severity
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if (selectedSymptom.equals("Body")) {
            questions = new String[]{
                    "Do you have muscle pain?",
                    "Are you feeling fatigued?",
                    "Do you have joint pain?",
                    "Do you feel weak?",
                    "Have you had chills?",
                    "Have you experienced any unexplained weight loss?", // Severe cases
                    "Do you have a fever?" // Infection
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if (selectedSymptom.equals("Stomach")) {
            questions = new String[]{
                    "Do you have stomach pain?",
                    "Do you feel bloated?",
                    "Have you experienced diarrhea?",
                    "Are you feeling nauseous?",
                    "Do you have heartburn?",
                    "Have you vomited recently?", // Severe stomach issues
                    "Do you feel pain when eating certain foods?" // Severity
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if (selectedSymptom.equals("Chest")) {
            questions = new String[]{
                    "Do you have chest pain?",
                    "Do you feel short of breath?",
                    "Do you have a cough?",
                    "Do you have a sore throat?",
                    "Have you had any palpitations?",
                    "Is the chest pain sharp and sudden?" // Severe chest pain
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if (selectedSymptom.equals("Lower Body")) {
            questions = new String[]{
                    "Do you have leg pain?",
                    "Do you have knee pain?",
                    "Is your foot swollen?",
                    "Do you experience pain in your ankles?",
                    "Do you have muscle cramps in your legs?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if (selectedSymptom.equals("Back")) {
            questions = new String[]{
                    "Do you have lower back pain?",
                    "Do you experience stiffness in your back?",
                    "Have you had sciatica or shooting pain in your legs?",
                    "Do you feel pain when bending over or lifting?",
                    "Have you had any recent trauma to your back?"
            };
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        } else if(countPain >= 3){
            tryLayout.setVisibility(View.VISIBLE);
            tryButton.setVisibility(View.VISIBLE);
            chooseAnswerTexts.setVisibility(View.GONE);
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            chatMessages.add(new ChatMessage("You are experiencing multiple pain-related symptoms. It is recommended to consult a doctor for proper diagnosis and treatment.", false));
            return;
        }else{
            tryLayout.setVisibility(View.VISIBLE);
            tryButton.setVisibility(View.VISIBLE);
            chooseAnswerTexts.setVisibility(View.GONE);
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            chatMessages.add(new ChatMessage("Unfortunately, there are no questions available for the selected combination of pain locations.", false));
            return;
        }
        chatMessages.add(new ChatMessage(questions[questionIndex], false)); // Add the first question to chat
        chatAdapter.notifyDataSetChanged();

    }


    // This method will be triggered when the user answers a question
    public void onAnswerSelected(String answer) {
        if (questionIndex < questions.length) {
            // Add the answer as a chat message
            chatMessages.add(new ChatMessage(answer, true)); // Add the "Yes" or "No" response as a message

            answers[questionIndex] = answer;  // Save the answer

            // Move to the next question
            questionIndex++;

            if (questionIndex < questions.length) {
                chatMessages.add(new ChatMessage(questions[questionIndex], false)); // Add the next question to chat
            } else {
                saveOrDiscardLayout.setVisibility(View.VISIBLE);
                questionLayout.setVisibility(View.GONE);
                // Once all questions are answered, analyze the symptoms and provide feedback
                String analysisResult = analyzeSymptoms();
                chatMessages.add(new ChatMessage(analysisResult, false)); // Add analysis to the chat
            }

            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to the latest message
        }
    }

    // Analyze the symptoms based on answers and return a diagnosis
    private String analyzeSymptoms() {


        // Head-related symptoms (Migraine, Tension Headache, etc.)
        if (painLocation.equals("Head")) {
            // Severe headache with trauma
            if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis = "You may have a severe migraine or a concussion.";
                advice = "Seek immediate medical attention. Avoid bright lights and lay down in a quiet room.";
            }
            // Worsening headache
            else if (answers[0].equals("Yes") && answers[6].equals("Yes")) {
                diagnosis = "Your headache may be worsening, which could be a sign of a serious condition.";
                advice = "Consult a doctor immediately for further evaluation.";
            }
            // Migraine-related
            else if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes")) {
                diagnosis = "You may have a migraine.";
                advice = "Please consult a doctor. Rest in a dark, quiet room and stay hydrated.";
            }
            // Tension headache
            else if (answers[0].equals("Yes") && answers[3].equals("Yes")) {
                diagnosis = "You may have a tension headache.";
                advice = "Rest, drink water, and consider using over-the-counter pain relievers.";
            }
            // Mild headache (Tension Headache)
            else if (answers[0].equals("Yes")) {
                diagnosis = "You may have a mild tension headache.";
                advice = "Relax and stay hydrated. Over-the-counter pain relief may help.";
            } else {
                diagnosis = "You do not seem to have any severe headache-related symptoms.";
                advice = "If the headache persists or worsens, seek medical attention.";
            }
        }

        // Body-related symptoms (Flu, Muscle Strain, etc.)
        else if (painLocation.equals("Body")) {
            // Severe flu-like symptoms (fatigue, muscle aches, chills, fever)
            if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[4].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis = "You may be experiencing severe flu-like symptoms.";
                advice = "Rest, hydrate, and monitor your symptoms. If fever or severe fatigue develops, consult a doctor.";
            }
            // Weight loss, muscle pain, and fatigue (possible systemic issue)
            else if (answers[5].equals("Yes") && answers[6].equals("Yes")) {
                diagnosis = "You may have a systemic condition, such as an infection or other underlying issue.";
                advice = "Consult a doctor for further evaluation as these symptoms can indicate a serious problem.";
            }
            // Muscle strain or mild body pain
            else if (answers[0].equals("Yes") && answers[1].equals("No") && answers[2].equals("Yes")) {
                diagnosis = "You may have mild muscle strain.";
                advice = "Gentle stretching, ice packs, and rest should help. Avoid overexertion.";
            }
            else {
                diagnosis = "You may have mild body pain or strain.";
                advice = "Rest and avoid physical strain for a few days. If pain persists, consider seeing a doctor.";
            }
        }

        // Stomach-related symptoms (Gastroenteritis, IBS, etc.)
        else if (painLocation.equals("Stomach")) {
            // Severe gastritis or food poisoning (pain, vomiting, diarrhea)
            if (answers[0].equals("Yes") && answers[2].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis = "You may have severe gastritis or food poisoning.";
                advice = "Stay hydrated and rest. If symptoms persist or worsen, seek medical attention.";
            }
            // Pain after eating (possible ulcer)
            else if (answers[6].equals("Yes")) {
                diagnosis = "You may have an ulcer or acid reflux.";
                advice = "Avoid spicy or acidic foods. Consult a doctor for an appropriate treatment plan.";
            }
            // Heartburn or acid reflux
            else if (answers[4].equals("Yes")) {
                diagnosis = "You might be experiencing heartburn or acid reflux.";
                advice = "Avoid heavy meals and spicy foods. Over-the-counter antacids may help.";
            }
            // Mild gastritis or stomach flu
            else if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes")) {
                diagnosis = "You may have mild gastritis or stomach flu.";
                advice = "Avoid spicy foods, take it easy on your stomach, and stay hydrated. Consider using antacids if necessary.";
            }
            else {
                diagnosis = "You may have mild indigestion.";
                advice = "Eat light meals and avoid alcohol or acidic foods. Consider an over-the-counter antacid.";
            }
        }

        // Chest-related symptoms (Heart Issues, Respiratory Problems, etc.)
        else if (painLocation.equals("Chest")) {
            // Severe chest pain (possible heart-related)
            if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis = "You may have a serious heart-related condition.";
                advice = "Seek immediate medical attention. This could be a sign of a heart attack or other severe issue.";
            }
            // Respiratory infection or flu-like symptoms (fever, cough, shortness of breath)
            else if (answers[0].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes")) {
                diagnosis = "You may have a respiratory infection, such as a cold or flu.";
                advice = "Rest, hydrate, and monitor your symptoms. Seek medical help if breathing difficulties or high fever occur.";
            }
            // Mild chest discomfort (Costochondritis)
            else if (answers[0].equals("Yes") && answers[4].equals("Yes")) {
                diagnosis = "You might have costochondritis, which is a mild inflammation of the chest cartilage.";
                advice = "It’s usually harmless, but apply heat or ice to the chest area and take over-the-counter pain relievers.";
            }
            // Mild chest discomfort
            else {
                diagnosis = "You may be experiencing mild chest discomfort.";
                advice = "Rest, stay calm, and avoid heavy physical activity. If symptoms persist or worsen, consult a doctor.";
            }
        }
        // Back-related symptoms (Muscle Strain, Sciatica, Herniated Disc, etc.)
        else if (painLocation.equals("Back")) {
            if (answers[0].equals("Yes") && answers[1].equals("Yes")) {
                diagnosis = "You might have a small muscle strain or bad posture.";
                advice = "Take it easy and try using heat or cold to relieve the pain. If the pain doesn't get better, see a doctor.";
            } else if (answers[2].equals("Yes")) {
                diagnosis = "You might be dealing with sciatica, which is pain caused by a pinched nerve in your back.";
                advice = "Try not to sit for long periods. Rest and apply heat or ice. If the pain continues, talk to a doctor.";
            } else if (answers[3].equals("Yes")) {
                diagnosis = "You might have a slipped disc or pulled muscle in your back.";
                advice = "Rest, apply ice to the painful area, and avoid heavy lifting or sudden movements. If the pain doesn't improve, see a doctor.";
            } else if (answers[4].equals("Yes")) {
                diagnosis = "You might have hurt your back recently.";
                advice = "Rest and apply ice to your back. If the pain is severe or if you feel numbness or weakness in your legs, get medical help right away.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }

        // Lower Body symptoms
        else if (painLocation.equals("Lower Body")) {
            if (answers[0].equals("Yes") && answers[1].equals("Yes")) {
                diagnosis = "You may have a pulled muscle or poor blood flow.";
                advice = "Rest and elevate your legs, and apply ice. If the pain doesn't get better, see a doctor.";
            } else if (answers[2].equals("Yes")) {
                diagnosis = "You might have deep vein thrombosis (DVT), which is a serious condition that needs immediate medical attention.";
                advice = "See a doctor right away. DVT can be dangerous and lead to serious complications like blood clots in the lungs.";
            } else if (answers[3].equals("Yes")) {
                diagnosis = "Your legs may be swollen due to fluid buildup or mild inflammation.";
                advice = "Try elevating your legs and using compression stockings. If the swelling doesn't improve, or gets worse, see a doctor.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }

        }

        else if(painLocation.equals("Head, Chest") || painLocation.equals("Chest, Head")){
            if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes")) {
                diagnosis = "You may be experiencing stress or mild anxiety.";
                advice = "Practice relaxation techniques, such as deep breathing or meditation. Ensure you are getting enough rest and avoiding overexertion. Seek help if the symptoms worsen or interfere with daily activities.";
            } else if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[3].equals("Yes")) {
                diagnosis = "You might be mildly dehydrated.";
                advice = "Drink plenty of water, avoid caffeine and alcohol, and rest. If symptoms persist, consult a healthcare provider.";
            } else if (answers[2].equals("Yes") && answers[3].equals("Yes") && answers[4].equals("Yes")) {
                diagnosis = "You could have a mild respiratory infection.";
                advice = "Rest, stay hydrated, and monitor symptoms. Use over-the-counter remedies if necessary. Seek medical help if symptoms worsen or breathing becomes difficult.";
            } else if(answers[0].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes")){
                diagnosis = "You might be experiencing mild Gastroesophageal Reflux Disease (GERD) or indigestion.";
                advice = "Avoid heavy meals, caffeine, and lying down immediately after eating. Over-the-counter antacids might help. If symptoms persist, consult a doctor.";
            }else {
                diagnosis = "Your symptoms suggest a non-specific mild condition.";
                advice = "Rest, stay hydrated, and monitor your symptoms. If they persist or worsen, seek medical advice.";
            }
        }

        else if(painLocation.equals("Head, Stomach") || painLocation.equals("Stomach, Head")){
            if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[3].equals("Yes")){
                diagnosis=" This could be a mild migraine or tension headache that also causes digestive upset.";
                advice= "Rest in a dark, quiet room. Drink water to stay hydrated and try to avoid strong smells. Over-the-counter pain relievers like ibuprofen may help.";
            } else  if(answers[1].equals("Yes") && answers[2].equals("Yes") && answers[4].equals("Yes")){
                diagnosis="Stress or anxiety might be triggering both a headache and digestive symptoms.";
                advice ="Practice relaxation techniques such as deep breathing, meditation, or yoga. Consider reducing caffeine intake, which can worsen both headaches and nausea.";
            } else if(answers[1].equals("Yes") && answers[3].equals("Yes") && answers[4].equals("Yes")){
                diagnosis="Mild food poisoning or a stomach bug could cause nausea, mild stomach pain, and fatigue.";
                advice="Stay hydrated with water or electrolyte drinks. Avoid solid food until nausea improves; try a bland diet when ready (like crackers, toast, or bananas).";
            } else if(answers[0].equals("Yes") && answers[2].equals("Yes") && answers[5].equals("Yes")){
                diagnosis="Mild acid reflux or GERD can cause both digestive discomfort and headaches.";
                advice="Avoid lying down after eating. Limit foods and drinks that may trigger reflux, like spicy foods, caffeine, and alcohol. Over-the-counter antacids may help relieve symptoms.";
            } else if(answers[0].equals("Yes") && answers[2].equals("Yes") && answers[4].equals("Yes")){
                diagnosis="Overexertion, lack of sleep, or poor sleep quality may result in both a mild headache and stomach discomfort.";
                advice="Prioritize rest and aim for 7-9 hours of quality sleep. Avoid excessive physical activity until symptoms improve, and hydrate properly.";
            } else if(answers[0].equals("Yes") && answers[3].equals("Yes") && answers[4].equals("Yes")){
                diagnosis="A mild viral infection, such as a cold or flu, could cause both a headache and stomach issues.";
                advice="Rest and stay hydrated. Use over-the-counter medications to relieve symptoms (e.g., acetaminophen for the headache, anti-nausea meds). Seek medical help if symptoms worsen or persist for several days.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }

        else if(painLocation.equals("Chest, Stomach") || painLocation.equals("Stomach, Chest")){
            if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[3].equals("Yes")){
                diagnosis = "Mild indigestion or acid reflux can cause chest discomfort and stomach issues.";
                advice="Avoid spicy, fatty, or acidic foods. Try an over-the-counter antacid for relief. Eat smaller, more frequent meals and avoid lying down immediately after eating.";
            }else if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes")){
                diagnosis = "Mild gastritis can lead to both chest discomfort and stomach bloating.";
                advice="Avoid alcohol, caffeine, and irritant foods. Consider using over-the-counter antacids or proton pump inhibitors (PPIs). Rest and hydrate.";
            }
            else if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[4].equals("Yes")){
                diagnosis ="Stress can cause both chest discomfort and mild stomach pain, sometimes leading to heartburn.";
                advice="Practice relaxation techniques like deep breathing or meditation. Ensure proper rest and try to reduce stressors. Avoid caffeine or alcohol if stress levels are high.";
            }
            else if(answers[0].equals("Yes") && answers[3].equals("Yes") && answers[5].equals("Yes")){
                diagnosis ="Mild gastric reflux could cause discomfort in the chest and mild stomach pain.";
                advice="Avoid trigger foods (spicy, acidic, fatty foods). Sleep with your head elevated to reduce reflux. Take over-the-counter antacids for symptom relief.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }

        else if(painLocation.equals("Back, Lower Body") || painLocation.equals("Lower Body, Back")){
            if(answers[0].equals("Yes") && answers[3].equals("Yes")){
                diagnosis ="Mild muscle strain can cause both lower back pain and cramps in the lower body.";
                advice="Rest, apply heat or ice, and gently stretch. Avoid heavy lifting or strenuous activity. Take over-the-counter pain relievers if necessary.";
            }else if(answers[1].equals("Yes") && answers[2].equals("Yes")){
                diagnosis ="Sciatica can cause pain in the lower back that radiates to the legs.";
                advice="Apply heat or ice to the affected area. Try gentle stretches and avoid sitting for long periods. If symptoms persist, consider seeing a healthcare provider for physical therapy.";
            }else if(answers[0].equals("Yes") && answers[5].equals("Yes")){
                diagnosis ="Poor posture during daily activities can lead to mild back pain and stiffness.";
                advice="Practice good posture and avoid slouching. Try ergonomic adjustments for your workstation. Take breaks to stretch and move regularly.";
            }else if(answers[1].equals("Yes") && answers[4].equals("Yes")){
                diagnosis ="A mild herniated disc in the lower back can cause stiffness and discomfort that improves with rest.";
                advice="Rest and avoid heavy lifting. Use heat or ice for pain relief.";
            } else if(answers[4].equals("Yes") && answers[5].equals("Yes")){
                diagnosis ="Fatigue or overexertion can lead to mild pain and stiffness in the back and lower body.";
                advice="Rest and allow your muscles to recover. Stay hydrated and avoid overexertion. Perform light stretching to relieve tension.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }
        else if(painLocation.equals("Body, Stomach") || painLocation.equals("Stomach, Body")){
            if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[5].equals("Yes")){
                diagnosis ="Mild muscle pain and fatigue may be caused by muscle strain, improving with rest.";
                advice="Take rest, apply heat or cold compress, and avoid strenuous activities.";
            }else if(answers[0].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes")){
                diagnosis ="Mild stomach pain and bloating could indicate indigestion or mild gastrointestinal issues.";
                advice="Avoid heavy meals, drink water, and consider over-the-counter antacids if necessary.";
            }else if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[4].equals("Yes")){
                diagnosis ="Mild chills, fatigue, and muscle pain may be signs of a mild viral infection like the flu.";
                advice="Rest, stay hydrated, and use fever reducers (like acetaminophen) if needed.";
            }else if(answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes")){
                diagnosis ="Stress can lead to fatigue, muscle pain, and mild stomach discomfort.";
                advice="Practice relaxation techniques, stay hydrated, and consider light physical activities.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }
        else if(painLocation.equals("Head, Back") || painLocation.equals("Back, Head")){
            if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes") && answers[4].equals("Yes")) {
                diagnosis ="Stiffness in the neck or back, along with mild headache and fatigue, may be due to muscle tension or strain, improving with rest.";
                advice="Take rest, apply heat or cold compress, and avoid strenuous activity.";
            }else if (answers[0].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis ="A mild headache with lightheadedness and fatigue, worsened by physical activity, may indicate a migraine or tension headache.";
                advice="Rest in a quiet, dark room, and consider over-the-counter pain relief.";
            }else if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[2].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis ="Stress may cause mild headaches, neck or back stiffness, fatigue, and pain worsened by physical activity.";
                advice="Practice relaxation techniques, stay hydrated, and take breaks to rest.";
            }else if (answers[0].equals("Yes") && answers[1].equals("Yes") && answers[5].equals("Yes")) {
                diagnosis ="Stiffness in the neck or back and mild headache, aggravated by physical activity, can result from poor posture or sitting habits.";
                advice="Improve posture, take breaks from sitting, and consider stretching exercises.";
            } else if (answers[0].equals("Yes") && answers[2].equals("Yes") && answers[3].equals("Yes") && answers[4].equals("Yes")) {
                diagnosis ="Mild headache, lightheadedness, and fatigue that improve with rest can be signs of dehydration.";
                advice="Drink more water and stay hydrated.";
            }else {
                diagnosis = "If none of the symptoms are present, it could be a temporary discomfort without a serious underlying issue.";
                advice ="Continue to monitor symptoms. Rest, stay active, and see a healthcare provider if symptoms persist or worsen.";
            }
        }
        // No serious symptoms
        if (diagnosis.equals("")) {
            diagnosis = "You do not appear to have serious symptoms at the moment.";
            advice = "It might just be a mild condition, but continue to monitor your health. If symptoms worsen, consult a doctor.";
        }

        // Check if symptoms are mild or severe and suggest consulting a doctor if needed
        if (diagnosis.contains("severe") || diagnosis.contains("consult a doctor")) {
            advice += "\nIt is recommended to consult a doctor for further evaluation.";
        }

        return "Diagnosis: " + diagnosis + "\n" + "Advice: " + advice;
    }
    private void showConfirmDiscardDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discarding");
        builder.setMessage("Are you sure you want to discard this record?");

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SymptomActivity.this, Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "ANALYZER");
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showConfirmSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Saving");
        builder.setMessage("Are you sure you want to save this record?");

        builder.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
            String savedEmail = sharedPreferences.getString("EMAIL", null);

            if (savedEmail != null) {
                String formattedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                List<String> answersList = Arrays.asList(answers);
                List<String> questionList = Arrays.asList(questions);
                if (painLocation == null || painLocation.isEmpty() || answersList.isEmpty()) {
                    Toast.makeText(SymptomActivity.this, "Please provide all required details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> symptomAnalyzer = new HashMap<>();
                symptomAnalyzer.put("email", savedEmail);
                symptomAnalyzer.put("painLocation", painLocation);
                symptomAnalyzer.put("answers", answersList);
                symptomAnalyzer.put("questions", questionList);
                symptomAnalyzer.put("dateAndTime", Timestamp.now());
                symptomAnalyzer.put("advice", advice);
                symptomAnalyzer.put("diagnosis", diagnosis);

                ProgressDialog progressDialog = new ProgressDialog(SymptomActivity.this);
                progressDialog.setMessage("Saving...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                firestore.collection("SymptomAnalyzer")
                        .add(symptomAnalyzer)
                        .addOnSuccessListener(documentReference -> {
                            progressDialog.dismiss();
                            Toast.makeText(SymptomActivity.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SymptomActivity.this, Menubar.class);
                            intent.putExtra("EXTRA_FRAGMENT", "ANALYZER");
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.e("FirestoreError", "Error saving data", e);
                            Toast.makeText(SymptomActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(SymptomActivity.this, "No email found in preferences", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void displayChatMessages(List<String> questions, List<String> answers, String fetchedDiagnosis, String fetchedAdvice, String fetchedPainLocation) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        int size = Math.min(questions.size(), answers.size()); // Ensure both arrays align
        chatMessages.add(new ChatMessage("Get Started!", true)); // User message
        chatMessages.add(new ChatMessage("Where do you feel pain?", false)); // System message
        chatMessages.add(new ChatMessage("I feel pain in "  + fetchedPainLocation, true));
        for (int i = 0; i < size; i++) {
            // Add question from system
            chatMessages.add(new ChatMessage(questions.get(i), false));
            // Add answer from user
            if (answers.get(i) != null) { // Ensure null values are handled
                chatMessages.add(new ChatMessage(answers.get(i), true));
            }
        }
        String diaAndAdv = "Diagnosis: " + fetchedDiagnosis + "\n Advice: " + fetchedAdvice;
        chatMessages.add(new ChatMessage(diaAndAdv, false));

        // Assuming you have a RecyclerView adapter
        chatAdapter.setChatMessages(chatMessages);
        chatAdapter.notifyDataSetChanged();
    }


}