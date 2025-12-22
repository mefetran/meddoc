plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.detekt)
//    id("jacoco")
    kotlin("kapt")
    id("realm-android")
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
//    config.setFrom("$projectDir/config/detekt.yml")
//    reports {
//        html.required.set(true)
//        html.outputLocation.set(file("build/reports/detekt/detekt_composeApp.html"))
//
//        xml.required.set(true)
//        txt.required.set(false)
//        sarif.required.set(false)
//    }
}

//sonarqube {
//    properties {
//        property("sonar.sources", "src/main/java,src/main/kotlin")
//        property("sonar.tests", "src/test/java,src/test/kotlin")
////        property("sonar.gradle.scanAll", true)
//        property("sonar.exclusions", "**/R.class,**/R\$*.class,**/BuildConfig.*,**/Manifest*.*")
//        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
//    }
//}

android {
    namespace = "mefetran.dgusev.meddocs"
    compileSdk = 36

    defaultConfig {
        applicationId = "mefetran.dgusev.meddocs"
//        testApplicationId = "mefetran.dgusev.meddocs.test"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1"

//        testInstrumentationRunner = "io.cucumber.android.runner.CucumberAndroidJUnitRunner"
        testInstrumentationRunner = "mefetran.dgusev.meddocs.HiltTestRunner"

        sourceSets {
            getByName("androidTest").assets.srcDirs("src/androidTest/assets")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    // Just want to try it
    androidResources {
        localeFilters.addAll(setOf("en", "ru"))
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                maybeCreate("java").apply {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)

    // Domain and Data module
    implementation(project(":domain"))
    implementation(project(":data"))

    // Jetpack Security
    implementation(libs.androidx.security.crypto)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // KotlinX serialization
    implementation(libs.org.jetbrains.kotlinx.serialization.json)

    // KotlinX coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Google Font Provider
    implementation(libs.androidx.compose.ui.google.fonts)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.compose)
    androidTestImplementation(libs.androidx.navigation.testing)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Lifecycle
    kapt(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Datastore
    implementation(libs.androidx.datastore)

    // Protobuf Google
    implementation(libs.google.protobuf.javalite)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.hamcrest)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.ultron.compose)

    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.compose)

    androidTestImplementation(libs.cucumber.android)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.cucumber.picocontainer)
    coreLibraryDesugaring(libs.android.tools.desugar)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
