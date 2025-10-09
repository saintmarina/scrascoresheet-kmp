package com.steelsoftware.scrascoresheet.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.steelsoftware.scrascoresheet.game.GameScreen
import com.steelsoftware.scrascoresheet.finished.FinishedScreen
import com.steelsoftware.scrascoresheet.welcome.WelcomeScreen

@Composable
fun RootContent(root: RootComponent) {
    Children(
        stack = root.childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Welcome -> WelcomeScreen(child.component)
            is RootComponent.Child.Game -> GameScreen(child.component)
            is RootComponent.Child.Finished -> FinishedScreen(child.component)
        }
    }
}