package com.steelsoftware.scrascoresheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent
import com.steelsoftware.scrascoresheet.ui.root.RootContent

var globalTopPadding = 0
const val GLOBAL_SIDE_PADDING = 16
var bannerHeight = 0

@Composable
fun App(root: RootComponent, lyricist: Lyricist<Strings>, urlOpener: UrlOpener) {
    var shouldShowBanner by remember { mutableStateOf(false) }
    root.childStack.subscribe { stack ->
        shouldShowBanner = stack.active.instance is RootComponent.Child.Game
    }

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidthDp = remember(windowInfo.containerSize) {
        (windowInfo.containerSize.width / density.density).toInt()
    }

    if (screenWidthDp >= 600) {
        globalTopPadding = 20
        bannerHeight = 99
    } else {
        globalTopPadding = 50
        bannerHeight = 68
    }

    ScrabbleTheme {

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = globalTopPadding.dp)
        ) {
            if (shouldShowBanner) {
                BannerAd(
                    adUnitId = AppConfig.TEST_BANNER_ID,  // TODO: replace with production ID
                    height = bannerHeight.dp,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = GLOBAL_SIDE_PADDING.dp)
                    .padding(bottom = GLOBAL_SIDE_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RootContent(root, lyricist, urlOpener)
            }
        }
    }
}