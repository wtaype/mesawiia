plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.mesawii.feature.lab"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:wii"))
    implementation(project(":core:data"))
    implementation(project(":feature:auth"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
