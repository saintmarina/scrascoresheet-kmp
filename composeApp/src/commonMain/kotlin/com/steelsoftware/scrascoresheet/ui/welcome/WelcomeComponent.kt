package com.steelsoftware.scrascoresheet.ui.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.steelsoftware.scrascoresheet.logic.Game
import com.steelsoftware.scrascoresheet.repository.GameRepository
import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WelcomeComponent(
    componentContext: ComponentContext,
    private val gameStorage: GameStorage,
    private val onStartGame: (Game) -> Unit,
) : ComponentContext by componentContext {
    private val _state = MutableValue<State>(State.Loading)
    val state: Value<State> = _state

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    private val repository = GameRepository(gameStorage)
    init {
        scope.launch {
            val gameExists = repository.load() != null
            _state.update {
                if (gameExists) State.ResumeGame else State.NewGame(
                    playerNames = emptyList(),
                )
            }
        }
    }

    fun startGame() {
        val newGame = Game.createNewGame(2) // TODO: make dynamic
        onStartGame(newGame)
    }
}
