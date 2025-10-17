package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable

@Serializable
data class Turn(
    val words: List<Word> = emptyList(),
    val bingo: Boolean = false
) {
    companion object {
        fun empty() = Turn()
        fun fromPlain(words: List<Word>, bingo: Boolean) = Turn(words, bingo)
    }

    fun isEmpty(): Boolean = words.isEmpty()

    fun isPassed(game: Game): Boolean =
        isEmpty() && this !== game.getCurrentTurn()

    fun isComplete(game: Game): Boolean =
        this !== game.getCurrentTurn()

    val score: Int
        get() {
            var result = words.sumOf { it.score }
            if (bingo) result += 50
            return result
        }
}