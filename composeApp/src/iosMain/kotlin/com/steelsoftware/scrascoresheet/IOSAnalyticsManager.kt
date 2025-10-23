package com.steelsoftware.scrascoresheet

import analytics.bridge.AnalyticsManagerBridge
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class AnalyticsManager actual constructor(apiKey: String, context: Any?) : Analytics {
    private val bridge = AnalyticsManagerBridge(apiKey = apiKey)

    actual override fun logEvent(name: String, properties: Map<String, Any>?) {
        @Suppress("UNCHECKED_CAST")
        bridge.logEvent(name, properties as Map<Any?, *>?)
    }
}