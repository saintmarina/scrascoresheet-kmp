package com.steelsoftware.scrascoresheet.i18n

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLyricist: ProvidableCompositionLocal<Strings> = staticCompositionLocalOf{
    error("No Lyricist provided")
}
