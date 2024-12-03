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
            from(files("versions/builds.version.toml"))
        }
        create("libs") {
            from(files("versions/libs.version.toml"))
        }
    }
}

fun includeModule(moduleName: String, dir: String? = null) {
    include(moduleName)
    if (dir != null) {
        project(moduleName).projectDir = file(dir)
    }
}

rootProject.name = "EasyBangumi"
includeModule(":app:android", "app/android")
includeModule(":app:desktop", "app/desktop")
includeModule(":app:shared", "app/shared")

// component
includeModule(":component:room", "component/component-room")

// utils
includeModule(":utils:android", "utils/android")
includeModule(":utils:jvm", "utils/jvm")

// base
includeModule(":base", "base")

