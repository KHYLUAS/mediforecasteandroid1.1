package com.example.mediforecast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiagnosisMapping {
    // Define the symptoms-to-diagnosis mapping
    private static final Map<String, List<String>> diagnosisSymptomsMap = new HashMap<>();

    // Static block to initialize the symptoms-to-diagnosis mapping
    static {
        // Add diagnosis mappings based on common symptom combinations
        diagnosisSymptomsMap.put("Flu", Arrays.asList("Fever", "Cough", "Sore Throat", "Headache", "Fatigue", "Muscle Aches", "Runny Nose", "Chills"));
        diagnosisSymptomsMap.put("Cold", Arrays.asList("Cough", "Sore Throat", "Stuffy Nose", "Sneezing"));
        diagnosisSymptomsMap.put("Migraine", Arrays.asList("Headache", "Nausea", "Light Sensitivity"));
        diagnosisSymptomsMap.put("COVID-19", Arrays.asList("Fever", "Cough", "Fatigue", "Shortness of Breath"));
        diagnosisSymptomsMap.put("Allergy", Arrays.asList("Sneezing", "Itchy Eyes", "Runny Nose"));
        diagnosisSymptomsMap.put("Stomach Infection", Arrays.asList("Abdominal Pain", "Vomiting", "Diarrhea"));
        diagnosisSymptomsMap.put("Pneumonia", Arrays.asList("Cough", "Shortness of Breath", "Chest Pain"));
        diagnosisSymptomsMap.put("Asthma", Arrays.asList("Cough", "Wheezing", "Shortness of Breath"));
        diagnosisSymptomsMap.put("Sinusitis", Arrays.asList("Facial Pain", "Nasal Congestion", "Headache"));
        diagnosisSymptomsMap.put("Tonsillitis", Arrays.asList("Sore Throat", "Fever", "Swollen Tonsils"));
        diagnosisSymptomsMap.put("COVID-19 (Mild)", Arrays.asList("Cough", "Fever", "Fatigue", "Sore Throat"));
        diagnosisSymptomsMap.put("Tuberculosis", Arrays.asList("Cough", "Night Sweats", "Weight Loss", "Fatigue"));
        diagnosisSymptomsMap.put("Diabetes", Arrays.asList("Frequent Urination", "Extreme Thirst", "Fatigue", "Blurred Vision"));
        diagnosisSymptomsMap.put("Hypertension", Arrays.asList("Headache", "Shortness of Breath", "Dizziness", "Chest Pain"));
        diagnosisSymptomsMap.put("Anemia", Arrays.asList("Fatigue", "Paleness", "Shortness of Breath", "Dizziness"));
        diagnosisSymptomsMap.put("Heart Disease", Arrays.asList("Chest Pain", "Shortness of Breath", "Fatigue", "Swelling"));
        diagnosisSymptomsMap.put("Kidney Infection", Arrays.asList("Painful Urination", "Back Pain", "Fever", "Nausea"));
        diagnosisSymptomsMap.put("Gastritis", Arrays.asList("Abdominal Pain", "Nausea", "Indigestion", "Vomiting"));
        diagnosisSymptomsMap.put("Arthritis", Arrays.asList("Joint Pain", "Stiffness", "Swelling", "Redness"));
        diagnosisSymptomsMap.put("Chickenpox", Arrays.asList("Itchy Rash", "Fever", "Fatigue"));
        diagnosisSymptomsMap.put("Dengue", Arrays.asList("High Fever", "Severe Headache", "Pain Behind the Eyes", "Joint and Muscle Pain"));
        diagnosisSymptomsMap.put("Conjunctivitis", Arrays.asList("Red Eyes", "Itching", "Tears", "Swollen Eyelids"));
        diagnosisSymptomsMap.put("Hepatitis", Arrays.asList("Jaundice", "Fatigue", "Nausea", "Abdominal Pain"));
        diagnosisSymptomsMap.put("Celiac Disease", Arrays.asList("Bloating", "Diarrhea", "Fatigue", "Weight Loss"));
        diagnosisSymptomsMap.put("Stroke", Arrays.asList("Sudden Numbness", "Confusion", "Difficulty Speaking", "Blurred Vision"));
        diagnosisSymptomsMap.put("Sepsis", Arrays.asList("Fever", "Rapid Heart Rate", "Low Blood Pressure", "Shortness of Breath"));
        diagnosisSymptomsMap.put("Bronchitis", Arrays.asList("Cough", "Chest Tightness", "Wheezing", "Fatigue"));
        diagnosisSymptomsMap.put("Chronic Fatigue Syndrome", Arrays.asList("Extreme Fatigue", "Muscle Pain", "Difficulty Sleeping", "Headaches"));
        diagnosisSymptomsMap.put("Acid Reflux", Arrays.asList("Heartburn", "Regurgitation", "Chest Pain", "Difficulty Swallowing"));
        diagnosisSymptomsMap.put("Lung Cancer", Arrays.asList("Cough", "Shortness of Breath", "Chest Pain", "Fatigue"));
        diagnosisSymptomsMap.put("Gallstones", Arrays.asList("Abdominal Pain", "Nausea", "Indigestion", "Vomiting"));
        diagnosisSymptomsMap.put("Urinary Tract Infection (UTI)", Arrays.asList("Painful Urination", "Frequent Urination", "Pelvic Pain", "Cloudy Urine"));
        diagnosisSymptomsMap.put("Epilepsy", Arrays.asList("Seizures", "Unusual Sensations", "Loss of Consciousness", "Confusion"));
        diagnosisSymptomsMap.put("Osteoarthritis", Arrays.asList("Joint Pain", "Stiffness", "Reduced Range of Motion", "Swelling"));
        diagnosisSymptomsMap.put("Panic Attack", Arrays.asList("Rapid Heartbeat", "Sweating", "Shortness of Breath", "Dizziness"));
        diagnosisSymptomsMap.put("Shingles", Arrays.asList("Painful Rash", "Itching", "Burning Sensation", "Red Blisters"));
        diagnosisSymptomsMap.put("Hernia", Arrays.asList("Bulge or Lump", "Pain at the Site", "Difficulty Lifting", "Abdominal Discomfort"));
        diagnosisSymptomsMap.put("Thyroid Disorder", Arrays.asList("Fatigue", "Weight Gain/Loss", "Cold Sensitivity", "Dry Skin"));
        diagnosisSymptomsMap.put("PMS (Premenstrual Syndrome)", Arrays.asList("Mood Swings", "Abdominal Cramps", "Fatigue", "Irritability"));
        diagnosisSymptomsMap.put("Menopause", Arrays.asList("Hot Flashes", "Night Sweats", "Mood Swings", "Irregular Periods"));
        diagnosisSymptomsMap.put("Chronic Sinusitis", Arrays.asList("Facial Pain", "Nasal Congestion", "Headache", "Post-nasal Drip"));
        diagnosisSymptomsMap.put("Tonsillitis", Arrays.asList("Sore Throat", "Difficulty Swallowing", "Fever", "Swollen Tonsils"));
        diagnosisSymptomsMap.put("Liver Disease", Arrays.asList("Fatigue", "Jaundice", "Abdominal Pain", "Swelling in the Abdomen"));
        diagnosisSymptomsMap.put("Anxiety Disorder", Arrays.asList("Constant Worry", "Fatigue", "Restlessness", "Difficulty Concentrating"));
        diagnosisSymptomsMap.put("Gout", Arrays.asList("Severe Pain in Joints", "Swelling", "Redness", "Warmth in Affected Area"));
        diagnosisSymptomsMap.put("Food Poisoning", Arrays.asList("Nausea", "Vomiting", "Diarrhea", "Abdominal Pain"));
        diagnosisSymptomsMap.put("Sciatica", Arrays.asList("Lower Back Pain", "Leg Pain", "Numbness", "Tingling Sensation"));
        diagnosisSymptomsMap.put("Hives", Arrays.asList("Itchy Skin", "Red Welts", "Swelling", "Burning Sensation"));
        diagnosisSymptomsMap.put("Malaria", Arrays.asList("Fever", "Chills", "Sweating", "Headache"));
        diagnosisSymptomsMap.put("Cystitis", Arrays.asList("Painful Urination", "Frequent Urination", "Blood in Urine", "Pelvic Pain"));
        diagnosisSymptomsMap.put("Mumps", Arrays.asList("Swollen Salivary Glands", "Fever", "Headache", "Muscle Aches"));
        diagnosisSymptomsMap.put("Lupus", Arrays.asList("Fatigue", "Skin Rash", "Joint Pain", "Shortness of Breath"));
        diagnosisSymptomsMap.put("Tinea (Ringworm)", Arrays.asList("Itchy Rash", "Red, Ring-Shaped Skin", "Scaling", "Crusting"));
        diagnosisSymptomsMap.put("Eczema", Arrays.asList("Itchy Skin", "Redness", "Inflammation", "Dry Skin"));
        diagnosisSymptomsMap.put("Chronic Bronchitis", Arrays.asList("Persistent Cough", "Mucus Production", "Shortness of Breath", "Wheezing"));
        diagnosisSymptomsMap.put("Cervical Cancer", Arrays.asList("Abnormal Bleeding", "Pelvic Pain", "Pain During Intercourse", "Abnormal Discharge"));
        diagnosisSymptomsMap.put("Esophageal Cancer", Arrays.asList("Difficulty Swallowing", "Weight Loss", "Chest Pain", "Regurgitation"));
        diagnosisSymptomsMap.put("Rheumatoid Arthritis", Arrays.asList("Joint Pain", "Swelling", "Fatigue", "Morning Stiffness"));
        diagnosisSymptomsMap.put("Psoriasis", Arrays.asList("Red, Scaly Patches", "Itching", "Dry Skin", "Cracked Skin"));

        // Add more mappings as needed
    }

    // Get possible diagnoses based on selected symptoms
    public static Map<String, Integer> getDiagnosesWithAccuracy(Set<String> selectedSymptoms) {
        Map<String, Integer> diagnosesWithAccuracy = new HashMap<>();

        // Check each diagnosis in the map
        for (Map.Entry<String, List<String>> entry : diagnosisSymptomsMap.entrySet()) {
            Set<String> diagnosisSymptoms = new HashSet<>(entry.getValue());

            // Count the number of matching symptoms
            int matchCount = 0;
            for (String symptom : selectedSymptoms) {
                if (diagnosisSymptoms.contains(symptom)) {
                    matchCount++;
                }
            }

            // If there are matching symptoms, calculate accuracy
            if (matchCount > 0) {
                int accuracy = (int) ((matchCount / (double) diagnosisSymptoms.size()) * 100);
                diagnosesWithAccuracy.put(entry.getKey(), accuracy);
            }
        }

        return diagnosesWithAccuracy;
    }

    // Get only possible diagnoses
    public static List<String> getDiagnoses(Set<String> selectedSymptoms) {
        return new ArrayList<>(getDiagnosesWithAccuracy(selectedSymptoms).keySet());
    }

    // Return the symptom-to-diagnosis map (if needed elsewhere)
    public static Map<String, List<String>> getDiagnosisSymptomsMap() {
        return diagnosisSymptomsMap;
    }
}
