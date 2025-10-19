package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.ui.components.GradientButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InGameButtonControls(
    currentGameState: GameState.Game,
    currentWord: Word,
    onEndTurn: () -> Unit,
    onAddWord: () -> Unit,
    onBingo: () -> Unit,
    onUndo: () -> Unit,
    onEndGame: () -> Unit,
) {
    val isModifierChosen = currentWord.modifiers.any { it != ModifierType.BLANK }
    val isCurrentWordEmpty =
        currentGameState.game.getCurrentTurn().isEmpty() && currentWord.value.isEmpty()
    val isFirstTurn =
        currentGameState.game.getCurrentTurnNumber() == 0 && currentGameState.game.currentPlayerIndex == 0
    val isEndTurnDisabled = !isModifierChosen && !isCurrentWordEmpty && isFirstTurn

    val totalLettersThisTurn = (currentGameState.game.getCurrentTurn().words + currentWord)
        .sumOf { it.value.length }
    val isBingoDisabled = totalLettersThisTurn < 7

    val isEndGameDisabled =
        currentGameState.game.currentPlayerIndex != 0 ||
                currentWord.value.isNotEmpty() ||
                currentGameState.game.getCurrentTurn().score > 0 ||
                currentGameState.game.playersTurns[currentGameState.game.currentPlayerIndex].size == 1

    val strings = LocalLyricist.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val maxBoxWidth = if (maxWidth > 550.dp) 550.dp else maxWidth

        Column(
            modifier = Modifier.width(maxBoxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val shouldShowInstruction =
                currentGameState.game.getCurrentTurnNumber() == 0 &&
                        currentGameState.game.currentPlayerIndex == 0 &&
                        !isModifierChosen &&
                        currentWord.value.isNotEmpty()

            if (shouldShowInstruction) {
                Text(
                    text = strings.pressOnALetterInstruction,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                )
            }

            // Row 1 — Pass / End Turn
            GradientButton(
                onClick = onEndTurn,
                text = if (isCurrentWordEmpty) strings.pass.uppercase() else strings.endTurn.uppercase(),
                enabled = !isEndTurnDisabled,
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
                    enabled = currentWord.value.isNotEmpty() && !isFirstTurn,
                    modifier = Modifier.weight(1f)
                )

                val bingoButtonText = if (currentGameState.game.getCurrentTurn().bingo) {
                    strings.bingo.uppercase() + " ✅"
                } else {
                    strings.bingo.uppercase()
                }
                GradientButton(
                    onClick = onBingo,
                    text = bingoButtonText,
                    enabled = !isBingoDisabled,
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
                    enabled = currentGameState.gameHistory.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                GradientButton(
                    onClick = onEndGame,
                    text = strings.endGame.uppercase(),
                    enabled = !isEndGameDisabled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonControlsPreview_NewGameState() {
    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )

    val sampleGame = com.steelsoftware.scrascoresheet.logic.Game.createNewGame(
        playerNames = listOf("Alice", "Bob")
    )

    val gameWithProgress = sampleGame.addWord(
        Word(
            value = "HELLO",
            modifiers = listOf(
                ModifierType.NONE,
                ModifierType.DOUBLE_LETTER,
                ModifierType.BLANK,
                ModifierType.BLANK,
                ModifierType.BLANK
            ),
            score = 10
        )
    )

    val gameHistory = listOf(sampleGame)

    val state = GameState.Game(game = gameWithProgress, gameHistory = gameHistory)

    val currentWord = Word(
        value = "WORLD",
        modifiers = List(5) { ModifierType.NONE },
        score = 8
    )

    ProvideStrings(lyricist, LocalLyricist) {
        ScrabbleTheme {
            InGameButtonControls(
                currentGameState = state,
                currentWord = currentWord,
                onEndTurn = {},
                onAddWord = {},
                onBingo = {},
                onUndo = {},
                onEndGame = {},
            )
        }
    }
}