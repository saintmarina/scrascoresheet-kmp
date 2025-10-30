//
//  GoogleAdsBridge.swift
//  GoogleAdsBridge
//
//  Created by anna.steel on 10/29/25.
//

import Foundation
import UIKit
import GoogleMobileAds

@objcMembers
public class BannerAdView: UIView, BannerViewDelegate {
    private var bannerView: BannerView!

    @objc public init(adUnitId: String) {
        super.init(frame: .zero)
        setupBanner(adUnitId: adUnitId)
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }

    private func setupBanner(adUnitId: String) {
        bannerView = BannerView()
        bannerView.adUnitID = adUnitId
        bannerView.delegate = self
        bannerView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(bannerView)

        NSLayoutConstraint.activate([
            bannerView.centerXAnchor.constraint(equalTo: centerXAnchor),
            bannerView.bottomAnchor.constraint(equalTo: safeAreaLayoutGuide.bottomAnchor)
        ])
    }

    // 💡 Called after UIKit attaches the view to the window
    public override func didMoveToWindow() {
        super.didMoveToWindow()
        if bannerView.rootViewController == nil,
           let windowScene = window?.windowScene,
           let rootVC = windowScene.windows.first(where: \.isKeyWindow)?.rootViewController {
            bannerView.rootViewController = rootVC
            let width = UIScreen.main.bounds.width
            bannerView.adSize = currentOrientationAnchoredAdaptiveBanner(width: width)
            bannerView.load(Request())
            print("✅ BannerAdView attached and ad requested.")
        }
    }

    // Optional debugging
    public func bannerViewDidReceiveAd(_ bannerView: BannerView) {
        print("✅ Ad loaded successfully!")
    }

    public func bannerView(_ bannerView: BannerView, didFailToReceiveAdWithError error: any Error) {
        print("❌ Failed to load ad:", error)
    }
}
