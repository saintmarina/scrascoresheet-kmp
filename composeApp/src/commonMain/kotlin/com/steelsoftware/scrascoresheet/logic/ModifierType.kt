package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable

@Serializable
enum class ModifierType {
    DOUBLE_LETTER,
    TRIPLE_LETTER,
    DOUBLE_WORD,
    TRIPLE_WORD,
    BLANK,
    NONE,
}