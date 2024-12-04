package com.example.mediforecast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SymptomActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private TextView chooseAnswerText, systemMessageTextView, questionTextView;
    private CheckBox headCheckbox, bodyCheckbox, stomachCheckbox, chestCheckbox, lowerBodyCheckbox, backCheckbox;
    //   private Button yesButton, noButton;
//    private Button submitButton;
    private MaterialButton submitButton, noButton, yesButton;
    private String[] questions;
    private String[] answers = new String[7]; // Storing answers
    private int questionIndex = 0;
    private String painLocation = "";
    private LinearLayout interactionLayout;
    private LinearLayout questionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

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

        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Add initial messages
        chatMessages.add(new ChatMessage("Get Started!", true)); // User message
        chatMessages.add(new ChatMessage("Where do you feel pain?", false)); // System message
        chatAdapter.notifyDataSetChanged();

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
                // Add user message with selected symptoms
//                StringBuilder selectedText = new StringBuilder();
//                for (String symptom : selectedSymptoms) {
//                    selectedText.append(symptom).append(" ");
//                }
                String selectedText = TextUtils.join(", ", selectedSymptoms);

                chatMessages.add(new ChatMessage("I feel pain in " + selectedText, true));

                // Set the pain location for further questions
                painLocation = selectedSymptoms.get(0);

                // Show the first question based on the selected symptom
                showQuestion(painLocation);

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
        // Set the questions based on the selected symptom
        if (selectedSymptom.equals("Head")) {
            questions = new String[]{
                    "Do you have a headache?",
                    "Are you feeling dizzy?",
                    "Do you have sensitivity to light?",
                    "Do you feel nauseous?",
                    "Have you had any vision problems?",
                    "Have you recently experienced head trauma?", // Severe cases
                    "Is the headache worsening as time goes on?" // Severity
            };
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
        } else if (selectedSymptom.equals("Chest")) {
            questions = new String[]{
                    "Do you have chest pain?",
                    "Do you feel short of breath?",
                    "Do you have a cough?",
                    "Do you have a sore throat?",
                    "Have you had any palpitations?",
                    "Is the chest pain sharp and sudden?" // Severe chest pain
            };
        }else if (selectedSymptom.equals("Lower Body")) {
            questions = new String[]{
                    "Do you have leg pain?",
                    "Do you have knee pain?",
                    "Is your foot swollen?",
                    "Do you experience pain in your ankles?",
                    "Do you have muscle cramps in your legs?"
            };
        } else if (selectedSymptom.equals("Back")) {
            questions = new String[]{
                    "Do you have lower back pain?",
                    "Do you experience stiffness in your back?",
                    "Have you had sciatica or shooting pain in your legs?",
                    "Do you feel pain when bending over or lifting?",
                    "Have you had any recent trauma to your back?"
            };
        }

        // Display the first question
        chatMessages.add(new ChatMessage(questions[questionIndex], false)); // Add the first question to chat
        chatAdapter.notifyDataSetChanged();
        yesButton.setVisibility(View.VISIBLE);
        noButton.setVisibility(View.VISIBLE);
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
        String diagnosis = "";
        String advice = "";

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
            }
        }


//        // Dehydration-related symptoms
//        if (answers[0].equals("No") && answers[1].equals("No") && answers[2].equals("No")) {
//            diagnosis = "You might be dehydrated.";
//            advice = "Drink more water and hydrate frequently. Aim to drink at least 8 cups of water daily.";
//        }

        // No serious symptoms
        if (diagnosis.isEmpty()) {
            diagnosis = "You do not appear to have serious symptoms at the moment.";
            advice = "It might just be a mild condition, but continue to monitor your health. If symptoms worsen, consult a doctor.";
        }

        // Check if symptoms are mild or severe and suggest consulting a doctor if needed
        if (diagnosis.contains("severe") || diagnosis.contains("consult a doctor")) {
            advice += "\nIt is recommended to consult a doctor for further evaluation.";
        }

        return "Diagnosis: " + diagnosis + "\n" + "Advice: " + advice;
    }
}