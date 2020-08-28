import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.5")
}

bootstrapRefreshVersions()

rootProject.name = "Avengers"
include(":app")
