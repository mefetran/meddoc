plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
    id("realm-android")
}

android {
    namespace = "mefetran.dgusev.meddocs"
    compileSdk = 35

    defaultConfig {
        applicationId = "mefetran.dgusev.meddocs"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
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
    // KotlinX serialization
    implementation(libs.org.jetbrains.kotlinx.serialization.json)

    // KotlinX coroutines
    implementation(libs.org.jetbrains.kotlinx.coroutines)

    // Google Font Provider
    implementation(libs.androidx.compose.ui.google.fonts)

    // Jetpack Navigation
    implementation(libs.androidx.navigation)
    androidTestImplementation(libs.androidx.navigation.testing)

    // Hilt
    implementation(libs.hilt.android)
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}