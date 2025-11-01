package com.steelsoftware.scrascoresheet

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isAndroid: Boolean = false
    override val isIOS: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()
