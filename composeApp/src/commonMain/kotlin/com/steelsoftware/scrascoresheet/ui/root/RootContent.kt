package com.steelsoftware.scrascoresheet.ui.root

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.game.GameScreen
import com.steelsoftware.scrascoresheet.ui.finished.FinishedScreen
import com.steelsoftware.scrascoresheet.ui.splash.SplashScreen
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeScreen

@Composable
fun RootContent(root: RootComponent, lyricist: Lyricist<Strings>) {
    Children(
        stack = root.childStack,
        animation = stackAnimation(fade()),
        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 64.dp, bottom = 16.dp)
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Splash -> SplashScreen(child.component)
            is RootComponent.Child.Welcome -> WelcomeScreen(child.component, lyricist)
            is RootComponent.Child.Game -> GameScreen(child.component, lyricist)
            is RootComponent.Child.Finished -> FinishedScreen(child.component)
        }
    }
}