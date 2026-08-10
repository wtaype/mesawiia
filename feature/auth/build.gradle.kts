plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.mesawii.feature.auth"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:wii"))
    implementation(project(":core:data"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.google.id)
}
