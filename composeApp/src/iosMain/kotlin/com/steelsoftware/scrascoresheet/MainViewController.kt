package com.steelsoftware.scrascoresheet

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.steelsoftware.scrascoresheet.i18n.EnglishStrings
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.RussianStrings
import com.steelsoftware.scrascoresheet.i18n.SpanishStrings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent

fun MainViewController() = ComposeUIViewController {
    val lifecycle = ApplicationLifecycle()
    val storage = IOSGameStorage()
    val analytics = AnalyticsManager(apiKey = AppConfig.AMPLITUDE_API_KEY)
    val root = RootComponent(
        componentContext = DefaultComponentContext(lifecycle),
        gameStorage = storage,
        analytics = analytics,
    )

    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    val urlOpener = IOSUrlOpener()

    ProvideStrings(lyricist, LocalLyricist) {
        App(root, lyricist, urlOpener)
    }
}