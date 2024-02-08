pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url  = "https://chaquo.com/maven-test")
        maven(url  = "https://chaquo.com/maven")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        google()
        mavenCentral()

    }
}

rootProject.name = "Bitcode"
include(":app")
