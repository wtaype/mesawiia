rootProject.name = "build-logic"

// build-logic es un included build separado y no hereda el catálogo de versiones
// del proyecto padre automáticamente. Lo apuntamos manualmente al mismo archivo TOML.
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
