package com.steelsoftware.scrascoresheet.i18n

import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.Lyricist

val LocalLyricist = staticCompositionLocalOf<Lyricist<Strings>> {
    error("No Lyricist provided")
}
