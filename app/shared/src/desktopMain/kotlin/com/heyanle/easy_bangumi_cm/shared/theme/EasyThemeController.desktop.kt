package com.heyanle.easy_bangumi_cm.shared.theme

import com.heyanle.easy_bangumi_cm.base.model.system.IPlatformInformation


actual fun IPlatformInformation.isSupportDynamicColor(): Boolean {
    return false
}