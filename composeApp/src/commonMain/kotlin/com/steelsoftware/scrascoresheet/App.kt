package com.steelsoftware.scrascoresheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.lyricist.Lyricist
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent
import com.steelsoftware.scrascoresheet.ui.root.RootContent

@Composable
fun App(root: RootComponent, lyricist: Lyricist<Strings>) {
    MaterialTheme {
        RootContent(root, lyricist)
    }
}