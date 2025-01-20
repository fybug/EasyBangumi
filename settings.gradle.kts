rootProject.name = "EasyBangumi"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }

    versionCatalogs {
        create("builds") {
            from(files("gradle/builds.version.toml"))
        }
        create("libs") {
            from(files("gradle/libs.version.toml"))
        }
    }
}

fun includeModule(moduleName: String, dir: String? = null) {
    include(moduleName)
    if (dir != null) {
        project(moduleName).projectDir = file(dir)
    }
}

includeModule(":app:android", "app/android")
includeModule(":app:desktop", "app/desktop")
includeModule(":app:shared")
includeModule(":app:shared:model")
includeModule(":app:shared:platform")
includeModule(":app:shared:utils")

// ----------cartoon_repository----------
includeModule(":repository", "repository")

// ----------plugin----------
includeModule(":plugin:api", "plugin/api")
includeModule(":plugin:core", "plugin/core")
includeModule(":plugin:utils", "plugin/utils")

// ----------utils----------
includeModule(":inject", "inject")
includeModule(":unifile", "unifile")
includeModule(":javascript", "javascript")

// ----------base----------
includeModule(":compose_base", "compose_base")
//includeModule(":base", "base")

