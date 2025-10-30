package com.steelsoftware.scrascoresheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun BannerAd(
    adUnitId: String,
    height: Dp,
)