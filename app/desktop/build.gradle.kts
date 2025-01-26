import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(builds.plugins.kotlinJvm)
    alias(builds.plugins.kotlinCompose)
    alias(builds.plugins.compose)
}

group = AppConfig.namespace
version = AppConfig.versionName

val projectVersion by extra("")



dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview funx tionality


    implementation(compose.desktop.currentOs)

    implementation(projects.app.shared)
    implementation(projects.lib.inject)


}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "com.heyanle.easy_bangumi_cm.MainKt"

        jvmArgs(
            "-DversionCode=${AppConfig.versionCode}",
            "-DversionName=${AppConfig.versionName}",
            "-Dnamespace=${AppConfig.namespace}",
        )

        // 有点 trick 但没办法
        if (project.gradle.startParameter.taskNames.contains("Release")
            || project.gradle.startParameter.taskNames.contains("release")) {
            jvmArgs(
                "-Drelease=true",
            )
        }



        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = AppConfig.namespace
            packageVersion = AppConfig.versionName

        }
        buildTypes.release.proguard {
            isEnabled.set(false)
        }
    }
}
