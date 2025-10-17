package com.steelsoftware.scrascoresheet.ui.game

import com.steelsoftware.scrascoresheet.logic.Game as GameObj

sealed class GameState {
    data class Game(val game: GameObj, val gameHistory: List<GameObj>) : GameState()
}
