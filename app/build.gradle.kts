plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.davidferrandiz.mangostore"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.davidferrandiz.mangostore"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:products"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:profile"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.lifecycle.viewmodel.compose)

    implementation(libs.coroutines.android)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}
