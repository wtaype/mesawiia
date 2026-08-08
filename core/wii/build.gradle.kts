plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mesawii.core.wii"

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.core)   // Icons: Lock, Visibility, Check, etc.
    api(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
}
