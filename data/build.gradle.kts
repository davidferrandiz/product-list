// =====================================================================
// :data — INFRAESTRUCTURA: red (Retrofit) + persistencia (Room)
// ---------------------------------------------------------------------
// Único módulo que sabe de HTTP y SQL. Implementa las interfaces de
// repositorio que :domain define. Las features NUNCA dependen de él:
// solo :app lo ve (para que Hilt conecte interfaz → implementación).
// Nota AGP 9: Kotlin viene INTEGRADO en el plugin de Android (built-in
// Kotlin) → ya no se aplica org.jetbrains.kotlin.android (daría error).
// =====================================================================
plugins {
    alias(libs.plugins.android.library)      // librería Android (necesita contexto para Room)
    alias(libs.plugins.kotlin.serialization) // habilita @Serializable en los DTOs
    alias(libs.plugins.ksp)                  // codegen de Room y Hilt (KSP, no kapt: kapt es incompatible con built-in Kotlin)
    alias(libs.plugins.hilt)                 // los @Module de DI de esta capa viven aquí
}

android {
    // Namespace = paquete base para el R generado y el manifest merge.
    // Obligatorio en el DSL desde AGP 8 (ya no va en el AndroidManifest).
    namespace = "com.davidferrandiz.mangostore.data"
    compileSdk = 37                          // API contra la que COMPILAMOS (siempre la última)

    defaultConfig {
        minSdk = 26                          // decisión de negocio: ~96% de dispositivos (ver DECISIONES.md)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Con built-in Kotlin, jvmTarget de Kotlin hereda de aquí:
        // una sola declaración, cero desalineaciones.
    }
}

dependencies {
    // La capa que implementa depende de la capa de contratos (inversión
    // de dependencias): :data conoce a :domain, nunca al revés.
    implementation(project(":domain"))

    // --- Red ---
    implementation(libs.retrofit)                       // definición declarativa de la API
    implementation(libs.retrofit.kotlinx.serialization) // converter oficial JSON <-> @Serializable
    implementation(libs.okhttp)                         // motor HTTP
    implementation(libs.okhttp.logging)                 // log de peticiones en debug
    implementation(libs.kotlinx.serialization.json)     // parser JSON

    // --- Persistencia ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)                       // suspend + Flow en los DAOs
    ksp(libs.room.compiler)                             // genera el código de la DB en compilación

    // --- DI ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // --- Corrutinas ---
    implementation(libs.coroutines.core)

    // --- Tests (Tarea 7) ---
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}
