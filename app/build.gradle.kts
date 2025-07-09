plugins {
    alias(libs.plugins.android.application)
    // Add Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ungdunglichhoctap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ungdunglichhoctap"
        minSdk = 24
        targetSdk = 35
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase BoM (Bill of Materials)
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
    // Firebase Storage
    implementation("com.google.firebase:firebase-storage")
    // Google Play services for Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.8")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}