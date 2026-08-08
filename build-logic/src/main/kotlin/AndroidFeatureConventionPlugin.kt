import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("mesawii.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                "implementation"(project(":core:wii"))
                "implementation"(libs.findLibrary("androidx.activity.compose").get())
            }
        }
    }
}

private val Project.libs get() =
    extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs")
