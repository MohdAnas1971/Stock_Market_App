plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
    //hilt
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    ///Auth
   // id("com.google.gms.google-services")
}

android {
    namespace = "com.example.stockmarketapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.stockmarketapp"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            optIn.add("kotlin.RequiresOptIn")
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


  //navigation
    implementation(libs.androidx.navigation.compose)
    // For type-safe arguments with Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Or the latest stable version

    //for more icons
    implementation(libs.androidx.material.icons.extended)
    // Hilt dependency
    implementation(libs.hilt.android.v2562)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose) // or latest
    //Serialization
    implementation(libs.kotlinx.serialization.json)
    //Coil for images
    implementation(libs.coil.compose)
    // Firebase Auth (BOM manages versions)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)
    implementation(libs.androidx.credentials.play.services.auth)
    // Google Sign-In
    implementation(libs.play.services.auth)
    // Coroutines + Play Services await()
  //  implementation(libs.kotlinx.coroutines.android)
 //   implementation(libs.kotlinx.coroutines.play.services)
    // Preferences Data Store
    implementation(libs.androidx.datastore.preferences)
    //Ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Room Database
    val room_version = "2.8.3"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    //CSV READER
    implementation("com.opencsv:opencsv:5.9")

    // Moshi for retrofit
    // Moshi converter for parsing JSON responses (if using with Retrofit)
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
// Check for the latest version
}