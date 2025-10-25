//
//  AnalyticsManagerBridge.swift
//  AnalyticsManagerBridge
//
//  Created by anna.steel on 10/23/25.
//

import Foundation
import AmplitudeSwift

@objc public class AnalyticsManagerBridge: NSObject {
    private let amplitude: Amplitude

    @objc public init(apiKey: String) {
        let config = Configuration(apiKey: apiKey)
        self.amplitude = Amplitude(configuration: config)
        super.init()
    }

    @objc public func logEvent(_ name: String, properties: [String: Any]? = nil) {
        var baseProps: [String: Any] = ["platform": "iOS"]
        if let props = properties {
            for (k, v) in props {
                baseProps[k] = v
            }
        }
        amplitude.track(eventType: name, eventProperties: baseProps)
    }
}
