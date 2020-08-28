plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    kotlin("android.extensions")
    kotlin("android")
    kotlin("plugin.serialization") version "1.4.0"
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.1")

    defaultConfig {
        applicationId = "com.avengers.employeedirectory"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mutableMapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true")
                )
            }
        }
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
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    //val kotlinVersion = rootProject.extra.get("kotlinVersion")
    //implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC") // JVM dependency
    implementation(AndroidX.core.ktx)
    //implementation("androidx.core:core-ktx:1.3.1")
    implementation(Kotlin.stdlib.jdk8)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)

    implementation(AndroidX.constraintLayout)
    implementation(KotlinX.coroutines.core)

    // OkHttp Logging Interceptor
    implementation(Square.okHttp3.loggingInterceptor)

    implementation(COIL)

    // Hilt
    val hiltVersion = "2.28-alpha"
    println("HILT VERSION: ${Google.dagger.hilt.android}")

    //implementation(Google.dagger.hilt.android)
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    //kapt(Google.dagger.hilt.android.compiler)
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    val hiltViewModelsVersion = "1.0.0-alpha01"
    //println("HILT VIEWMODEL: ${AndroidX.hilt.lifecycleViewModel}")
    implementation(AndroidX.hilt.lifecycleViewModel)
    //implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hiltViewModelsVersion")
    kapt(AndroidX.hilt.compiler)
    //kapt("androidx.hilt:hilt-compiler:$hiltViewModelsVersion")

    // Retrofit
    implementation(Square.retrofit2.retrofit)
    implementation(JakeWharton.retrofit2.converter.kotlinxSerialization)

    val roomVersion = "2.2.5"
    implementation(AndroidX.room.runtime)
    kapt(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)

    val fragmentVersion = "1.3.0-alpha07"
    implementation(AndroidX.fragmentKtx)

    implementation(AndroidX.activityKtx)

    kapt(AndroidX.lifecycle.compiler)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)

    val navigationVersion = "2.3.0"
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)


    // Test

    //testImplementation(Junit)
    testImplementation("junit:junit:4.13")
    //androidTestImplementation(AndroidX.test.ext)
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")

    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    debugImplementation(AndroidX.fragmentTesting)
    //debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")

    val testVersion = "1.3.0-rc01"
    //androidTestImplementation(AndroidX.test.coreKtx)
    androidTestImplementation("androidx.test:core-ktx:$testVersion")
}

kapt {
    correctErrorTypes = true
}