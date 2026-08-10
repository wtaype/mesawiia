pluginManagement {
    includeBuild("build-logic")
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
rootProject.name = "MesaWii"

include(":app")
include(":core:wii")
include(":core:data")
include(":feature:lab")
include(":feature:bienvenida")
include(":feature:auth")
include(":feature:empresas")
