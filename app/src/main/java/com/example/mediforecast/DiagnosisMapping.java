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
        diagnosisSymptomsMap.put("Cold", Arrays.asList("Cough", "Sore Throat", "Stuffy Nose", "Sneezing", "Runny Nose", "Headache"));
        diagnosisSymptomsMap.put("Migraine", Arrays.asList("Headache", "Nausea", "Light Sensitivity", "Fatigue", "Vomiting", "Confusion", "Blurred Vision"));
        diagnosisSymptomsMap.put("COVID-19", Arrays.asList("Fever", "Cough", "Fatigue", "Shortness of Breath", "Sore Throat", "Muscle Aches", "Headache", "Vomiting", "Nasal Congestion", "Diarrhea", "Loss of Taste or Smell"));
        diagnosisSymptomsMap.put("Allergy", Arrays.asList("Sneezing", "Itchy Eyes", "Runny Nose", "Wheezing", "Skin Rash", "Swelling"));
        diagnosisSymptomsMap.put("Stomach Infection", Arrays.asList("Abdominal Pain", "Vomiting", "Diarrhea", "Fever", "Nausea"));
        diagnosisSymptomsMap.put("Pneumonia", Arrays.asList("Cough", "Shortness of Breath", "Chest Pain", "Fever", "Sweating", "Chills"));
        diagnosisSymptomsMap.put("Asthma", Arrays.asList("Cough", "Wheezing", "Shortness of Breath", "Chest Tightness"));
        diagnosisSymptomsMap.put("Sinusitis", Arrays.asList("Facial Pain", "Nasal Congestion", "Headache", "Post-nasal Drip", "Fever"));
        diagnosisSymptomsMap.put("Tuberculosis", Arrays.asList("Cough", "Night Sweats", "Weight Loss", "Fatigue", "Chest Pain", "Fever"));
        diagnosisSymptomsMap.put("Diabetes", Arrays.asList("Frequent Urination", "Extreme Thirst", "Fatigue", "Blurred Vision", "Weight Loss", "Slow Healing Wounds"));
        diagnosisSymptomsMap.put("Hypertension", Arrays.asList("Headache", "Shortness of Breath", "Dizziness", "Chest Pain", "Nosebleeds", "Blurred Vision"));
        diagnosisSymptomsMap.put("Anemia", Arrays.asList("Fatigue", "Paleness", "Shortness of Breath", "Dizziness", "Cold Hands and Feet", "Headache"));
        diagnosisSymptomsMap.put("Gastritis", Arrays.asList("Abdominal Pain", "Nausea", "Indigestion", "Vomiting", "Bloating", "Loss of Appetite"));
        diagnosisSymptomsMap.put("Arthritis", Arrays.asList("Joint Pain", "Stiffness", "Swelling", "Redness", "Reduced Range of Motion", "Fatigue"));
        diagnosisSymptomsMap.put("Chickenpox", Arrays.asList("Itchy Rash", "Fever", "Fatigue", "Loss of Appetite", "Headache"));
        diagnosisSymptomsMap.put("Dengue", Arrays.asList("High Fever", "Severe Headache", "Pain Behind the Eyes", "Joint and Muscle Pain", "Skin Rash", "Bleeding"));
        diagnosisSymptomsMap.put("Conjunctivitis", Arrays.asList("Red Eyes", "Itching", "Tears", "Swollen Eyelids", "Discharge", "Blurred Vision"));
        diagnosisSymptomsMap.put("Hepatitis", Arrays.asList("Jaundice", "Fatigue", "Nausea", "Abdominal Pain", "Dark Urine", "Loss of Appetite"));
        diagnosisSymptomsMap.put("Bronchitis", Arrays.asList("Cough", "Chest Tightness", "Wheezing", "Fatigue", "Mucus Production", "Sore Throat"));
        diagnosisSymptomsMap.put("Chronic Fatigue Syndrome", Arrays.asList("Extreme Fatigue", "Muscle Pain", "Difficulty Sleeping", "Headaches", "Memory Problems", "Joint Pain"));
        diagnosisSymptomsMap.put("Acid Reflux", Arrays.asList("Heartburn", "Regurgitation", "Chest Pain", "Difficulty Swallowing", "Chronic Cough", "Sore Throat"));
        diagnosisSymptomsMap.put("Gallstones", Arrays.asList("Abdominal Pain", "Nausea", "Indigestion", "Vomiting", "Bloating", "Fever"));
        diagnosisSymptomsMap.put("Urinary Tract Infection (UTI)", Arrays.asList("Painful Urination", "Frequent Urination", "Pelvic Pain", "Cloudy Urine", "Strong Urine Odor", "Fever"));
        diagnosisSymptomsMap.put("Epilepsy", Arrays.asList("Seizures", "Unusual Sensations", "Loss of Consciousness", "Confusion", "Jerking Movements", "Staring Spells"));
        diagnosisSymptomsMap.put("Osteoarthritis", Arrays.asList("Joint Pain", "Stiffness", "Reduced Range of Motion", "Swelling", "Cracking Sounds", "Muscle Weakness"));
        diagnosisSymptomsMap.put("Panic Attack", Arrays.asList("Rapid Heartbeat", "Sweating", "Shortness of Breath", "Dizziness", "Trembling", "Chest Pain"));
        diagnosisSymptomsMap.put("Shingles", Arrays.asList("Painful Rash", "Itching", "Burning Sensation", "Red Blisters", "Fever", "Sensitivity to Touch"));
        diagnosisSymptomsMap.put("Hernia", Arrays.asList("Bulge or Lump", "Pain at the Site", "Difficulty Lifting", "Abdominal Discomfort", "Burning Sensation", "Swelling"));
        diagnosisSymptomsMap.put("Thyroid Disorder", Arrays.asList("Fatigue", "Weight Gain/Loss", "Cold Sensitivity", "Dry Skin", "Hair Thinning", "Depression"));
        diagnosisSymptomsMap.put("PMS (Premenstrual Syndrome)", Arrays.asList("Mood Swings", "Abdominal Cramps", "Fatigue", "Irritability"));
        diagnosisSymptomsMap.put("Menopause", Arrays.asList("Hot Flashes", "Night Sweats", "Mood Swings", "Irregular Periods", "Insomnia", "Thinning Hair"));
        diagnosisSymptomsMap.put("Chronic Sinusitis", Arrays.asList("Facial Pain", "Nasal Congestion", "Headache", "Post-nasal Drip", "Cough", "Bad Breath"));
        diagnosisSymptomsMap.put("Tonsillitis", Arrays.asList("Sore Throat", "Difficulty Swallowing", "Fever", "Swollen Tonsils", "Ear Pain", "Headache"));
        diagnosisSymptomsMap.put("Anxiety Disorder", Arrays.asList("Constant Worry", "Fatigue", "Restlessness", "Difficulty Concentrating", "Irritability", "Sleep Disturbances"));
        diagnosisSymptomsMap.put("Gout", Arrays.asList("Severe Pain in Joints", "Swelling", "Redness", "Warmth in Affected Area", "Limited Range of Motion", "Tenderness"));
        diagnosisSymptomsMap.put("Food Poisoning", Arrays.asList("Nausea", "Vomiting", "Diarrhea", "Abdominal Pain", "Fever", "Weakness", "Headache"));
        diagnosisSymptomsMap.put("Sciatica", Arrays.asList("Lower Back Pain", "Leg Pain", "Numbness", "Numbness", "Muscle Weakness", "Burning Sensation"));
        diagnosisSymptomsMap.put("Hives", Arrays.asList("Itchy Skin", "Red Welts", "Swelling", "Burning Sensation", "Raised Bumps", "Skin Discoloration"));
        diagnosisSymptomsMap.put("Malaria", Arrays.asList("Fever", "Chills", "Sweating", "Headache", "Nausea", "Vomiting", "Muscle Pain", "Cough", "Fatigue", "Abdominal Pain", "Diarrhea", "Joint Pain"));
        diagnosisSymptomsMap.put("Mumps", Arrays.asList("Swollen Salivary Glands", "Fever", "Headache", "Muscle Aches", "Loss of Appetite", "Pain While Chewing", "Fatigue"));
        diagnosisSymptomsMap.put("Lupus", Arrays.asList("Fatigue", "Skin Rash", "Joint Pain", "Shortness of Breath", "Fever", "Mouth Sores", "Confusion"));
        diagnosisSymptomsMap.put("Tinea (Ringworm)", Arrays.asList("Itchy Rash", "Red, Ring-Shaped Skin", "Scaling", "Crusting", "Hair Loss", "Inflamed Skin"));
        diagnosisSymptomsMap.put("Eczema", Arrays.asList("Itchy Skin", "Redness", "Inflammation", "Dry Skin", "Cracking", "Blisters"));
        diagnosisSymptomsMap.put("Chronic Bronchitis", Arrays.asList("Persistent Cough", "Mucus Production", "Shortness of Breath", "Wheezing", "Chest Discomfort", "Fatigue"));
        diagnosisSymptomsMap.put("Rheumatoid Arthritis", Arrays.asList("Joint Pain", "Swelling", "Fatigue", "Morning Stiffness", "Loss of Joint Function", "Fever"));
        diagnosisSymptomsMap.put("Psoriasis", Arrays.asList("Red, Scaly Patches", "Itching", "Dry Skin", "Cracked Skin", "Burning Sensation", "Thickened Nails"));
        //new
        diagnosisSymptomsMap.put("Heat Exhaustion", Arrays.asList("Sweating", "Dizziness", "Nausea", "Weakness", "Headache", "Rapid Heartbeat"));
        diagnosisSymptomsMap.put("Prickly Heat (Bungang Araw)", Arrays.asList("Itchy Rash", "Red Bumps", "Burning Sensation", "Skin Irritation"));
        diagnosisSymptomsMap.put("Dyspepsia (Indigestion)", Arrays.asList("Bloating", "Abdominal Discomfort", "Nausea", "Belching", "Heartburn"));
        diagnosisSymptomsMap.put("Otitis Media (Ear Infection)", Arrays.asList("Ear Pain", "Hearing Loss", "Fever", "Ear Drainage", "Irritability"));
        diagnosisSymptomsMap.put("Athlete’s Foot (Alipunga)", Arrays.asList("Itchy Feet", "Cracked Skin", "Redness", "Burning Sensation", "Foul Odor"));
        diagnosisSymptomsMap.put("Fungal Skin Infection (Hadhad)", Arrays.asList("Itchy Rash", "Redness", "Scaling", "Skin Peeling"));
        diagnosisSymptomsMap.put("Boils (Pigsa)", Arrays.asList("Painful Lump", "Swelling", "Pus Formation", "Redness", "Fever"));
        diagnosisSymptomsMap.put("Constipation", Arrays.asList("Hard Stools", "Straining During Bowel Movements", "Bloating", "Abdominal Discomfort"));
        diagnosisSymptomsMap.put("Diaper Rash", Arrays.asList("Redness", "Itchiness", "Discomfort", "Skin Peeling"));
        diagnosisSymptomsMap.put("Motion Sickness", Arrays.asList("Dizziness", "Nausea", "Vomiting", "Sweating", "Headache"));
        diagnosisSymptomsMap.put("Tension Headache", Arrays.asList("Headache", "Pressure Around Forehead", "Neck Pain", "Light Sensitivity"));
        diagnosisSymptomsMap.put("Hand, Foot, and Mouth Disease (HFMD)", Arrays.asList("Fever", "Sore Throat", "Skin Rash", "Mouth Sores", "Fatigue"));
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
