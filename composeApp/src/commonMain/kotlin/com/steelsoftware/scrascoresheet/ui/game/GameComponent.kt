package com.steelsoftware.scrascoresheet.ui.game

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.logic.scoreListsMap
import com.steelsoftware.scrascoresheet.ui.game.GameState.Game
import com.steelsoftware.scrascoresheet.logic.Game as GameObj


class GameComponent(
    componentContext: ComponentContext,
    private val game: GameObj,
    private val onGameFinished: () -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableValue<GameState>(Game(game, listOf(game)))
    val state: Value<GameState> = _state

    fun saveGame() {
        // Implement game saving logic here

    }

    fun finishGame() = onGameFinished()

    fun calculateScrabbleScore(
        word: String,
        modifiers: List<ModifierType>,
        language: String
    ): Int {
        val scores = scoreListsMap[language] ?: return 0
        var result = 0

        word.forEachIndexed { i, letter ->
            var score = scores[letter.lowercaseChar()] ?: 0
            when (modifiers.getOrNull(i)) {
                ModifierType.DOUBLE_LETTER -> score *= 2
                ModifierType.TRIPLE_LETTER -> score *= 3
                else -> Unit
            }
            result += score
        }

        modifiers.forEach { modifier ->
            when (modifier) {
                ModifierType.DOUBLE_WORD -> result *= 2
                ModifierType.TRIPLE_WORD -> result *= 3
                else -> Unit
            }
        }

        return result
    }

    fun passTurn() {
        // TODO: Implement pass turn logic
    }

    /**
     * Ends the current player's turn and advances the game to the next player.
     *
     * If the provided [currentWord] is not empty, it is first added to the
     * current turn using [Game.addWord]. The game state is then advanced
     * by calling [Game.endTurn].
     *
     * The previous [Game] instance is preserved in [GameState.Game.gameHistory]
     * to enable undo functionality.
     *
     * @param currentWord The word currently being entered by the active player.
     */
    fun endTurn(currentWord: Word) {
        val currentState = _state.value
        if (currentState !is Game) return

        var game = currentState.game

        if (currentWord.value.isNotBlank()) {
            game = game.addWord(currentWord)
        }

        val newGame = game.endTurn()

        _state.value = Game(
            game = newGame,
            gameHistory = currentState.gameHistory + currentState.game
        )
    }

    fun addWord() {
        // TODO: Implement addWord logic
    }

    fun toggleBingo() {
        // TODO: Implement toggleBingo logic
    }

    fun undo() {
        // TODO: Implement undo logic
    }
}