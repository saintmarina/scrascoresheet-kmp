package com.steelsoftware.scrascoresheet

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
actual fun BannerAd(
    adUnitId: String,
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    AndroidView(
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            if (adView.adUnitId != adUnitId) {
                adView.adUnitId = adUnitId
                adView.loadAd(AdRequest.Builder().build())
            }
        }
    )
}