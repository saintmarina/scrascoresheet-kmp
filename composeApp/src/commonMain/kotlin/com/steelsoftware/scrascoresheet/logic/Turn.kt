package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val value: String,
    val modifiers: List<String> = emptyList(),
    val score: Int = 0
)

@Serializable
data class Turn(
    val words: List<Word> = emptyList(),
    val bingo: Boolean = false
) {
    companion object {
        fun empty() = Turn(emptyList(), false)
        fun fromPlain(obj: Turn) = Turn(obj.words, obj.bingo)
    }

    val isEmpty: Boolean get() = words.isEmpty()

    fun score(): Int {
        var total = words.sumOf { it.score }
        if (bingo) total += 50
        return total
    }
}