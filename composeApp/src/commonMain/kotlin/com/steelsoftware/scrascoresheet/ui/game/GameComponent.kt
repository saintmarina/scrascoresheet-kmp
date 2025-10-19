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
    private val _state = MutableValue<GameState>(Game(game, emptyList()))
    val state: Value<GameState> = _state

    fun saveGame() {
        // TODO: Implement game saving logic here
    }

    fun finishGame() = onGameFinished()

    fun calculateScrabbleScore(
        word: String,
        modifiers: List<ModifierType>,
        language: String
    ): Int {
        val scores = scoreListsMap[language] ?: return 0

        var result = 0
        var wordMultiplier = 1

        word.forEachIndexed { i, letter ->
            val modifier = modifiers.getOrNull(i) ?: ModifierType.NONE
            var score = scores[letter.lowercaseChar()] ?: 0

            when (modifier) {
                ModifierType.BLANK -> {
                    score = 0
                }

                ModifierType.DOUBLE_LETTER -> score *= 2
                ModifierType.TRIPLE_LETTER -> score *= 3
                else -> Unit
            }

            result += score

            when (modifier) {
                ModifierType.DOUBLE_WORD -> wordMultiplier *= 2
                ModifierType.TRIPLE_WORD -> wordMultiplier *= 3
                else -> Unit
            }
        }

        return result * wordMultiplier
    }

    /**
     * Ends the current player's turn and advances the game to the next player.
     * Also used to PASS turn.
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

    /**
     * Calls [Game.addWord] and saves the new GameState
     */
    fun addWord(currentWord: Word) {
        val currentState = _state.value
        if (currentState !is Game) return
        val game = currentState.game

        // Add a new word to current turn
        val newGame = game.addWord(currentWord)
        _state.value = Game(newGame, currentState.gameHistory + game)
    }

    /**
     * Toggles the bingo flag for the current player's turn.
     *
     * If bingo is enabled, a 50-point bonus will later apply
     * to this turn’s total score. The updated game state is
     * stored along with the previous snapshot for undo support.
     */
    fun toggleBingo() {
        val currentState = _state.value
        if (currentState !is Game) return
        val game = currentState.game

        val isBingo = !game.getCurrentTurn().bingo
        val newGame = game.setBingo(isBingo)
        _state.value = Game(newGame, currentState.gameHistory + game)
    }

    /**
     * Reverts the game state to the previous turn.
     *
     * If a previous state exists in [gameHistory], it restores the most recent
     * game snapshot and removes it from history. Has no effect if there is no
     * previous game recorded.
     */
    fun undo() {
        val currentState = _state.value
        if (currentState !is Game) return

        if (currentState.gameHistory.isNotEmpty()) {
            val previousHistory = currentState.gameHistory.dropLast(1)
            val previousGame = currentState.gameHistory.last()
            _state.value = Game(previousGame, previousHistory)
        }
    }
}