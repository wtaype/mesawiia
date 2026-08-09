plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mesawii.feature.empresas"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:wii"))
    implementation(project(":core:data"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
