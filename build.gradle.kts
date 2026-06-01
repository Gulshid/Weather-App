// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Hilt plugin — must be here at root level
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    // KSP (Kotlin Symbol Processing) — modern replacement for kapt
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}