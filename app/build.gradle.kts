// =====================================================================
// :app — EL ENSAMBLADOR
// ---------------------------------------------------------------------
// Único módulo "application". No contiene lógica de negocio: solo
// Application (@HiltAndroidApp), MainActivity, la navegación (Tarea 6)
// y el wiring final de DI. Es el ÚNICO que ve TODOS los módulos —
// necesario para que Hilt conecte interfaces (:domain) con
// implementaciones (:data) en el grafo final.
// Paralelo iOS: el target de app que importa todos tus paquetes SPM.
// =====================================================================
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.davidferrandiz.mangostore"
    compileSdk = 37   // compilamos contra la última API…

    defaultConfig {
        applicationId = "com.davidferrandiz.mangostore"  // identidad de la app (el "bundle id")
        minSdk = 26        // …soportamos desde Android 8.0 (~96% de dispositivos)
        targetSdk = 36     // …y declaramos comportamiento verificado hasta API 36
                           // (Play exige 36+ desde 31/08/2026; 37 solo tras probar sus behavior changes)
        versionCode = 1
        versionName = "1.0"

        // Runner para los androidTest (Tarea 8)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Prueba técnica: sin ofuscación para que el evaluador lea
            // el APK sin fricción. En producción: true + mapping.
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
    // --- Los módulos del proyecto: aquí se cierra el grafo ---
    implementation(project(":domain"))
    implementation(project(":data"))              // SOLO :app ve :data (wiring de Hilt)
    implementation(project(":core:ui"))
    implementation(project(":feature:products"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:profile"))

    // --- Base Android + Compose ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)   // ComponentActivity + setContent {}
    implementation(libs.lifecycle.runtime.compose)

    // --- Navegación (Tarea 6) ---
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // --- DI ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // --- Corrutinas con dispatcher Main de Android ---
    implementation(libs.coroutines.android)

    // --- androidTest (Tarea 8) ---
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))  // el BOM también en tests instrumentados
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)     // Activity vacía para tests de Compose
}
