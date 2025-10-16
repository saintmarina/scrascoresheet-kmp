package com.steelsoftware.scrascoresheet

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.Lyricist
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent
import com.steelsoftware.scrascoresheet.ui.root.RootContent

@Composable
fun App(root: RootComponent, lyricist: Lyricist<Strings>) {
    ScrabbleTheme {
        RootContent(root, lyricist)
    }
}