package com.steelsoftware.scrascoresheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
actual fun BannerAd(
    adUnitId: String,
    height: Dp,
) {
    val context = LocalContext.current
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val adWidthDp = remember(windowInfo.containerSize) {
        (windowInfo.containerSize.width / density.density).toInt()
    }

    AndroidView(
        factory = {
            AdView(context).apply {
                val adaptive =
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidthDp)
                setAdSize(adaptive)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            val adaptive =
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidthDp)
            if (adView.adSize != adaptive) {
                adView.setAdSize(adaptive)
                adView.loadAd(AdRequest.Builder().build())
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(height)
    )
}