plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mediforecast"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mediforecast"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


}

dependencies {
//    implementation ("com.sun.mail:android-mail:1.6.7")
//    implementation ("com.sun.mail:android-activation:1.6.7")

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore:24.0.2")
    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    implementation ("com.airbnb.android:lottie:6.1.0")
    implementation ("com.google.firebase:firebase-storage:20.0.1") //to use firebase storage
    implementation ("com.github.bumptech.glide:glide:4.16.0") // glide api to display image using URL
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.google.firebase:firebase-database:20.0.5")
    implementation ("com.github.dhaval2404:imagepicker:2.1")//imagepicker api
//    implementation ("com.jakewharton:butterknife:10.2.3")//Butterknife for binding the ids
//    annotationProcessor ("com.jakewharton:butterknife-compiler:10.2.3")
    implementation ("kr.co.prnd:readmore-textview:1.0.0")// read more
    implementation ("androidx.media3:media3-exoplayer:1.1.0")//media player for video in community post
    implementation ("androidx.media3:media3-ui:1.1.0")//media player for video in community post
    implementation ("androidx.activity:activity-ktx:1.2.3")//imagepicker api
    implementation ("androidx.fragment:fragment-ktx:1.3.3")//imagepicker api
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("com.getkeepsafe.taptargetview:taptargetview:1.13.3")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.room:room-runtime:2.5.2")//room database
    implementation ("androidx.room:room-ktx:2.5.2")//""
    annotationProcessor ("androidx.room:room-compiler:2.5.2")// ""
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs\\activation.jar"))
    implementation(files("libs\\additionnal.jar"))
    implementation(files("libs\\mail.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}