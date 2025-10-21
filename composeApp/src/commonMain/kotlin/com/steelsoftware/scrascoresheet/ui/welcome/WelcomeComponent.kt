package com.steelsoftware.scrascoresheet.ui.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.steelsoftware.scrascoresheet.logic.Game
import com.steelsoftware.scrascoresheet.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WelcomeComponent(
    componentContext: ComponentContext,
    private val gameRepository: GameRepository,
    private val onStartGame: (Game) -> Unit,
) : ComponentContext by componentContext {
    private val _state = MutableValue<WelcomeState>(WelcomeState.Loading)
    val state: Value<WelcomeState> = _state

    private var savedGame: Game? = null

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        doOnResume {
            scope.launch {
                savedGame = gameRepository.loadGame()
                _state.update {
                    if (savedGame != null) WelcomeState.ResumeGame else WelcomeState.NewGame(
                        playerNames = emptyList(),
                    )
                }
            }
        }
    }

    fun startGame(playerNames: List<String>) {
        scope.launch {
            val newGame = Game.createNewGame(playerNames)
            gameRepository.saveGame(newGame)
            onStartGame(newGame)
        }
    }

    fun restartGame() {
        val currentState = _state.value
        if (currentState is WelcomeState.ResumeGame && savedGame != null) {
            _state.update {
                WelcomeState.NewGame(
                    playerNames = savedGame!!.playerNames
                )
            }
        }
    }

    fun resumeGame() {
        val game = savedGame ?: return
        onStartGame(game)
    }
}
