package com.steelsoftware.scrascoresheet

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.steelsoftware.scrascoresheet.root.RootComponent

fun MainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()

    // Create RootComponent just like on Android
    val root = RootComponent(DefaultComponentContext(lifecycle))

    // Render your app
    App(root)
}