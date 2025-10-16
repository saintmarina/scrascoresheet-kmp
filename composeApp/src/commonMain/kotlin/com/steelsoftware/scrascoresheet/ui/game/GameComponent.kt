package com.steelsoftware.scrascoresheet.ui.game

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.scoreListsMap
import com.steelsoftware.scrascoresheet.ui.game.GameState.Game
import com.steelsoftware.scrascoresheet.logic.Game as GameObj


class GameComponent(
    componentContext: ComponentContext,
    private val game: GameObj,
    private val onGameFinished: () -> Unit
) : ComponentContext by componentContext {

    private val _state = MutableValue<GameState>(Game(game))
    val state: Value<GameState> = _state

    fun saveGame() {
        // Implement game saving logic here

    }

    fun finishGame() = onGameFinished()

    fun calculateScrabbleScore(
        word: String,
        modifiers: List<List<ModifierType>>,
        language: String
    ): Int {
        val scores = scoreListsMap[language] ?: return 0
        var result = 0

        // 1️⃣ Apply letter-level modifiers (DL, TL, BLANK)
        word.forEachIndexed { i, letter ->
            var score = scores[letter.lowercaseChar()] ?: 0
            val letterModifiers = modifiers.getOrNull(i).orEmpty()

            for (modifier in letterModifiers) {
                when (modifier) {
                    ModifierType.BLANK -> score = 0
                    ModifierType.DOUBLE_LETTER -> score *= 2
                    ModifierType.TRIPLE_LETTER -> score *= 3
                    else -> Unit
                }
            }
            result += score
        }

        // 2️⃣ Apply word-level modifiers (DW, TW)
        modifiers.flatten().forEach { modifier ->
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

    fun endTurn() {
        // TODO: Implement endTurn turn logic
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