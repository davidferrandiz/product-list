// =====================================================================
// :core:ui — DESIGN SYSTEM en miniatura
// ---------------------------------------------------------------------
// Tema (colores/tipografía) y componentes Compose COMPARTIDOS entre
// features (loading, error…). Sin ViewModels, sin lógica: solo piezas
// visuales tontas y reutilizables.
// Paralelo iOS: tu paquete "DesignSystem" con Views de SwiftUI comunes.
// =====================================================================
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)  // compiler plugin de Compose (recompone @Composable)
}

android {
    namespace = "com.davidferrandiz.mangostore.core.ui"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true   // habilita Compose en AGP (necesario ADEMÁS del plugin de arriba)
    }
}

dependencies {
    // El BOM fija las versiones de TODO Compose de forma coherente.
    // api(platform(...)): lo exponemos transitivamente para que quien
    // dependa de :core:ui herede el mismo BOM (una sola verdad de versiones).
    api(platform(libs.compose.bom))

    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.material3)
    api(libs.compose.material.icons)      // iconos (corazón de favoritos)
    api(libs.compose.ui.tooling.preview)  // @Preview en las features

    // El renderer de previews solo entra en builds de debug
    debugImplementation(libs.compose.ui.tooling)
}
