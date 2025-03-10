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
    TextView symptomNameTV, description, remedyText;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_details);
        symptomNameTV = findViewById(R.id.symptomName);
        back = findViewById(R.id.back);
        description = findViewById(R.id.descriptionText);
        remedyText = findViewById(R.id.remedyText);
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        Intent intent = getIntent();
        int symptomId = intent.getIntExtra("symptomId", -1);
        String symptomName = intent.getStringExtra("symptomName");
        String descriptionName = intent.getStringExtra("description");
        String remedies = intent.getStringExtra("remedies");

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
        }else if (symptomName.equals("Weakness")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine2.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/painurine3.png", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/asthenia-weakness";
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
            slideModels.add(new SlideModel("https://www.amwwall.com/wp-content/uploads/2018/12/Muscle-and-Joint-Treatment-Wall-Township-350x350.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.painscale.com/cms/imgs/dd427880-a85c-11ea-835d-4bb524880da2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://novusspinecenter.com/wp-content/uploads/2018/10/Joint-Pain_2000x1976.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17752-joint-pain";
        }else if (symptomName.equals("Stiffness")) {
            slideModels.add(new SlideModel("https://cdn.prod.website-files.com/60d3395d60e9503a507bae32/60ffa47387a2e66a47329797_Muscle%20Stiffness.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn-prod.medicalnewstoday.com/content/images/articles/320/320545/woman-in-exercise-and-sports-wear-with-muscle-stiffness-in-back.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://orthotoc.com/wp-content/uploads/2018/12/Muscle-Stiffness.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/25147-muscle-stiffness";
        }else if (symptomName.equals("Redness")) {
            slideModels.add(new SlideModel("https://skinkraft.com/cdn/shop/articles/Facial-Redness_1024x400.jpg?v=1584087937", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://gladskin.com/cdn/shop/articles/header-419954.png?v=1677712926", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.lotusbotanicals.com/cdn/shop/articles/Screenshot_2023-01-20_at_9.44.20_AM.png?v=1694422395", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/323521";
        }else if (symptomName.equals("Itchy Rash")) {
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/sites/3/2020/01/327469_2200-800x1200.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/03/Contact-dermatitis-of-the-arm-body19.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.everydayhealth.com/images/skin-beauty/common-types-of-rashes-00-1440x810.jpg?w=720", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if(symptomName.equals("High Fever")){
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever2.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fever3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/10880-fever";
      }
//        else if (symptomName.equals("Sneezing")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
//        }
        else if (symptomName.equals("Severe Headache")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache1.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/headache3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/9639-headaches";
        }else if (symptomName.equals("Pain Behind the Eyes")) {
            slideModels.add(new SlideModel("https://gv-brxm.imgix.net/binaries/_ht_1669733317165/content/gallery/gb-visionexpress/content-pages/eye-health/eye-health-2022/pain-behind-the-eye/symptoms_d.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://media.istockphoto.com/id/503995760/video/allergic-man-scratching-his-eyes.jpg?s=640x640&k=20&c=PLgDsbHLFqclM-zrwrkAdBmSng_hU73MCmAqAAMOiF4=", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://res.cloudinary.com/leightons/image/upload/c_fill,f_auto,g_auto,h_600,w_800/jMQwZ0idUBdKGBxtGHo4YGdxINPj8wPB3Mxy00wT.png", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/pain-behind-eye#:~:text=Pain%20behind%20the%20eye%20can,with%20many%20different%20health%20conditions.";
        }else if (symptomName.equals("Joint and Muscle Pain")) {
            slideModels.add(new SlideModel("https://drmaggieyu.com/wp-content/uploads/2023/11/big_osteopenia.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShqGoqumsOCRPjwR4nXUvoFL1bD9pgSMK_qQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.amwwall.com/wp-content/uploads/2018/12/Muscle-and-Joint-Treatment-Wall-Township.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/muscle-and-joint-pain";
        }else if (symptomName.equals("Red Eyes")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6Wcs8nTkVyGG6b3DQz3CwDkAzFG-LwCj3ZQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://myvision.org/wp-content/uploads/2022/04/man-with-bloodshot-eyes-664x443.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsREJRmwilcQ7OKP58e46zfBZewWPVJ6kjvQ&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17690-red-eye";
        }else if (symptomName.equals("Itching")) {
            slideModels.add(new SlideModel("https://post.healthline.com/wp-content/uploads/2021/03/rash-1296x728-1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/what-causes-itching-1570469798.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://ghc.health/cdn/shop/articles/What_Causes_Skin_Itching_Understanding_the_Root_of_the_Problem_720x.jpg?v=1681968800", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/11879-pruritus";
        }else if (symptomName.equals("Tears")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShacPw2o3IGmOmF75ypp82mE63tkoXPHP0bA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.verywellhealth.com/thmb/BKvoQy7MR4CjTkTrk_hxv1u6ASw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/tear-falling-from-woman-s-eye--close-up-200239461-001-59da598a6f53ba001044eb61.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/0/08/USMC-04952.jpg", ScaleTypes.FIT));
            url = "https://medlineplus.gov/ency/article/003036.htm";
        }else if (symptomName.equals("Swollen Eyelids")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNwTTnzulxDnkI97JjPLmh63exyd0d7WbDGg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgRJ2I7KU6oru0AWDeJDWoB9xD1HWH8AUaJg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.laxmieye.org/blog/wp-content/uploads/2024/12/swollen-eyelids-cause-treatment.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/10032-blepharitis";
        }else if (symptomName.equals("Jaundice")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTj70tgEJYVdoe4Ib80_SqIQI7cBOxPJLqvsQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBEybbF45Ec3DgzkKdYtZewtDZrY7mZqbiSA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.health.com/thmb/OqJFVKQKXTs9EHE8vuvtxr1XKVc=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/health-GettyImages-1297312951-f23a8ab26cce48d784c5b5675eb5d57f.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15367-adult-jaundice";
        }else if (symptomName.equals("Bloating")) {
            slideModels.add(new SlideModel("https://www.health.com/thmb/ZR7AMEMgyVxuALW62MsnFsYa6s8=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1499661935-76f60a6a2b104a54bd6b9bb03520e890.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.baptisthealth.com/-/media/images/migrated/blog-images/teaser-images/woman-bloated-after-eating-1280x853.jpg?rev=2669d46c44c34df888967eb9eeae5e68", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.shopify.com/s/files/1/0151/9191/files/Untitled_design-2_480x480.png?v=1703017408", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21740-bloated-stomach";
        }else if (symptomName.equals("Red Bumps")) {
            slideModels.add(new SlideModel("https://mddermcare.com/wp-content/uploads/2021/06/Red-Bumps-on-Neck-1024x635.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/05/Allergic-eczema-1296x728-body.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/woman-back-with-acne-red-spots-skin-disease-royalty-free-image-1646431493.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Confusion")) {
            slideModels.add(new SlideModel("https://frontiermgmt.com/wp-content/uploads/2024/04/iStock-1367834825.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://mindedforfamilies.org.uk/content/delirium_sudden_confusion_in_physical_illness/course/assets/8231321e7c43e3cdf9da4df627fc2a4303ba30e3.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://pakobserver.net/wp-content/uploads/2023/09/logo-10.webp", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/25206-confusion";
        }
//        else if (symptomName.equals("Difficulty Speaking")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/diseases/17653-dysarthria";
//        }
//        if (symptomName.equals("Rapid Heart Rate")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/diseases/22108-tachycardia";
//        }
//        else if (symptomName.equals("Low Blood Pressure")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/diseases/21156-low-blood-pressure-hypotension";
//        }
        else if (symptomName.equals("Chest Tightness")) {
            slideModels.add(new SlideModel("https://sa1s3optim.patientpop.com/assets/images/provider/photos/2389840.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://sa1s3optim.patientpop.com/assets/images/provider/photos/2559345.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVsip4Gf39_-maJpvNBBWbxXW83mksp9nRPg&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/sneezing";
        }else if (symptomName.equals("Extreme Fatigue")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/fatigue3.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21206-fatigue";
        }else if (symptomName.equals("Difficulty Swallowing")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRn9wLmSKHQZ5bhxlpLsWpzzs5QBvvrl9CnJw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://c.ndtvimg.com/2022-02/tqb279hg_swollen-throat-home-remedies-_625x300_22_February_22.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://medicine.tulane.edu/sites/default/files/pictures/dysphagia_800W_AdobeStock_211652869-compressor.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21195-dysphagia-difficulty-swallowing";
        }
//        else if (symptomName.equals("Abdominal Pain")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
//        }
        else if (symptomName.equals("Skin Irritation")) {
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/03/Measles-on-the-torso-of-a-child-body9.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/ds00720-im00515-ans7-pityriasis-roseathu-jpg-copy-1674585718.jpg?crop=1.00xw:1.00xh;0,0&resize=980:*", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/1024px-cross-reaction-rash-1609359684.jpg?crop=0.9609375xw:1xh;center,top&resize=980:*", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Cloudy Urine")) {
            slideModels.add(new SlideModel("https://images.healthshots.com/healthshots/en/uploads/2023/12/16105752/cloudy-urine.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://facty.mblycdn.com/fh/resized/2018/02/730x487/Symptoms-Urinary-Problems.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scarysymptoms.com/wp-content/uploads/2013/06/URINE-By-Igor-Nikushin-1024x683.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21894-cloudy-urine";
        }else if (symptomName.equals("Seizure")) {
            slideModels.add(new SlideModel("https://www.metropolisindia.com/upgrade/blog/upload/24/05/Seizures1715416788.webp", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cprcare.com/wp-content/uploads/2022/12/first-aid-tips-for-helping-someone-having-a-seizure-img.webp", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.hindustantimes.com/ht-img/img/2024/03/28/550x309/Can_Essential_Oils_Help_With_Seizures_And_Epilepsy_1711604530926_1711604531093.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/22789-seizure";
        }else if (symptomName.equals("Unusual Sensations")) {
            slideModels.add(new SlideModel("https://images.hindustantimes.com/img/2022/10/04/1600x900/uday-mittal-bwKtz4YVtmA-unsplash_1664883695240_1664883708689_1664883708689.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.calmclinic.com/storage/images/74/qhh5pg/tax/w305.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.calmclinic.com/storage/images/42/qhh40s/tax/w305.png", ScaleTypes.FIT));
            url = "https://www.physio.co.uk/what-we-treat/neurological/symptoms/sensory-problems/abnormal-sensation.php#:~:text=Abnormal%20sensation%20is%20when%20a,resulting%20from%20a%20neurological%20problem.";
        }else if (symptomName.equals("Loss of Consciousness")) {
            slideModels.add(new SlideModel("https://www.verywellhealth.com/thmb/lxuHnDAJNV__cqZ6rIUpbSzWZu4=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/fainting-136811247-5c0a8b52c9e77c0001e77359.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.heart.org/-/media/Images/Health-Topics/Arrhythmia/woman-passed-out.jpg?h=533&w=800&sc_lang=en", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJ1yjyU47OMgigmX1kjOIpPBmQDPJEGE3s2tISDzhADyQnyDWkf39dy6Zd6mY3L4krCnE&usqp=CAU", ScaleTypes.FIT));
            url = "https://www.clinicbarcelona.org/en/assistance/be-healthy/loss-of-consciousness";
        }else if (symptomName.equals("Reduced Range of Motion")) {
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/2020/08/woman-at-beach-make-namaste-behind-1200x628-facebook-1200x628.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/2019/04/Male_Physical_Therapy_732x549-thumbnail.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://neuroandspineconsultants.com/wp-content/uploads/2024/07/limited-range-of-motion.jpg", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/limited-range-of-motion#:~:text=When%20a%20joint%20does%20not,the%20muscles%2C%20pain%20or%20disease.";
        }else if (symptomName.equals("Sweating")) {
            slideModels.add(new SlideModel("https://vitalrecord.tamu.edu/wp-content/uploads/2016/05/w_sweating_thefacialfitness.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.ejisinc.com/cdn/shop/articles/sweat-so-much-feature_1600x.jpg?v=1608065473", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://static.toiimg.com/thumb/msid-91842657,width-400,resizemode-4/91842657.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17113-hyperhidrosis";
        }else if (symptomName.equals("Painful Rash")) {
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/05/Scabies-on-the-hand-body1.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.pnghttps://images.prismic.io/gohealth/MmYzYTgyY2ItMzZlMy00M2M0LTkxYTYtMjk2NDc1YzBjMTMy_whats-that-rash-scabies.png?auto=compress,format&rect=0,0,854,641&w=854&h=641", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://static.wixstatic.com/media/084b92_71f176275d884229b5dbf29bca5869f5~mv2.png/v1/fill/w_568,h_386,al_c,lg_1,q_85,enc_avif,quality_auto/084b92_71f176275d884229b5dbf29bca5869f5~mv2.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Burning Sensation")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRav1rngbLJHDl5KucdmABqWHgPiSsawtECFg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbNn1bRryPXbF0a8E0_gQQ26ZgMaSXRZtOzA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.onlymyhealth.com/imported/images/2023/June/16_Jun_2023/Why-Do-You-Have-Burning-Sensation-In-Hands-And-Feet-Main.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/burning-sensation";
        }else if (symptomName.equals("Red Blisters")) {
            slideModels.add(new SlideModel("https://www.verywellhealth.com/thmb/b3r7fFrCrx8aVppqlmmOz8R5mYU=/3202x2187/filters:no_upscale():max_bytes(150000):strip_icc()/red-papules-on-the-skin-due-to-scabies-680795427-597a2cc1d088c0001072cd3a.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2023/03/hg-blisters.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://assets.nhs.uk/nhsuk-cms/images/M7290012-Close-up_of_a_blister_after_verruca_c.width-320_FQLvzmZ.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/16787-blisters";
        }else if (symptomName.equals("Bulge or Lump")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzK82xG5EDIEINW1Q6q3MdMD3m8s2wHkJn9w&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.everydayhealth.com/images/skin-beauty/what-is-a-skin-lump-guide-promo-rm-1440x810.jpg?sfvrsn=ce10d53d_5", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://drcalvinong.com/wp-content/uploads/2024/09/Visible-Bulge-a.svg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/lump-in-the-abdomen";
        }else if (symptomName.equals("Difficulty Lifting")) {
            slideModels.add(new SlideModel("https://www.shutterstock.com/image-vector/vector-cartoon-illustration-strong-brain-600nw-2530096213.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0gvLUQmjd9XNCjqiSO7IALQV7Yd2eKi0DTg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://a0.anyrgb.com/pngimg/1926/456/not-easy-strenuous-insist-laborious-sweaty-difficulty-not-sweat-weightlifting-angry-man.png", ScaleTypes.FIT));
            url = "https://www.mayoclinic.org/diseases-conditions/muscular-dystrophy/symptoms-causes/syc-20375388";
        }else if (symptomName.equals("Abdominal Discomfort")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThAtCPhl9upiOWEI7IbE67PDbegcqJSiLfBw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://lh7-us.googleusercontent.com/f3fF177bNIkPNECW2GDahZ3fMLzF-KTLb-QJjenuoeuTN52wbGViINGO09V0ONZVa0WvLCN1pWefFQdP7kCoiTzOH_zOvO7HfJsT0lwpTn4l8mM9O3-eM8hTGiHv94cbelPqKgI_rKTM", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.emergencyphysicians.org/siteassets/emphysicians/all-images/kwtg/stomach-ache3.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/4167-abdominal-pain";
        }else if (symptomName.equals("Cold Sensitivity")) {
            slideModels.add(new SlideModel("https://articles-1mg.gumlet.io/articles/wp-content/uploads/2018/12/feeling-cold-or-cold-intolerance.jpg?compress=true&quality=80&w=640&dpr=2.6", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR30MmHh2euS1IZE69fpSYwmwJh6OLRBFpKsA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbcBu_SCSknzQ0IM1Ag9DFbfjK8CjnH_-ByQ&s", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/cold-intolerance";
        }else if (symptomName.equals("Dry Skin")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkCvt9Q9Nl484P_F_p1habiHjoxsR6N08xFA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://domf5oio6qrcr.cloudfront.net/medialibrary/15370/00da22b4-df92-456d-b30f-7226e4cb151e.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.carehospitals.com/ckfinder/userfiles/images/dry_skin.webp", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/16940-dry-skin";
        }else if (symptomName.equals("Mood Swings")) {
            slideModels.add(new SlideModel("https://yourdost-blog-images.s3-ap-southeast-1.amazonaws.com/wp-content/uploads/2016/03/11153029/mood-swings-compressor.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.driefcase.com/wp-content/uploads/2023/03/mood-swing-1.1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://clearviewtreatment.com/wp-content/uploads/2019/01/iStock-873215718_sm.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/mood-swings";
        }else if (symptomName.equals("Abdominal Cramps")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThAtCPhl9upiOWEI7IbE67PDbegcqJSiLfBw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://lh7-us.googleusercontent.com/f3fF177bNIkPNECW2GDahZ3fMLzF-KTLb-QJjenuoeuTN52wbGViINGO09V0ONZVa0WvLCN1pWefFQdP7kCoiTzOH_zOvO7HfJsT0lwpTn4l8mM9O3-eM8hTGiHv94cbelPqKgI_rKTM", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.emergencyphysicians.org/siteassets/emphysicians/all-images/kwtg/stomach-ache3.jpg", ScaleTypes.FIT));
            url = "https://www.webmd.com/digestive-disorders/stomach-cramps";
        }else if (symptomName.equals("Irritability")) {
            slideModels.add(new SlideModel("https://images.ctfassets.net/ut7rzv8yehpf/n9E8o74KNZh2GjL9GxpX5/a0e740aded04f844897b4054c8fbef6b/woman-in-white-shirt-showing-frustration-3807738.jpg?w=1440&h=960&fl=progressive&q=50&fm=jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_5PkFI9UAFPZEuipVM7CKFNzwvpOm0RqCIQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.prod.website-files.com/6162585b34ebcf5eeea80dde/63f3798e13c6d3b804bf8211_1*8LEA1UMIpTu_I6K7v9JWGQ.png", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/irritability";
        }else if (symptomName.equals("Hot Flashes")) {
            slideModels.add(new SlideModel("https://flo.health/uploads/media/sulu-1200x630/04/9554-Woman%20experiencing%20perimenopause%20hot%20flashes%2001_1006x755.jpg?v=1-0&inline=1", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.vinmec.com/static/uploads/medium_20210224_053615_748431_boc_hoa_o_phu_nu_ma_max_1800x1800_jpg_bf34c81057.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXdqg37KoS69yf-Omb37K8hGt8dFJgXPn7erqgk8j9oUaNhRKL_bWK6YmaPbhEj29k7sA&usqp=CAU", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/15223-hot-flashes";
        }else if (symptomName.equals("Irregular Periods")) {
            slideModels.add(new SlideModel("https://blog.thesirona.com/wp-content/uploads/2022/06/unnamed-3.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.onlymyhealth.com/imported/images/2022/November/02_Nov_2022/main-menstrualirregularities.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://genepowerx.com/wp-content/uploads/2022/05/Irregular-Menstrual-Cycle-700x400.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/14633-abnormal-menstruation-periods";
        }else if (symptomName.equals("Facial Pain")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqkLk0B9372Y_uDU_tHXFMxgpCZsSgZdsBLg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://premierneurologycenter.com/wp-content/uploads/sites/436/2020/06/shutterstock_1334414867.jpg.optimal.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://painfreeodisha.com/wp-content/uploads/2022/04/facial-pain-managment.png", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/face-pain";
        }else if (symptomName.equals("Post-nasal Drip")) {
            slideModels.add(new SlideModel("https://rockytop-ent.com/wp-content/uploads/2024/03/postnasal-drip.webp", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.prod.website-files.com/5ff5fd551315f2211cf00331/61576d6679258d68c998f59d_60344a8ec4bccf67adff511d_post-nasal-drip-e1508788365810.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQyzodytUfZ2ofxq5e8SHXBesA6AckAMzpxjw&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/23082-postnasal-drip";
        }
//        else if (symptomName.equals("Swelling in the Abdomen")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/symptoms/21819-abdominal-distension-distended-abdomen";
//        }
        else if (symptomName.equals("Constant Worry")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1WU8BHDhX0LQN_AWvtRLglUvRCmqmZhMtuA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.verywellmind.com/thmb/qvToQJgWX2TzWQw4WOE9aCmBOlc=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/470621891-56a6e83c5f9b58b7d0e56e42.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://beatingtheblues.co.nz/wp-content/uploads/2024/08/young-man-in-depression-sitting-on-bed-2023-11-27-04-51-51-utc-1024x683.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/23940-generalized-anxiety-disorder-gad";
        }else if (symptomName.equals("Restlessness")) {
            slideModels.add(new SlideModel("https://www.charliehealth.com/wp-content/uploads/2024/06/iStock-1530904519-1.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.betterup.com/hs-fs/hubfs/Blog%20Images/restlessness/restlessness-person-looking-stressed-at-night.jpg?width=1164&name=restlessness-person-looking-stressed-at-night.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.theresasreviews.com/wp-content/uploads/2023/11/shutterstock_1419781835-1360x712.jpg.webp", ScaleTypes.FIT));
            url = "https://www.verywellhealth.com/restlessness-5223436";
        }else if (symptomName.equals("Difficulty Concentration")) {
            slideModels.add(new SlideModel("https://www.healthyplace.com/sites/default/files/images/stories/other-info/newsletter/difficulty-concentrating-healthyplace.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.mentalup.co/img/blog/trouble-concentrating.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/jreidtherapy.com/wp-content/uploads/2017/08/AdobeStock_97101944-min-scaled.jpeg?fit=2560%2C1703&ssl=1", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/unable-to-concentrate";
        }else if (symptomName.equals("Severe Pain in Joints")) {
            slideModels.add(new SlideModel("https://www.amwwall.com/wp-content/uploads/2018/12/Muscle-and-Joint-Treatment-Wall-Township-350x350.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.painscale.com/cms/imgs/dd427880-a85c-11ea-835d-4bb524880da2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://novusspinecenter.com/wp-content/uploads/2018/10/Joint-Pain_2000x1976.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17752-joint-pain";
        }else if (symptomName.equals("Warmth in Affected Area")) {
            slideModels.add(new SlideModel("https://i0.wp.com/post.medicalnewstoday.com/wp-content/uploads/sites/3/2023/01/heat_intolerance_1296x728_header-1024x575.jpg?w=1155&h=1528", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/22111-hyperthermia";
        }else if (symptomName.equals("Lower Back Pain")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9zjpkwNhs6Y0xehrlKFZkp9R7nWlaA9l-1g&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.squareonephysio.ca/hubfs/Imported_Blog_Media/back-pain-spine.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv5B0bUdSX5bdJBRdtX-WYY19vL3rYKHFM9Q&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/7936-lower-back-pain";
        }else if (symptomName.equals("Leg Pain")) {
            slideModels.add(new SlideModel("https://stgaccinwbsdevlrs01.blob.core.windows.net/newcorporatewbsite/blogs/May2024/YfUMxyWG2aCcZlm5TVic.webp", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlZkKzSTAes_usEw0ACx9FYO8O97qdaQPjCQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpctFYcQkBab9-5PtBFwwgM5uT4Wjp9yeacQ&s", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/241968";
        }else if (symptomName.equals("Numbness")) {
            slideModels.add(new SlideModel("https://sa1s3optim.patientpop.com/assets/images/provider/photos/2476488.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://certifiedfoot.com/wp-content/uploads/2018/07/foot-numbness-1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://thumbnails.icloudhospital.com/tr:w-1536,f-webp,q-75/1-a5d021e3-ba85-47f6-892d-b79e4925b301.webp", ScaleTypes.FIT));
            url = "https://www.mayoclinic.org/symptoms/numbness/basics/definition/sym-20050938#:~:text=Numbness%20describes%20a%20loss%20of,both%20sides%20of%20the%20body.";
        }
//        else if (symptomName.equals("Tingling Sensation")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/symptoms/24932-paresthesia";
//        }
        else if (symptomName.equals("Red Welts")) {
            slideModels.add(new SlideModel("https://i0.wp.com/post.medicalnewstoday.com/wp-content/uploads/sites/3/2021/01/699092595_bbc1535f22_o_slide.jpg?w=1155", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTE1FT2WFiaH2O8Q_4F6RBnhsK2wsfEmD-ng&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://m4b6f3p8.delivery.rocketcdn.me/app/uploads/2021/04/urticariaHives_43491_lg.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/8630-hives";
        }else if (symptomName.equals("Chills")) {
            slideModels.add(new SlideModel("https://www.keckmedicine.org/wp-content/uploads/2021/11/A-woman-has-the-chills.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZHqUHSSL4AZ0NO8H0xAJw4bGKyC9UFgGPrQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQhRs8cofcFMf3ZDkxxlCbHPC3GpltqoZSvUA&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21476-chills";
        }else if (symptomName.equals("Belching")) {
            slideModels.add(new SlideModel("https://cdcssl.ibsrv.net/ibimg/smb/774x581_80/webmgr/16/r/a/burp.png.webp?760e0f92e413d829dc03aaa7ef550921", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSggs9wOdhfVqE0uGa2fBoRJwXYDWetk8ZNsA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDovinKWF-mUWdIlcvrJLeJR3XGECVi3loqg&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/belching";
        }else if (symptomName.equals("Swollen Salivary Glands")) {
            slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/b/b6/SubMandObstructionInfection.png", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_a2-5x2D47cC_gZ3K9udGlKDwfDJZXRcFzQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://post.medicalnewstoday.com/wp-content/uploads/sites/3/2021/12/Internal-view-of-swelling-of-salivary-gland-body2.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/15749-sialadenitis-swollen-salivary-gland";
        }else if (symptomName.equals("Muscle Aches")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiBo2xOOfgTZX5ya18nXLnVDaD7yQ9y46j9Q&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/handsome-young-man-feeling-the-pain-in-shoulder-at-royalty-free-image-1648044119.jpg?crop=0.669xw:1.00xh;0.166xw,0&resize=640:*", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr7tTSkV-wvsCwjCCGC-PljjPvP4f5phPmMBBnBDoDRJ0wK3s5DBKhu-CbhCOcRoFzWSk&usqp=CAU", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/myalgia-muscle-pain";
        }else if (symptomName.equals("Skin Rash")) {
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/03/Measles-on-the-torso-of-a-child-body9.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/ds00720-im00515-ans7-pityriasis-roseathu-jpg-copy-1674585718.jpg?crop=1.00xw:1.00xh;0,0&resize=980:*", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/1024px-cross-reaction-rash-1609359684.jpg?crop=0.9609375xw:1xh;center,top&resize=980:*", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17413-rashes-red-skin";
        }else if (symptomName.equals("Red, Ring-Shaped Skin")) {
            slideModels.add(new SlideModel("https://assets.nhs.uk/nhsuk-cms/images/M2700152-Close-up_of_ringworm_infection_on_gir.width-320.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/post.medicalnewstoday.com/wp-content/uploads/sites/3/2022/04/Nummular-eczema-coin-shaped-lesions-of-dry-skin-body2-1024x575.jpg?w=1155&h=1528", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.nidirect.gov.uk/sites/default/files/styles/nigov_float_x1/public/images/A-Z_ringworm-1_22012020.jpg?itok=-pU4-56y", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/4560-ringworm";
        }else if (symptomName.equals("Scaling")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPCT_ej6hAfxAKasCM4dplOExVJ7civRwuBg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/2024/07/psoriasis-rash.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/sites/3/2024/01/Contact-dermatitis-slide.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/scaling-skin";
        }else if (symptomName.equals("Crusting")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROBi09-p4w3-EEyHMtlxxrSDd759K4A1oerQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpXRvSIQG32b6JX6LkGRhXgZeXupqfrzi4rw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.researchgate.net/publication/372358321/figure/fig1/AS:11431281184752004@1693426113996/Scarring-and-crusting-of-the-patients-skin.ppm", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/15134-impetigo";
        }else if (symptomName.equals("Inflammation")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVkJM0kOgVs4roRrs2wJmX3IUvxsy-0yUUjg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZR0tp5pGTlXbBTGa69NLe-lw9ABYaMOqU4J1-QcokKsRk8le-0g2K4JUa7j-niCRKrY4&usqp=CAU", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn.prod.website-files.com/60d3395d60e9503a507bae32/60ffa4e0cc23bb071c76c5e2_Inflammation.png", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21660-inflammation";
        }else if (symptomName.equals("Persistent Cough")) {
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("file:///android_asset/symptoms/cough2.jpg", ScaleTypes.FIT));
            url = "https://info.health.nz/conditions-treatments/lungs/persistent-coughs";
        }else if (symptomName.equals("Mucus Production")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdPEqOHmCTdwvoTVHuuRxPSZ-82T4mzHdUOw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i-cf65.ch-static.com/content/dam/cf-consumer-healthcare/bp-robitussin-v2/en_US/Articles/banners/mucus-vs-phlegm-article.jpg?auto=format", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsClAO8CAJdUU5u9YDE8V1GRug1g9tbHvItcb6ZV_aDyW24BGnpnEgSmJxKlCGdSW7vHA&usqp=CAU", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/body/mucus";
        }
//        else if (symptomName.equals("Abnormal Bleeding")) {
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing1.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing2.png", ScaleTypes.FIT));
//            slideModels.add(new SlideModel("file:///android_asset/symptoms/sneezing3.jpg", ScaleTypes.FIT));
//            url = "https://my.clevelandclinic.org/health/diseases/menometrorrhagia-abnormal-uterine-bleeding";
//        }
        if (symptomName.equals("Heartburn")) {
            slideModels.add(new SlideModel("https://www.carygastro.com/uploads/blog/_1200x630_crop_center-center_82_none/What-Causes-Chronic-Heartburn-FB.jpg?mtime=1645545857", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.gastrosurgeonindia.com/wp-content/uploads/2020/11/Regular-Heart-burn.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn-prod.medicalnewstoday.com/content/images/articles/322/322737/heartburn-vs-acid-reflux.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/9617-heartburn-overview";
        }else if (symptomName.equals("Hearing Loss")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTa_4PRT9smsyV8cHMUewdVNpDq0weA_tQPxQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTOHjfkfz2W85QBGGU49YoU-jlC664Ipa5gIMfkn542F6k9KxdNVTu6t0VP2DweK9Vqhg&usqp=CAU", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://s3.amazonaws.com/media.audiologydesign.com/wp-content/uploads/sites/345/2022/08/29142854/shutterstock_296808683.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/17673-hearing-loss";
        }else if (symptomName.equals("Regurgitation")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8esLzELzBLBGnavXT2X9TEKLN2MP4IOF-xg&s", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/gerd/regurgitation";
        }else if (symptomName.equals("Morning Stiffness")) {
            slideModels.add(new SlideModel("https://www.verywellhealth.com/thmb/n8p99gCgdNnM_DuEbZieLaYLIYA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/166676230-57a6b4b93df78cf459822cb4.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://domf5oio6qrcr.cloudfront.net/medialibrary/4469/joint-stiffness-monring.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQobTDm60M-t4laN6jNAG1AzWQes0h82PDSTQ&s", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/psoriatic-arthritis-stiffness-in-morning";
        }else if (symptomName.equals("Red, Scaly Patches")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRV331__0CqDybb7hbLwJsK1RF8I6uiH6F6dw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYqSR6UWXflZR54i0u0tSL61n55w9Q2yBfDg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.epiphanydermatology.com/wp-content/uploads/2019/01/Basal-cell-carcinoma.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/14403-seborrheic-dermatitis";
        }else if (symptomName.equals("Cracked Skin")) {
            slideModels.add(new SlideModel("https://smb.ibsrv.net/imageresizer/image/article_manager/1200x1200/198337/813625/heroimage0.728481001662128609.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://smb.ibsrv.net/imageresizer/image/article_manager/1200x1200/198337/813625/heroimage0.728481001662128609.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i.shgcdn.com/06739079-6e33-458b-9beb-e7a028f824eb/-/format/auto/-/preview/3000x3000/-/quality/lighter/", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/cracked-skin";
        }else if (symptomName.equals("Stuffy/Runny Nose")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVSvwagD8tAitWggOVipcFExnMOxv-P8uGUQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMNFATF0ZVNLiQErw46XxOlGNkwW_sEjlSBA&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMNFATF0ZVNLiQErw46XxOlGNkwW_sEjlSBA&s", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/stuffy-or-runny-nose-adult";
        }else if (symptomName.equals("Weight Gain")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfpekLwzbJyXHbxQyY8mNbvACppXtnRLhaSw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.healthshots.com/healthshots/en/uploads/2022/10/27205724/weight-gain.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.healthshots.com/healthshots/en/uploads/2022/10/27205724/weight-gain.jpg", ScaleTypes.FIT));
            url = "https://www.mountsinai.org/health-library/symptoms/weight-gain-unintentional#:~:text=Bloating%2C%20or%20swelling%20due%20to,smoking%2C%20you%20might%20gain%20weight.";
        }else if (symptomName.equals("Ear Drainage")) {
            slideModels.add(new SlideModel("https://www.wikihow.com/images/thumb/8/85/Drain-Ear-Fluid-Step-15-Version-2.jpg/v4-460px-Drain-Ear-Fluid-Step-15-Version-2.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://static.wixstatic.com/media/e79598_9976036bcf714fc394a1303ec3a2feee~mv2_d_1200_1600_s_2.jpeg/v1/crop/x_166,y_284,w_943,h_1311/fill/w_352,h_490,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/WhatsApp%20Image%202018-09-11%20at%2011_08_20_jp.jpeg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://static.wixstatic.com/media/e79598_9976036bcf714fc394a1303ec3a2feee~mv2_d_1200_1600_s_2.jpeg/v1/crop/x_166,y_284,w_943,h_1311/fill/w_352,h_490,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/WhatsApp%20Image%202018-09-11%20at%2011_08_20_jp.jpeg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/23570-otorrhea";
        }else if (symptomName.equals("Itchy Feet")) {
            slideModels.add(new SlideModel("https://www.health.com/thmb/VMA5Z_SJrTDhm_Blq8rxYsd-_1g=/724x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1321880860-21c7a7a0228647b5a48b4bbac9c970b7.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.health.com/thmb/VMA5Z_SJrTDhm_Blq8rxYsd-_1g=/724x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1321880860-21c7a7a0228647b5a48b4bbac9c970b7.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.footankleaz.com/wp-content/uploads/2024/08/How-Do-You-Stop-Diabetic-Feet-from-Itching.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/itchy-feet";
        }else if (symptomName.equals("Foul Odor")) {
            slideModels.add(new SlideModel("https://www.kaplansinusrelief.com/wp-content/uploads/shutterstock_2200900075-1.webp", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://thumbs.dreamstime.com/b/elderly-was-sniffing-her-clothes-smell-musty-shirt-moldy-bad-foul-odor-washing-unclean-laundry-senior-woman-smelling-212984591.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://nypost.com/wp-content/uploads/sites/2/2020/12/covid-19-foul-smell.jpg?quality=75&strip=all", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17865-body-odor";
        }else if (symptomName.equals("Pus Formation")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt6jmT2uZ1ykNTfvSl7YoEBTlIGSoNXMajCg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://cdn-prod.medicalnewstoday.com/content/images/articles/249/249182/pus-abscess.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://dims.healthgrades.com/dims3/MMH/1f2e8b9/2147483647/strip/true/crop/3600x2022+0+0/resize/800x449!/quality/75/?url=https%3A%2F%2Fucmscdn.healthgrades.com%2F54%2F1b%2Ffa8214762df062ee2a2cd389f998%2Fistock-842429684-scaled.jpg", ScaleTypes.FIT));
            url = "https://www.healthline.com/health/pus";
        }else if (symptomName.equals("Hard Stools")) {
            slideModels.add(new SlideModel("https://cdn.shopify.com/s/files/1/0764/6851/9258/files/constipation_600x600.jpg?v=1706618743", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://media.post.rvohealth.io/wp-content/uploads/sites/3/2021/01/hard_stool_GettyImages1190102749_Feature-732x549.jpg", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/326205";
        }else if (symptomName.equals("Straining During Bowel Movements")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuViq1iGxYdM1Dei6iYN7o7dLwmos9IKoRkw&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://live-production.wcms.abc-cdn.net.au/e487d2c40d700b8a144458c13a952860?impolicy=wcms_crop_resize&cropH=1326&cropW=994&xPos=11&yPos=0&width=862&height=1149", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://ipa.physio/wp-content/uploads/2021/11/6-300x300.png", ScaleTypes.FIT));
            url = "https://www.medicalnewstoday.com/articles/harmful-to-strain-while-pooping";
        }else if (symptomName.equals("Itchiness")) {
            slideModels.add(new SlideModel("https://post.healthline.com/wp-content/uploads/2021/03/rash-1296x728-1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRp258O13BKO5tCrtvtAvM2VVJD8tXPKCxvJg&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSa94rEBR4Em3kAchCw7Q-Rd3A_HUxj2UjIEQ&s", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/11879-pruritus";
        }else if (symptomName.equals("Skin Peeling")) {
            slideModels.add(new SlideModel("https://www.verywellhealth.com/thmb/s_TIC9zpt6mofGILjlITZ_P3S2w=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1302019384-2c90315145794528b0abd974e94d2ce1.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://images.healthshots.com/healthshots/en/uploads/2023/09/23020323/skin-peeling-1600x900.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://hips.hearstapps.com/hmg-prod/images/sunburn-peeling-skin-1555974888.png?crop=0.665xw:1.00xh;0.279xw,0&resize=640:*", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/17832-peeling-skin";
        }else if (symptomName.equals("Pressure Around Forehead")) {
            slideModels.add(new SlideModel("https://m4b6f3p8.delivery.rocketcdn.me/app/uploads/2022/08/sinusHeadache_59965_lg.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i.dailymail.co.uk/i/newpix/2018/04/23/12/4B74E20800000578-5646873-image-a-91_1524484540105.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://www.sapnamed.com/wp-content/uploads/2021/03/difference-between-headaches-and-migraines-1200x800.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/8257-tension-headaches";
        }else if (symptomName.equals("Neck Pain")) {
            slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHj2rkY7MNr6upXayWjOfR9Q6L9AzHA3qmWQ&s", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://getwellnj.com/wp-content/uploads/2019/03/bigstock-Woman-Suffering-From-Neck-Pain-238305661.jpg", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://sa1s3optim.patientpop.com/filters:format(webp)/assets/images/provider/photos/2624702.jpg", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/symptoms/21179-neck-pain";
        }else if (symptomName.equals("Mouth Sores")) {
            slideModels.add(new SlideModel("https://my.clevelandclinic.org/-/scassets/Images/org/health/articles/21766-mouth-ulcer", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://my.clevelandclinic.org/-/scassets/images/org/health/articles/10945-canker-sores", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://i0.wp.com/post.healthline.com/wp-content/uploads/2022/04/anemia-mouth-ulcers-body3.jpg?w=1155&h=1528", ScaleTypes.FIT));
            url = "https://my.clevelandclinic.org/health/diseases/21766-mouth-ulcer";
        }

        remedyText.setText(remedies);
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