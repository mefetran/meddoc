import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
//    alias(libs.plugins.sonarqube)
//    id("info.solidsoft.pitest") version "1.19.0-rc.2" apply false
}

buildscript {
    dependencies {
        classpath(libs.realm.gradle.plugin)
    }
}


//sonarqube {
//    properties {
//        property("sonar.host.url", "http://localhost:9000")
//        property("sonar.token", "squ_cab4cd3e0254530e5fd68260cb1475f55ce26590")
//        property("sonar.projectKey", "Meddoc")
//        property("sonar.language", "kotlin")
//        property("sonar.sourceEncoding", "UTF-8")
//    }
//}

tasks.register("detektAll") {
    dependsOn(":app:detekt", ":data:detekt", ":domain:detekt")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<Detekt>().configureEach {
            val moduleName = this.project.name
            reports {
                html.required.set(true)
                html.outputLocation.set(
                    file("$buildDir/reports/detekt/${moduleName}.html")
                )
            }
        }
    }
}