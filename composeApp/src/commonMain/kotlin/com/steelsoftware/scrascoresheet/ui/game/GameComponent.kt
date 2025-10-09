package com.steelsoftware.scrascoresheet.ui.game

import com.arkivanov.decompose.ComponentContext

class GameComponent(
    componentContext: ComponentContext,
    private val onGameFinished: () -> Unit
) : ComponentContext by componentContext {

    fun saveGame() {
        // Implement game saving logic here

    }
    fun finishGame() = onGameFinished()
}