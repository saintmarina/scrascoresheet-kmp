package com.steelsoftware.scrascoresheet

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.steelsoftware.scrascoresheet.i18n.EnglishStrings
import com.steelsoftware.scrascoresheet.i18n.SpanishStrings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent

fun MainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()
    val storage = IOSGameStorage()
    val root = RootComponent(
        componentContext = DefaultComponentContext(lifecycle),
        gameStorage = storage,
    )

    val lyricist = Lyricist(
        defaultLanguageTag = "en",
        translations = mapOf(
            "en" to EnglishStrings,
            "es" to SpanishStrings
        )
    )
    App(root, lyricist)
}