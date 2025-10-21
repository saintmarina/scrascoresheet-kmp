package com.steelsoftware.scrascoresheet

import cocoapods.AmplitudeUnified.AMPBaseEvent
import cocoapods.AmplitudeUnified.AMPConfiguration
import cocoapods.AmplitudeUnified.Amplitude
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class AnalyticsManager actual constructor(apiKey: String, context: Any?) : Analytics {
    private val amplitude: Amplitude

    init {
        val config = AMPConfiguration(apiKey)
        amplitude = Amplitude(configuration = config)
    }

    actual override fun logEvent(name: String, properties: Map<String, Any>?) {
        if (properties != null) {

            val eventProps: MutableMap<Any?, Any?> = mutableMapOf()
            properties.forEach { (key, value) ->
                eventProps[key] = value
            }
            val event = AMPBaseEvent(
                eventType = name,
                eventProperties = eventProps
            )
            amplitude.track(event)
        } else {
            val event = AMPBaseEvent(
                eventType = name,
            )
            amplitude.track(event)
        }
    }
}