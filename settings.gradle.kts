// =====================================================================
// settings.gradle.kts — EL MAPA DEL PROYECTO
// ---------------------------------------------------------------------
// Paralelo iOS: es tu .xcworkspace. Declara QUÉ módulos existen y de
// DÓNDE se descargan plugins y dependencias. Se evalúa antes que
// cualquier build.gradle.kts.
// =====================================================================

// De dónde se resuelven los PLUGINS de Gradle (AGP, Kotlin, KSP, Hilt…)
pluginManagement {
    repositories {
        google()             // Repositorio Maven de Google: AGP, AndroidX, Hilt
        mavenCentral()       // Kotlin, KSP, librerías de terceros
        gradlePluginPortal() // Portal oficial de plugins de Gradle
    }
}

// De dónde se resuelven las DEPENDENCIAS de los módulos (Retrofit, Room…)
dependencyResolutionManagement {
    // FAIL_ON_PROJECT_REPOS: prohíbe que un módulo declare sus propios
    // repositorios. Todos usan estos, centralizados. Consistencia forzada.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "mango-test"

// ---------------------------------------------------------------------
// El grafo de módulos (decidido en Tarea 0, ver docs/DECISIONES.md D2):
//   :app        → ensamblaje: Application, NavHost, wiring de Hilt
//   :domain     → Kotlin PURO (sin Android): modelos, interfaces, use cases
//   :data       → Retrofit + Room + mappers; implementa :domain
//   :core:ui    → tema Compose + componentes compartidos
//   :feature:*  → un módulo por pantalla (ViewModel + Composables)
// ---------------------------------------------------------------------
include(":app")
include(":domain")
include(":data")
include(":core:ui")
include(":feature:products")
include(":feature:favorites")
include(":feature:profile")
