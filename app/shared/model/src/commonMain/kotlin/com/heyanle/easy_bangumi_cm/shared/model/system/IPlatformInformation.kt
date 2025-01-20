package com.heyanle.easy_bangumi_cm.shared.model.system

interface IPlatformInformation {
    val namespace: String
    val platformName: String

    val versionCode : Int
    val versionName : String

    val isRelease: Boolean

    val isAndroid: Boolean
    val isIos: Boolean
    val isDesktop: Boolean
}