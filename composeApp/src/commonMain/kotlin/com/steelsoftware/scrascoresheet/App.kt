package com.steelsoftware.scrascoresheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent
import com.steelsoftware.scrascoresheet.ui.root.RootContent
import org.jetbrains.compose.resources.painterResource
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.logo

const val GLOBAL_TOP_PADDING = 64
const val GLOBAL_SIDE_PADDING = 16

@Composable
fun App(root: RootComponent, lyricist: Lyricist<Strings>, urlOpener: UrlOpener) {
    ScrabbleTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = GLOBAL_SIDE_PADDING.dp)
                .padding(top = GLOBAL_TOP_PADDING.dp, bottom = GLOBAL_SIDE_PADDING.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = strings.logoDescription,
                modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = GLOBAL_SIDE_PADDING.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
            RootContent(root, lyricist, urlOpener)
        }
    }
}