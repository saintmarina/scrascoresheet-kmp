package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.ui.components.GradientButton

@Composable
fun InGameOverButtonControls(
    currentGameState: GameState.Game,
    currentWord: Word,
    onSubmitLeftovers: (Word) -> Unit,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
) {
    val submitButtonText = if (currentWord.value.isNotEmpty())
        strings.submitLeftovers.uppercase()
    else
        strings.submitNoLeftovers.uppercase()
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
            if (!currentGameState.game.areLeftOversSubmitted()) {
                // SUBMIT LEFTOVERS / NO LEFTOVERS
                GradientButton(
                    onClick = {
                        onSubmitLeftovers(currentWord)
                    },
                    text = submitButtonText,
                    modifier = Modifier.fillMaxWidth(),
                )

                // UNDO
                GradientButton(
                    onClick = onUndo,
                    text = strings.undo.uppercase(),
                    enabled = currentGameState.gameHistory.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                GradientButton(
                    onClick = { onNewGame() },
                    text = strings.newGame.uppercase(),
                    modifier = Modifier.fillMaxWidth()
                )

                GradientButton(
                    onClick = {
                        // TODO: Open App Store link
                        // openUrl("https://apps.apple.com/us/app/scrabble-score-calculator/id1497216063")
                    },
                    text = "⭐ ${strings.rateUsOnAppStore.uppercase()} ⭐",
                    modifier = Modifier.fillMaxWidth()
                )

                GradientButton(
                    onClick = {
                        // TODO: Open Play Store link
                        //openUrl("https://play.google.com/store/apps/details?id=com.saintmarina.scrabblescore")
                    },
                    text = "⭐ ${strings.rateUsOnPlayStore.uppercase()} ⭐",
                    modifier = Modifier.fillMaxWidth()
                )

                GradientButton(
                    onClick = onUndo,
                    text = strings.undo.uppercase(),
                    enabled = currentGameState.gameHistory.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}