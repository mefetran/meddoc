plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
//    kotlin("jvm")
//    id("info.solidsoft.pitest") version "1.19.0-rc.2"
    kotlin("kapt")
    id("realm-android")
}

//pitest {
//    targetClasses.set(listOf("mefetran.dgusev.meddocs.data.usecase.*"))
//    mutators.set(listOf("STRONGER"))
//    junit5PluginVersion.set("1.2.3")
//
//    threads.set(4)
//    outputFormats.set(listOf("HTML"))
//}

android {
    namespace = "mefetran.dgusev.meddocs.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    // JavaX Inject
    implementation(libs.javax.inject)

    // Domain module
    implementation(project(":domain"))

    // KotlinX serialization
    implementation(libs.org.jetbrains.kotlinx.serialization.json)

    // KotlinX coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlin.test)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.hamcrest)
    androidTestImplementation(libs.androidx.espresso.core)

//    pitest("org.pitest:pitest-junit5-plugin:1.2.3")
//    pitest("com.arcmutate:pitest-kotlin-plugin:1.5.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}