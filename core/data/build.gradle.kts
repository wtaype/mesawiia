import java.util.Properties

plugins {
    id("mesawii.android.library")
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.mesawii.core.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("supabase.url", "")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${properties.getProperty("supabase.anon_key", "")}\"")
    }
}

dependencies {
    api(project(":core:wii"))
    
    // Supabase Kotlin SDK
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.0"))
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    // Ktor Engine
    implementation(libs.ktor.client.android)
    implementation(libs.kotlinx.serialization.json)
}
