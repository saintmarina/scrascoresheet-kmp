package com.steelsoftware.scrascoresheet.game

import com.arkivanov.decompose.ComponentContext

class GameComponent(
    componentContext: ComponentContext,
    private val onGameFinished: () -> Unit
) : ComponentContext by componentContext {

    fun finishGame() = onGameFinished()
}