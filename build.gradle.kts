import java.net.URI

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion: String by rootProject.extra { "1.4.10" }
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha14")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(Square.sqlDelight.gradlePlugin)
        classpath(Google.dagger.hilt.android.gradlePlugin)
        classpath(AndroidX.navigation.safeArgsGradlePlugin)

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
        maven {
            url = URI("https://jitpack.io")
        }
    }
}

tasks.register("clean", type=Delete::class) {
    delete(rootProject.buildDir)
}
