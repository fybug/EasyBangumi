package com.heyanle.easy_bangumi_cm.plugin.entity

import java.io.File

/**
 * 拓展清单信息，拓展加载的材料
 * Created by heyanlin on 2024/12/13.
 */
data class ExtensionManifest (
    // 基础信息
    val key: String,
    val label: String,
    val versionCode: Long,
    val libVersion: Int, // 纯纯看番拓展引擎版本

    // 元数据
    val readme: String?,
    val author: String?,
    val icon: Any?,

    // 加载数据
    val loadType: Int,
    val sourcePath: String?, // 拓展源文件位置，可能为空

    val assetsPath: String, // 资源文件位置
    val folderPath: String, // 拓展工作路径，对于解压类拓展为解压路径

    val lastModified : Long, // 最后修改时间，用于判断是否需要重新加载

    // other
    val ext : Any? = null
){

    companion object {
        const val LOAD_TYPE_INNER = 1
        const val LOAD_TYPE_JS_FILE = 2
        const val LOAD_TYPE_JS_PKG = 3
    }

}