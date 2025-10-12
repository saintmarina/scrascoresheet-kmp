package com.steelsoftware.scrascoresheet.ui.root


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.steelsoftware.scrascoresheet.repository.GameRepository
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeComponent
import com.steelsoftware.scrascoresheet.ui.game.GameComponent
import com.steelsoftware.scrascoresheet.ui.finished.FinishedComponent
import com.steelsoftware.scrascoresheet.ui.splash.SplashComponent
import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RootComponent(
    componentContext: ComponentContext,
    private val gameStorage: GameStorage,
) : ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    private val gameRepository = GameRepository(gameStorage)
    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Splash,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = null,
    )

    init {
        // Wait 2 seconds and then set the next screen to Welcome
        scope.launch {
            delay(2000)
            navigation.replaceAll(Config.Welcome)
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(config: Config, ctx: ComponentContext): Child =
        when (config) {
            Config.Splash -> Child.Splash(SplashComponent(ctx))
            Config.Welcome -> Child.Welcome(
                WelcomeComponent(
                    componentContext = ctx,
                    gameRepository = gameRepository,
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