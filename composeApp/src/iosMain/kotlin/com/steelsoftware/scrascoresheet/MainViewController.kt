package com.steelsoftware.scrascoresheet

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.steelsoftware.scrascoresheet.ui.root.RootComponent

fun MainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()

    // Create RootComponent with IOSGameStorage
    val storage = IOSGameStorage()
    val root = RootComponent(
        componentContext = DefaultComponentContext(lifecycle),
        gameStorage = storage,
    )

    // Render your app
    App(root)
}