//
//  AnalyticsManagerBridge.swift
//  AnalyticsManagerBridge
//
//  Created by anna.steel on 10/23/25.
//

import Foundation
import AmplitudeUnified

@objc public class AnalyticsManagerBridge: NSObject {
    private let amplitude: Amplitude

    @objc public init(apiKey: String) {
        // Initialize Amplitude configuration
        let config = Configuration(apiKey: apiKey)
        self.amplitude = Amplitude(configuration: config)
        super.init()
    }

    /// Logs an event with optional properties.
    @objc public func logEvent(_ name: String, properties: [String: Any]? = nil) {
        // Add base properties
        var baseProps: [String: Any] = ["platform": "iOS"]

        if let additionalProps = properties {
            for (key, value) in additionalProps {
                baseProps[key] = value
            }
        }

        amplitude.track(eventType: name, eventProperties: baseProps)
    }
}
