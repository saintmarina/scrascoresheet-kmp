package com.steelsoftware.scrascoresheet

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.net.toUri

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

class AndroidUrlOpener(private val context: Context) : UrlOpener {
    override fun openAppStoreUrl() {
        // TODO: update the url
        val url = "https://play.google.com/store/apps/details?id=com.saintmarina.scrabblescore"
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}