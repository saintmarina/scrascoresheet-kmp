package com.steelsoftware.scrascoresheet.ui.welcome

sealed class State {
    object Loading : State()
    object ResumeGame : State()
    data class NewGame(
        val playerNames: List<String>,
    ) : State()
}
