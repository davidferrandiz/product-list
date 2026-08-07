// =====================================================================
// :domain — EL CORAZÓN DE LA ARQUITECTURA
// ---------------------------------------------------------------------
// Módulo Kotlin/JVM PURO. Fíjate en lo que NO hay aquí:
//   - NO plugin com.android.library  → no puede usar el SDK de Android
//   - NO Retrofit, NO Room, NO Compose
// La promesa de la Tarea 0 ("el dominio no sabe que Android existe")
// hecha build script: si alguien intenta importar android.* aquí,
// NO COMPILA. El compilador es el guardián de la arquitectura.
// Bonus: sus tests corren en la JVM a secas, sin emulador → rapidísimos.
// Paralelo iOS: un Swift Package sin `import UIKit` posible.
// =====================================================================
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // Kotlin "a secas" para la JVM. Único módulo que aplica este plugin.
    alias(libs.plugins.kotlin.jvm)
}

// Bytecode Java 17: el mismo target que usan los módulos Android
// (AGP 9 exige JDK 17+), así no hay mezclas raras de bytecode.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Flow y suspend viven en el dominio (las interfaces de repositorio
    // devuelven Flow<...>), así que corrutinas-core SÍ es dependencia
    // legítima: es una librería Kotlin multiplataforma, no de Android.
    implementation(libs.coroutines.core)

    // La anotación @Inject del estándar JSR-330 (javax.inject).
    // Nos permite anotar constructores de use cases SIN depender de Hilt:
    // el dominio declara "soy inyectable", pero no sabe QUIÉN inyecta.
    implementation(libs.javax.inject)

    // Tests del dominio: JUnit puro + corrutinas de test (Tarea 7)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}
