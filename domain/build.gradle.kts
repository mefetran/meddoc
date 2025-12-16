plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.detekt)
//    id("jacoco")
}

detekt {
    source.setFrom(
        files(
            "src/main/kotlin",
            "src/main/java",
            "src/test/java",
            "src/test/kotlin",
            "src/androidTest/kotlin",
        )
    )
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt.yml")
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt/detekt_composeApp.html"))

        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}


//sonarqube {
//    properties {
//        property("sonar.sources", "src/main/java,src/main/kotlin")
//        property("sonar.tests", "src/test/java,src/test/kotlin")
//        property("sonar.exclusions", "**/R.class,**/R\$*.class,**/BuildConfig.*,**/Manifest*.*")
//        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
//    }
//}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlin.test)
}