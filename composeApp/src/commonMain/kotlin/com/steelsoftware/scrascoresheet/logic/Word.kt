package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val value: String,
    val modifiers: List<List<ModifierType>> = emptyList(),
    val score: Int = 0
)
