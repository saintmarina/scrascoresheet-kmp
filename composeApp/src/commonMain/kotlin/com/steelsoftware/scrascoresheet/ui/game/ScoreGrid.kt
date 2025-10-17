package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.logic.Game
import com.steelsoftware.scrascoresheet.logic.Turn
import com.steelsoftware.scrascoresheet.logic.Word
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScoreGrid(
    game: Game,
) {
    val strings = LocalLyricist.current
    val playerNames = game.playerNames
    val numRows = if (game.isGameOver)
        (game.leftOversTurnNumber ?: 0) + 1
    else
        game.getCurrentTurnNumber() + 1

    val totals = playerNames.indices.map { game.getRunningTotals(it) }
    var headerWidth by remember { mutableStateOf(80.dp) }
    val density = LocalDensity.current

    val borderColor = ScrabbleTheme.colors.offWhite
    val strokeWidth = with(density) { 1.dp.toPx() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScrabbleTheme.colors.deepRed40)
            .drawBehind {
                // Outer border
                drawRect(
                    color = borderColor,
                    style = Stroke(width = strokeWidth)
                )
            }
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ScrabbleTheme.colors.deepRed30)
                .height(IntrinsicSize.Min)
        ) {
            Box(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = strings.gridHeaderNames,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(8.dp)
                        .onGloballyPositioned { coordinates ->
                            headerWidth = with(density) { coordinates.size.width.toDp() + 16.dp }
                        }
                )
            }
            TableDivider()
            TableCell(strings.gridHeaderPlayerTurn, Modifier.weight(1f), bold = true)
        }


        // Moves and player turns
        repeat(numRows) { moveIndex ->
            val text = if (game.isMoveInGameOver(moveIndex))
                strings.gridLeftoverAccounting
            else
                "${strings.gridMove} ${moveIndex + 1}"
            // Move header
            MoveNumberCell(text = text, isFirstTurn = moveIndex == 0)

            // Player rows per move
            game.playersTurns.forEachIndexed { playerIndex, turns ->
                val turn = turns.getOrNull(moveIndex)
                if (turn != null) {
                    val totalScore = totals[playerIndex].getOrNull(moveIndex) ?: 0
                    val turnWords = if (turn.words.isEmpty()) strings.gridSubmitAWordOrPass
                    else turn.words.joinToString("+") { it.value } // TODO: add scrabble tiles

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                // Bottom line only (prevent overlap)
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = borderColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            }
                            .height(IntrinsicSize.Min)
                    ) {
                        TableCell(
                            "${playerNames[playerIndex]}\n($totalScore)",
                            modifier = Modifier.width(headerWidth + 16.dp)
                        )
                        TableDivider()
                        TableCell(turnWords, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun MoveNumberCell(
    text: String,
    isFirstTurn: Boolean = false,
) {
    val density = LocalDensity.current
    val borderColor = ScrabbleTheme.colors.offWhite
    val strokeWidth = with(density) { 1.dp.toPx() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ScrabbleTheme.colors.deepRed30)
            //.border(0.5.dp, ScrabbleTheme.colors.whiteBorder)
            .drawBehind {
                if (isFirstTurn) {
                    // Top line (only for first turn)
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }

                // Bottom line (always)
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier.padding(8.dp),
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TableDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(ScrabbleTheme.colors.offWhite)
    )
}

@Preview(showBackground = true)
@Composable
fun ScoreGridLazyPreview() {
    val game = Game(
        playerNames = listOf("Anna", "Polina", "Olesya"),
        playersTurns = listOf(
            listOf(
                Turn(words = listOf(Word("CAT", emptyList(), 5))),
                Turn(words = listOf(Word("DOG", emptyList(), 8)))
            ),
            listOf(
                Turn(words = listOf(Word("HELLO", emptyList(), 10))),
                Turn(words = listOf(Word("WORLD", emptyList(), 7)))
            ),
            listOf(
                Turn(words = listOf(Word("QUIZ", emptyList(), 22))),
                Turn(words = listOf(Word("JAZZ", emptyList(), 24)))
            )
        ),
        currentPlayerIndex = 0,
        leftOversTurnNumber = null
    )


    MaterialTheme {
        ScoreGrid(game = game)
    }
}