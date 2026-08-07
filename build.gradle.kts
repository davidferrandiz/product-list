// =====================================================================
// build.gradle.kts (RAÍZ)
// ---------------------------------------------------------------------
// Solo hace una cosa: DECLARAR los plugins (con su versión, vía catalog)
// sin aplicarlos ("apply false"). Así Gradle carga cada plugin UNA vez
// en un classpath común y cada módulo lo aplica ya sin versión.
// Si un módulo pusiera su propia versión, tendríamos dos fuentes de
// verdad y conflictos de classpath — la trampa clásica.
// =====================================================================
plugins {
    alias(libs.plugins.android.application) apply false // :app
    alias(libs.plugins.android.library) apply false     // :data, :core:ui, :feature:*
    alias(libs.plugins.kotlin.jvm) apply false          // :domain (Kotlin puro)
    alias(libs.plugins.kotlin.compose) apply false      // módulos con UI Compose
    alias(libs.plugins.kotlin.serialization) apply false// :data (DTOs @Serializable)
    alias(libs.plugins.ksp) apply false                 // Room + Hilt (codegen)
    alias(libs.plugins.hilt) apply false                // DI
}
