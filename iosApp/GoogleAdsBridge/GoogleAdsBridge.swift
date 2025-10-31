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
    private var heightConstraint: NSLayoutConstraint?

    @objc public init(adUnitId: String) {
        super.init(frame: .zero)
        translatesAutoresizingMaskIntoConstraints = false
        setupBanner(adUnitId: adUnitId)
    }

    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }

    private func setupBanner(adUnitId: String) {
        bannerView = BannerView()
        bannerView.adUnitID = adUnitId
        bannerView.delegate = self
        bannerView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(bannerView)

        NSLayoutConstraint.activate([
            bannerView.centerXAnchor.constraint(equalTo: centerXAnchor),
            bannerView.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
    }


    public override func layoutSubviews() {
        super.layoutSubviews()

        let width = bounds.width
        guard width > 0 else { return }

        if bannerView.rootViewController == nil,
           let rootVC = window?.windowScene?.windows.first(where: \.isKeyWindow)?.rootViewController {
            bannerView.rootViewController = rootVC
        }

        let adSize = currentOrientationAnchoredAdaptiveBanner(width: width)
        bannerView.adSize = adSize

        heightConstraint?.isActive = false
        heightConstraint = heightAnchor.constraint(equalToConstant: adSize.size.height)
        heightConstraint?.isActive = true

        bannerView.load(Request())
    }
}
