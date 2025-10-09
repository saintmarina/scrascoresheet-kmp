package com.steelsoftware.scrascoresheet.welcome

import com.arkivanov.decompose.ComponentContext

class WelcomeComponent(
    componentContext: ComponentContext,
    private val onStartGame: () -> Unit
) : ComponentContext by componentContext {

    fun startGame() = onStartGame()
}