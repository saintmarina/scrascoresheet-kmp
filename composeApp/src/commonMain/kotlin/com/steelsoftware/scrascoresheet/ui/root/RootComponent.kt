package com.steelsoftware.scrascoresheet.ui.root


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeComponent
import com.steelsoftware.scrascoresheet.ui.game.GameComponent
import com.steelsoftware.scrascoresheet.ui.finished.FinishedComponent
import com.steelsoftware.scrascoresheet.ui.splash.SplashComponent
import com.steelsoftware.scrascoresheet.storage.GameStorage

class RootComponent(
    componentContext: ComponentContext,
    private val gameStorage: GameStorage,
) : ComponentContext by componentContext {
    // Initialize coroutine scope with essenty lifecycle coroutine scope

    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Splash,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = null,
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(config: Config, ctx: ComponentContext): Child =
        when (config) {
            Config.Splash -> Child.Splash(SplashComponent(ctx))
            Config.Welcome -> Child.Welcome(
                WelcomeComponent(
                    componentContext = ctx,
                    gameStorage = gameStorage,
                    onStartGame = { navigation.push(Config.Game) }
                )
            )
            Config.Game -> Child.Game(
                GameComponent(
                    componentContext = ctx,
                    onGameFinished = { navigation.push(Config.Finished) }
                )
            )
            Config.Finished -> Child.Finished(
                FinishedComponent(
                    componentContext = ctx,
                    onRestart = { navigation.replaceAll(Config.Welcome) }
                )
            )
        }

    private sealed class Config {
        data object Splash : Config()
        data object Welcome : Config()

        data object Game : Config()

        data object Finished : Config()
    }

    sealed class Child {
        data class Splash(val component: SplashComponent) : Child()
        data class Welcome(val component: WelcomeComponent) : Child()
        data class Game(val component: GameComponent) : Child()
        data class Finished(val component: FinishedComponent) : Child()
    }
}