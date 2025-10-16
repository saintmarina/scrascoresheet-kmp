package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import com.steelsoftware.scrascoresheet.i18n.EnglishStrings
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.RussianStrings
import com.steelsoftware.scrascoresheet.i18n.SpanishStrings
import com.steelsoftware.scrascoresheet.ui.components.GradientButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ButtonControls(
    textInInputBox: String,
    isFirstTurn: Boolean,
    onPass: () -> Unit,
    onEndTurn: () -> Unit,
    onAddWord: () -> Unit,
    onBingo: () -> Unit,
    onUndo: () -> Unit,
    onEndGame: () -> Unit,
) {
    val strings = LocalLyricist.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxBoxWidth = if (maxWidth > 550.dp) 550.dp else maxWidth

        val hasText = textInInputBox.isNotEmpty()
        val hasSevenOrMore = textInInputBox.length >= 7
        val isFirstMove = isFirstTurn

        Column(
            modifier = Modifier.width(maxBoxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1 — Pass / End Turn
            GradientButton(
                onClick = if (hasText) onEndTurn else onPass,
                text = if (hasText) strings.endTurn.uppercase() else strings.pass.uppercase(),
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Row 2 — Add Word / Bingo
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GradientButton(
                    onClick = onAddWord,
                    text = strings.addWord.uppercase(),
                    enabled = hasText,
                    modifier = Modifier.weight(1f)
                )

                GradientButton(
                    onClick = onBingo,
                    text = strings.bingo.uppercase(),
                    enabled = hasSevenOrMore,
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3 — Undo / End Game
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GradientButton(
                    onClick = onUndo,
                    text = strings.undo.uppercase(),
                    enabled = !isFirstMove,
                    modifier = Modifier.weight(1f)
                )

                GradientButton(
                    onClick = onEndGame,
                    text = strings.endGame.uppercase(),
                    enabled = !hasText && !isFirstMove,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonControlsPreview_EmptyInput() {
    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    ProvideStrings(lyricist, LocalLyricist) {
        ScrabbleTheme  {
            ButtonControls(
                textInInputBox = "",
                isFirstTurn = true,
                onPass = {},
                onEndTurn = {},
                onAddWord = {},
                onBingo = {},
                onUndo = {},
                onEndGame = {}
            )
        }
    }
}

@Preview
@Composable
fun ButtonControlsPreview_WithLetters() {
    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    ProvideStrings(lyricist, LocalLyricist) {
        ScrabbleTheme {
            ButtonControls(
                textInInputBox = "HELLO",
                isFirstTurn = false,
                onPass = {},
                onEndTurn = {},
                onAddWord = {},
                onBingo = {},
                onUndo = {},
                onEndGame = {}
            )
        }
    }
}

@Preview
@Composable
fun ButtonControlsPreview_BingoReady() {
    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    ProvideStrings(lyricist, LocalLyricist) {
        ScrabbleTheme {
            ButtonControls(
                textInInputBox = "SCRABBLE",
                isFirstTurn = false,
                onPass = {},
                onEndTurn = {},
                onAddWord = {},
                onBingo = {},
                onUndo = {},
                onEndGame = {}
            )
        }
    }
}