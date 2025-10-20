package com.steelsoftware.scrascoresheet

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

class IOSUrlOpener : UrlOpener {
    override fun openAppStoreUrl() {
        val url = "https://apps.apple.com/us/app/scrabble-score-calculator/id1497216063"
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}