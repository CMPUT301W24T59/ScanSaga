plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.scansaga"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.scansaga"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    android {
        defaultConfig {
            vectorDrawables.useSupportLibrary = true
        }
    }
}

dependencies {
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.google.firebase:firebase-storage:20.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:21.0.0") // Replace version number with the latest version
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("androidx.activity:activity-ktx:1.2.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testImplementation("org.mockito:mockito-inline:3.+")
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation ("androidx.test:core:1.5.0")
    testImplementation ("androidx.test:runner:1.5.2")
    testImplementation ("androidx.test:rules:1.5.0")
    testImplementation ("androidx.test.ext:junit-ktx:1.1.5")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-accessibility:3.5.1")
    androidTestImplementation ("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("org.mockito:mockito-android:3.8.0")
    androidTestImplementation ("org.mockito:mockito-core:3.11.2") // Required for mocking final classes (like Firebase components)
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.3.0")

    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.1")




}