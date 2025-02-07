package com.example.mediforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class ViewDetails extends AppCompatActivity {
    String url;
    TextView symptomNameTV, description;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_details);
        symptomNameTV = findViewById(R.id.symptomName);
        back = findViewById(R.id.back);
        description = findViewById(R.id.descriptionText);
        ImageSlider imageSlider = findViewById(R.id.imageSlider);

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        Intent intent = getIntent();
        int symptomId = intent.getIntExtra("symptomId", -1);
        String symptomName = intent.getStringExtra("symptomName");
        String descriptionName = intent.getStringExtra("description");

        if(symptomName.equals("Fever")){
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/10880-fever";
        } else if (symptomName.equals("Cough")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough2.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17755-cough";
        } else if (symptomName.equals("Headache")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache1.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/9639-headaches";
        }else if (symptomName.equals("Sore Throat")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sorethroat1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sorethroat2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sorethroat3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/8274-sore-throat-pharyngitis";
        }else if (symptomName.equals("Runny Nose")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/runnynose1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/runnynose2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/runnynose3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17660-runny-nose";
        }else if (symptomName.equals("Body Aches")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/bodyaches.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/bodyaches2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/bodyaches3.png", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/body-aches#stress";
        }else if (symptomName.equals("Nausea")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nausea1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nausea2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nausea3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/8106-nausea--vomiting";
        }else if (symptomName.equals("Diarrhea")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diarrhea1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diarrhea2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diarrhea3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/4108-diarrhea";
        }else if (symptomName.equals("Fatigue")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21206-fatigue";
        }else if (symptomName.equals("Stomach Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain2.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
        }else if (symptomName.equals("Sneezing")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
        }else if (symptomName.equals("Light Sensitivity")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/light1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/light2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/light3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/photophobia";
        }else if (symptomName.equals("Shortness of Breath")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/shortbreath1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/shortbreath2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/shortbreath3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
        }else if (symptomName.equals("Itchy Eyes")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/itchyeyes1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/itchyeyes2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/itchyeyes3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/316536#Causes";
        }else if (symptomName.equals("Weight Gain/Loss")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightgainorloss1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightgainorloss2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightgainorloss3.png", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/316536#Causes";
        }else if (symptomName.equals("Abdominal Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/stomachpain2.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
        }else if (symptomName.equals("Vomiting")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/vomiting1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/vomiting2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/vomiting3.jpg", ScaleTypes.FIT));
            url = "hhttps://www.healthdirect.gov.au/vomiting";
        }else if (symptomName.equals("Chest Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/chestpain1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/chestpain2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/chestpain3.jpeg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21209-chest-pain";
        }else if (symptomName.equals("Wheezing")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/wheeze1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/wheezing2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/wheezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15203-wheezing";
        }else if (symptomName.equals("Nasal Congestion")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nasalcongestion1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nasalcongestion2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nasalcongestion3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17980-nasal-congestion";
        }else if (symptomName.equals("Swollen Tonsils")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swollentonsil1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swollentonsil2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swollentonsil3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/21146-tonsillitis";
        }else if (symptomName.equals("Night Sweats")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nightsweat1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nightsweat2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/nightsweat3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/16562-night-sweats";
        }else if (symptomName.equals("Weight Loss")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightloss.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightloss2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/weightgainorloss3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/unexplained-weight-loss";
        }else if (symptomName.equals("Frequent Urination")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/frequenturination1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/frequenturination2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/frequenturination3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15533-frequent-urination";
        }else if (symptomName.equals("Extreme Thirst")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/extremethirst1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/extremethirst2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/extremethirst3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/24050-polydipsia";
        }else if (symptomName.equals("Blurred Vision")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/blurred1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/blurred2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/blurred3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/24262-blurred-vision";
        }else if (symptomName.equals("Dizziness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diziness1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diziness2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/diziness3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/6422-dizziness";
        }else if (symptomName.equals("Swelling")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swelling1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swelling2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/swelling3.png", ScaleTypes.FIT));
            url = "https://medlineplus.gov/ency/article/003103.htm#:~:text=Swelling%20is%20the%20enlargement%20of,of%20the%20body%20(localized).";
        }else if (symptomName.equals("Painful Urination")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine2.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15176-dysuria-painful-urination";
        }else if (symptomName.equals("Back Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/backpain1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/backpain2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/backpain3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/back-pain";
        }else if (symptomName.equals("Indigestion")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/indigestion1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/indigestion2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/indigestion3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/7316-indigestion-dyspepsia";
        }
        //start here image
        else if (symptomName.equals("Joint Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17752-joint-pain";
        }else if (symptomName.equals("Stiffness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/25147-muscle-stiffness";
        }else if (symptomName.equals("Redness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/323521";
        }else if (symptomName.equals("Itchy Rash")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if(symptomName.equals("High Fever")){
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/10880-fever";
        }else if (symptomName.equals("Severe Headache")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
        }else if (symptomName.equals("Severe Headache")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache1.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/9639-headaches";
        }else if (symptomName.equals("Pain Behind the Eyes")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/pain-behind-eye#:~:text=Pain%20behind%20the%20eye%20can,with%20many%20different%20health%20conditions.";
        }else if (symptomName.equals("Joint and Muscle Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/muscle-and-joint-pain";
        }else if (symptomName.equals("Red Eyes")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17690-red-eye";
        }else if (symptomName.equals("Itching")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/11879-pruritus";
        }else if (symptomName.equals("Tears")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://medlineplus.gov/ency/article/003036.htm";
        }else if (symptomName.equals("Swollen Eyelids")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/10032-blepharitis";
        }else if (symptomName.equals("Jaundice")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15367-adult-jaundice";
        }else if (symptomName.equals("Bloating")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21740-bloated-stomach";
        }else if (symptomName.equals("Sudden Numbness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21015-numbness";
        }else if (symptomName.equals("Confusion")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/25206-confusion";
        }else if (symptomName.equals("Difficulty Speaking")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17653-dysarthria";
        }else if (symptomName.equals("Rapid Heart Rate")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/22108-tachycardia";
        }else if (symptomName.equals("Low Blood Pressure")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/21156-low-blood-pressure-hypotension";
        }else if (symptomName.equals("Chest Tightness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
        }else if (symptomName.equals("Extreme Fatigue")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21206-fatigue";
        }else if (symptomName.equals("Difficulty Swallowing")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21195-dysphagia-difficulty-swallowing";
        }else if (symptomName.equals("Abdominal Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
        }else if (symptomName.equals("Pelvic Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/12106-pelvic-pain";
        }else if (symptomName.equals("Cloudy Urine")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21894-cloudy-urine";
        }else if (symptomName.equals("Seizure")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/22789-seizure";
        }else if (symptomName.equals("Unusual Sensations")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.physio.co.uk/what-we-treat/neurological/symptoms/sensory-problems/abnormal-sensation.php#:~:text=Abnormal%20sensation%20is%20when%20a,resulting%20from%20a%20neurological%20problem.";
        }else if (symptomName.equals("Loss of Consciousness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.clinicbarcelona.org/en/assistance/be-healthy/loss-of-consciousness";
        }else if (symptomName.equals("Reduced Range of Motion")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/limited-range-of-motion#:~:text=When%20a%20joint%20does%20not,the%20muscles%2C%20pain%20or%20disease.";
        }else if (symptomName.equals("Sweating")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17113-hyperhidrosis";
        }else if (symptomName.equals("Painful Rash")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Burning Sensation")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/burning-sensation";
        }else if (symptomName.equals("Red Blisters")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/16787-blisters";
        }else if (symptomName.equals("Bulge or Lump")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/lump-in-the-abdomen";
        }else if (symptomName.equals("Difficulty Lifting")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.mayoclinic.org/diseases-conditions/muscular-dystrophy/symptoms-causes/syc-20375388";
        }else if (symptomName.equals("Abdominal Discomfort")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
        }else if (symptomName.equals("Cold Sensitivity")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/cold-intolerance";
        }else if (symptomName.equals("Dry Skin")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/16940-dry-skin";
        }else if (symptomName.equals("Mood Swings")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/mood-swings";
        }else if (symptomName.equals("Abdominal Cramps")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.webmd.com/digestive-disorders/stomach-cramps";
        }else if (symptomName.equals("Irritability")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/irritability";
        }else if (symptomName.equals("Hot Flashes")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15223-hot-flashes";
        }else if (symptomName.equals("Irregular Periods")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/14633-abnormal-menstruation-periods";
        }else if (symptomName.equals("Facial Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/face-pain";
        }else if (symptomName.equals("Post-nasal Drip")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/23082-postnasal-drip";
        }else if (symptomName.equals("Swelling in the Abdomen")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21819-abdominal-distension-distended-abdomen";
        }else if (symptomName.equals("Constant Worry")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/23940-generalized-anxiety-disorder-gad";
        }else if (symptomName.equals("Restlessness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.verywellhealth.com/restlessness-5223436";
        }else if (symptomName.equals("Difficulty Concentration")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/unable-to-concentrate";
        }else if (symptomName.equals("Severe Pain in Joints")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17752-joint-pain";
        }else if (symptomName.equals("Warmth in Affected Area")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/22111-hyperthermia";
        }else if (symptomName.equals("Lower Back Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/7936-lower-back-pain";
        }else if (symptomName.equals("Leg Pain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/241968";
        }else if (symptomName.equals("Numbness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.mayoclinic.org/symptoms/numbness/basics/definition/sym-20050938#:~:text=Numbness%20describes%20a%20loss%20of,both%20sides%20of%20the%20body.";
        }else if (symptomName.equals("Tingling Sensation")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/24932-paresthesia";
        }else if (symptomName.equals("Red Welts")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/8630-hives";
        }else if (symptomName.equals("Chills")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21476-chills";
        }else if (symptomName.equals("Blood in Urine")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/15234-hematuria";
        }else if (symptomName.equals("Swollen Salivary Glands")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/15749-sialadenitis-swollen-salivary-gland";
        }else if (symptomName.equals("Muscle Aches")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/myalgia-muscle-pain";
        }else if (symptomName.equals("Skin Rash")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Red, Ring-Shaped Skin")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/4560-ringworm";
        }else if (symptomName.equals("Scaling")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/scaling-skin";
        }else if (symptomName.equals("Crusting")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/15134-impetigo";
        }else if (symptomName.equals("Inflammation")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21660-inflammation";
        }else if (symptomName.equals("Persistent Cough")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://info.health.nz/conditions-treatments/lungs/persistent-coughs";
        }else if (symptomName.equals("Mucus Production")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/body/mucus";
        }else if (symptomName.equals("Abnormal Bleeding")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/menometrorrhagia-abnormal-uterine-bleeding";
        }else if (symptomName.equals("Pain During Intercourse")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/12325-dyspareunia-painful-intercourse";
        }else if (symptomName.equals("Abnormal Discharge")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4719-vaginal-discharge";
        }else if (symptomName.equals("Regurgitation")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/gerd/regurgitation";
        }else if (symptomName.equals("Morning Stiffness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/psoriatic-arthritis-stiffness-in-morning";
        }else if (symptomName.equals("Red, Scaly Patches")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/14403-seborrheic-dermatitis";
        }else if (symptomName.equals("Cracked Skin")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/cracked-skin";
        }else if (symptomName.equals("Stuffy/Runny Nose")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/stuffy-or-runny-nose-adult";
        }else if (symptomName.equals("Weight Gain")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/weight-gain-unintentional#:~:text=Bloating%2C%20or%20swelling%20due%20to,smoking%2C%20you%20might%20gain%20weight.";
        }
        description.setText(descriptionName);
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        symptomNameTV.setText(symptomName);
        symptomNameTV.setOnClickListener(v->{
            Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intents);
        });
        back.setOnClickListener(v->{
            Intent intents = new Intent(ViewDetails.this, SymptomAnalyzer.class);
            startActivity(intents);
            finish();
        });
    }
}