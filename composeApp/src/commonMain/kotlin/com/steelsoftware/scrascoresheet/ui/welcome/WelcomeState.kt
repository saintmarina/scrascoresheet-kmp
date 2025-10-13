package com.steelsoftware.scrascoresheet.ui.welcome

sealed class WelcomeState {
    object Loading : WelcomeState()
    object ResumeGame : WelcomeState()
    data class NewGame(
        val playerNames: List<String>,
    ) : WelcomeState()
}
