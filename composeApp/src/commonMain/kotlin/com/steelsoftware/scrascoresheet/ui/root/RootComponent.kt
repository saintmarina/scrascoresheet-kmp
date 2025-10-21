package com.steelsoftware.scrascoresheet.ui.root


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.steelsoftware.scrascoresheet.AnalyticsManager
import com.steelsoftware.scrascoresheet.repository.GameRepository
import com.steelsoftware.scrascoresheet.storage.GameStorage
import com.steelsoftware.scrascoresheet.ui.game.GameComponent
import com.steelsoftware.scrascoresheet.ui.splash.SplashComponent
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.steelsoftware.scrascoresheet.logic.Game as GameObj

class RootComponent(
    componentContext: ComponentContext,
    gameStorage: GameStorage,
    private val analytics: AnalyticsManager,
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
            //delay(2000)
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
                    onStartGame = { gameObj ->
                        navigation.push(Config.Game(gameObj))
                    },
                    analytics = analytics,
                )
            )

            is Config.Game -> Child.Game(
                GameComponent(
                    componentContext = ctx,
                    gameRepository = gameRepository,
                    game = config.game,
                    onStartNewGame = { navigation.replaceAll(Config.Welcome) },
                    analytics = analytics,
                )
            )
        }

    private sealed class Config {
        data object Splash : Config()
        data object Welcome : Config()
        data class Game(val game: GameObj) : Config()
    }

    sealed class Child {
        data class Splash(val component: SplashComponent) : Child()
        data class Welcome(val component: WelcomeComponent) : Child()
        data class Game(val component: GameComponent) : Child()
    }
}