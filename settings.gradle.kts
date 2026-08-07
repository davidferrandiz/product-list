pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "mango-test"

include(":app")
include(":domain")
include(":data")
include(":core:ui")
include(":feature:products")
include(":feature:favorites")
include(":feature:profile")
