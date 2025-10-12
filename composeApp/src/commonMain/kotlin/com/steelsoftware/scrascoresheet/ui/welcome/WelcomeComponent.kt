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
    private val _state = MutableValue<State>(State.Loading)
    val state: Value<State> = _state

    private var savedGame: Game? = null

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    init {
        doOnResume {
            scope.launch {
                savedGame = gameRepository.load()
                _state.update {
                    if (savedGame != null) State.ResumeGame else State.NewGame(
                        playerNames = emptyList(),
                    )
                }
            }
        }
    }

    fun startGame(playerNames: List<String>) {
        scope.launch {
            val newGame = Game.createNewGame(playerNames)
            gameRepository.save(newGame)
            onStartGame(newGame)
        }
    }

    fun restartGame() {
        val currentState = _state.value
        if (currentState is State.ResumeGame && savedGame != null) {
            _state.update {
                State.NewGame(
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
