package com.steelsoftware.scrascoresheet.ui.welcome

import com.arkivanov.decompose.ComponentContext
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
    private val onStartGame: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    private val repository = GameRepository(gameStorage)
    init {
        scope.launch {
            val gameExists = repository.load() != null
            if (gameExists) {
                // Show option to resume or start new
            } else {
                // No existing game, stay on welcome screen
            }

        }
    }

    fun startGame() {
        val newGame = Game.createNewGame(2) // TODO: make dynamic
        onStartGame()
    }

}