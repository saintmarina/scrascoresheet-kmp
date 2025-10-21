package com.steelsoftware.scrascoresheet

interface Analytics {
    fun logEvent(name: String, properties: Map<String, Any>? = null)
}

expect class AnalyticsManager(apiKey: String, context: Any? = null) : Analytics {
    override fun logEvent(
        name: String,
        properties: Map<String, Any>?
    )
}