package com.steelsoftware.scrascoresheet

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration

actual class AnalyticsManager actual constructor(apiKey: String, context: Any?) : Analytics {

    private val amplitude: Amplitude

    init {
        val androidContext = context as? Context
            ?: error("Context is required to initialize AnalyticsManager on Android")
        amplitude = Amplitude(Configuration(apiKey, androidContext))
    }


    actual override fun logEvent(name: String, properties: Map<String, Any>?) {
        amplitude.track(name, properties)
    }
}