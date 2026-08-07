// =====================================================================
// :feature:products — pantalla de LISTADO de productos
// ---------------------------------------------------------------------
// Una feature = ViewModel + Composables de UNA pantalla.
// Solo ve :domain (use cases/modelos) y :core:ui (tema/componentes).
// Intentar importar Retrofit o Room aquí → error de compilación,
// porque :data no está en su classpath. Frontera real, no convención.
// =====================================================================
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)   // para @HiltViewModel
}

android {
    namespace = "com.davidferrandiz.mangostore.feature.products"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
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
    implementation(project(":domain"))    // use cases + modelos
    implementation(project(":core:ui"))   // tema + componentes (trae el BOM de Compose vía api())

    // ViewModel con StateFlow + colección lifecycle-aware en Compose
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)    // collectAsStateWithLifecycle()

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)      // hiltViewModel() en el NavHost

    // Imágenes de producto
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Tests (Tarea 7)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
}
