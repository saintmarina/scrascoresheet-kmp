package com.steelsoftware.scrascoresheet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

interface UrlOpener {
    fun openAppStoreUrl()
}