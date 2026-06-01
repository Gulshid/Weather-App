plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Hilt
    id("com.google.dagger.hilt.android")
    // KSP for Room & Hilt code generation
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.yourname.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourname.weatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // API key from local.properties — we'll set this up in Step 5
        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${project.findProperty("WEATHER_API_KEY") ?: ""}\""
        )
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

    kotlinOptions {
        jvmTarget = "11"
    }

    // Enable ViewBinding — no more findViewById!
    buildFeatures {
        viewBinding = true
        buildConfig = true  // needed for BuildConfig.WEATHER_API_KEY
    }
}

dependencies {
    // ── Core Android ──────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ── Navigation Component ──────────────────────────────────────
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")

    // ── ViewModel + LiveData ──────────────────────────────────────
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // ── Hilt (Dependency Injection) ───────────────────────────────
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    // ── Retrofit (Networking) ─────────────────────────────────────
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ── Room (Local Database) ─────────────────────────────────────
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ── Coroutines ────────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // ── Glide (Image Loading) ─────────────────────────────────────
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ── Shimmer (Loading Effect) ──────────────────────────────────
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // ── Lottie (Animations) ───────────────────────────────────────
    implementation("com.airbnb.android:lottie:6.6.0")

    // ── SwipeRefreshLayout ────────────────────────────────────────
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // ── Testing ───────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}