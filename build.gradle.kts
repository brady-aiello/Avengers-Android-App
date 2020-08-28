// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion: String by rootProject.extra { "1.4.0" }
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha07")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(Google.dagger.hilt.android.gradlePlugin)
        //classpath("com.google.dagger:hilt-android-gradle-plugin:2.28.3-alpha")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }
}

tasks.register("clean", type=Delete::class) {
    delete(rootProject.buildDir)
}
