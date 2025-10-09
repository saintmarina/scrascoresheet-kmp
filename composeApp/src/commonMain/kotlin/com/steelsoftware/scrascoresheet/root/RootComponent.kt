package com.steelsoftware.scrascoresheet.root


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.steelsoftware.scrascoresheet.welcome.WelcomeComponent
import com.steelsoftware.scrascoresheet.game.GameComponent
import com.steelsoftware.scrascoresheet.finished.FinishedComponent

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Welcome, // TODO: determineInitialConfig() based on the saved game state
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = null,
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(config: Config, ctx: ComponentContext): Child =
        when (config) {
            Config.Welcome -> Child.Welcome(
                WelcomeComponent(
                    componentContext = ctx,
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
        data object Welcome : Config()

        data object Game : Config()

        data object Finished : Config()
    }

    sealed class Child {
        data class Welcome(val component: WelcomeComponent) : Child()
        data class Game(val component: GameComponent) : Child()
        data class Finished(val component: FinishedComponent) : Child()
    }
}