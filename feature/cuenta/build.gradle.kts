plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mesawii.feature.cuenta"

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
