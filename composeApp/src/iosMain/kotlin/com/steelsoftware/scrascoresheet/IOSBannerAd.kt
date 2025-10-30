package com.steelsoftware.scrascoresheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import googleadsbridge.BannerAdView
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BannerAd(adUnitId: String) {
    UIKitView<BannerAdView>(
        factory = { BannerAdView(adUnitId = adUnitId) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}