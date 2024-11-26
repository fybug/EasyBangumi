import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

group = "com.heyanle.easy_bangumi_cm.budiness.media"
version = "1.0.0"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    jvm()
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {

        }
        commonMain.dependencies {

        }
        desktopMain.dependencies {

        }
    }
}

android {
    namespace = "com.heyanle.easy_bangumi_cm.budiness.cartoon"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
