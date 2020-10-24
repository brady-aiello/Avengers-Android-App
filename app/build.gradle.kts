plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.squareup.sqldelight")
    kotlin("kapt")
    kotlin("android.extensions")
    kotlin("android")
    kotlin("plugin.serialization") version "1.4.10"
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")
    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.avengers.employeedirectory"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner = "com.avengers.employeedirectory.CustomHiltTestRunner"
        signingConfig = signingConfigs.getByName("debug")
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/license.txt")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/*.kotlin_module")
    }

    sourceSets["androidTest"].java {
        srcDir("src/shared/java")
    }

    sourceSets["test"].java {
        srcDir("src/shared/java")
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        // "-Xjvm-default=all-compatibility" will also work.
        freeCompilerArgs = listOf("-Xjvm-default=all")
        jvmTarget = "1.8"
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    // Source compatibility
    implementation(Kotlin.stdlib.jdk8)

    // Core Jetpack dependencies
    implementation(AndroidX.legacy.supportV4)
    implementation(AndroidX.core.ktx)

    // UI
    implementation(AndroidX.appCompat)
    implementation(AndroidX.constraintLayout)
    implementation(Google.android.material)

    // Serialization
    //implementation(KotlinX.serialization.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

    // Coroutines
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)

    // OkHttp Logging Interceptor
    implementation(Square.okHttp3.loggingInterceptor)

    // Image loading
    implementation(COIL)
    implementation("com.github.Commit451.coil-transformations:transformations-face-detection:1.0.0")

    // Hilt
    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.android.compiler)
    implementation(AndroidX.hilt.lifecycleViewModel)
    kapt(AndroidX.hilt.compiler)

    // Retrofit
    implementation(Square.retrofit2.retrofit)
    implementation(JakeWharton.retrofit2.converter.kotlinxSerialization)

    // SQLDelight
    implementation(Square.sqlDelight.drivers.android)
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.4.3")
    testImplementation(Square.sqlDelight.drivers.jdbcSqlite)

    // Lifecycle
    implementation(AndroidX.fragmentKtx)
    implementation(AndroidX.activityKtx)
    kapt(AndroidX.lifecycle.compiler)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)

    // Navigation
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)

    // Unit Test
    testImplementation(Testing.junit4)
    androidTestImplementation(AndroidX.test.ext.junit)

    // Coroutines Test
    androidTestImplementation(KotlinX.coroutines.test)
    testImplementation(KotlinX.coroutines.test)

    // UI Test
    androidTestImplementation(AndroidX.test.espresso.core)
    debugImplementation(AndroidX.fragmentTesting)
    androidTestImplementation(AndroidX.test.coreKtx)
    androidTestImplementation(AndroidX.archCore.testing)

    // Hilt / Dagger Test
    testImplementation(Google.dagger.hilt.android.testing)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptTest(Google.dagger.hilt.android.compiler)
    kaptAndroidTest(Google.dagger.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}