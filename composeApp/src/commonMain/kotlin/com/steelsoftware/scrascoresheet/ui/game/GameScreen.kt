package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.GLOBAL_SIDE_PADDING
import com.steelsoftware.scrascoresheet.ScrabbleStrings
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import com.steelsoftware.scrascoresheet.UrlOpener
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.ui.components.GradientButton
import org.jetbrains.compose.resources.painterResource
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.logo

@Composable
fun GameScreen(component: GameComponent, urlOpener: UrlOpener) {
    val state by component.state.subscribeAsState()

    var popoverAnchor by remember { mutableStateOf<Rect?>(null) }
    var inputBoxBounds by remember { mutableStateOf<Rect?>(null) }
    var currentWord by remember {
        mutableStateOf(Word("", emptyList(), 0))
    }

    fun resetCurrentWord() {
        currentWord = Word("", emptyList(), 0)
    }

    var selectedLetterTileIndex by remember { mutableStateOf<Int?>(null) }

    var shouldShowPopoverInstruction by remember { mutableStateOf(false) }

    var isInLeftoverMode by remember { mutableStateOf(false) }
    var shouldShowStartNewGameDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        if (shouldShowStartNewGameDialog) {
            ConfirmNewGamePopup(
                show = true,
                onConfirm = { component.startNewGame() },
                onDismiss = { shouldShowStartNewGameDialog = false }
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    popoverAnchor = null
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(GLOBAL_SIDE_PADDING.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = strings.logoDescription,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(bottom = GLOBAL_SIDE_PADDING.dp)
                    .clickable { shouldShowStartNewGameDialog = true },
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
            when (val currentState = state) {
                is GameState.Loading -> {}
                is GameState.Game -> {
                    isInLeftoverMode = currentState.game.leftOversTurnNumber != null
                    shouldShowPopoverInstruction =
                        currentState.game.getCurrentTurnNumber() == 0 && currentState.game.currentPlayerIndex == 0
                    ScoreGrid(game = currentState.game)
                    if (!currentState.game.areLeftOversSubmitted()) {
                        ScrabbleInputBox(
                            language = ScrabbleStrings.language,
                            onInputChanged = {},
                            popoverAnchor = popoverAnchor,
                            setPopoverAnchor = { popoverAnchor = it },
                            setInputBoxBounds = { inputBoxBounds = it },
                            currentWord = currentWord,
                            setCurrentWord = { newWord -> currentWord = newWord },
                            setSelectedLetterTileIndex = { selectedLetterTileIndex = it },
                            calculateScrabbleScore = component::calculateScrabbleScore,
                            inLeftoversMode = currentState.game.leftOversTurnNumber != null
                        )
                    } else {
                        val turnBeforeLeftOvers = (currentState.game.leftOversTurnNumber ?: 0)
                        val winners = currentState.game.getWinners()
                        val points = strings.points
                        if (winners.size > 1) {
                            Text(
                                text = strings.thisIsATieBetween,
                                style = MaterialTheme.typography.displayLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                            winners.forEach {
                                val score = currentState.game.getTotalScore(
                                    it,
                                    turnBeforeLeftOvers
                                )
                                val points = strings.points
                                Text(
                                    text = "${currentState.game.playerNames[it]} - $score $points",
                                    style = MaterialTheme.typography.displayLarge,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        } else {
                            val finalScore = currentState.game.getTotalScore(winners[0])
                            val wonWith = strings.wonWith
                            Text(
                                text = "${currentState.game.playerNames[winners[0]]} $wonWith $finalScore $points!",
                                style = MaterialTheme.typography.displayLarge,
                                modifier = Modifier.fillMaxWidth(0.65f),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    if (!currentState.game.isGameOver) {
                        InGameButtonControls(
                            currentGameState = currentState,
                            currentWord = currentWord,
                            onEndTurn = {
                                component.endTurn(currentWord)
                                resetCurrentWord()
                            },
                            onAddWord = {
                                component.addWord(currentWord)
                                resetCurrentWord()
                            },
                            onBingo = { component.toggleBingo() },
                            onUndo = {
                                component.undo()
                                resetCurrentWord()
                            },
                            onEndGame = component::endGame,
                        )
                    } else {
                        InGameOverButtonControls(
                            currentGameState = currentState,
                            currentWord = currentWord,
                            onSubmitLeftovers = { word ->
                                component.submitLeftovers(word)
                                resetCurrentWord()
                            },
                            onUndo = {
                                component.undo()
                                resetCurrentWord()
                            },
                            onNewGame = { component.startNewGame() },
                            urlOpener = urlOpener,
                        )
                    }
                }
            }
            Instructions()
            Spacer(Modifier.height(16.dp))
        }
        if (popoverAnchor != null && inputBoxBounds != null && !isInLeftoverMode) {
            Box(
                Modifier
                    .clickable(
                        // Click anywhere outside = dismiss
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        popoverAnchor = null
                    }
            ) {
                ModifierPopover(
                    inputBoxBounds,
                    tileBounds = popoverAnchor,
                    onSelect = { type ->
                        selectedLetterTileIndex?.let { index ->
                            val modifiers = currentWord.modifiers.toMutableList()

                            if (index in modifiers.indices) {
                                modifiers[index] =
                                    if (modifiers[index] == type) ModifierType.NONE else type
                            }

                            val newWord = currentWord.copy(
                                modifiers = modifiers,
                                score = component.calculateScrabbleScore(
                                    word = currentWord.value,
                                    modifiers = modifiers,
                                    language = ScrabbleStrings.language,
                                )
                            )

                            currentWord = newWord
                        }
                        popoverAnchor = null
                    },
                    shouldShowPopoverInstructions = shouldShowPopoverInstruction,
                )
            }
        }
    }
}

@Composable
fun Instructions() {
    Text(
        text = strings.instructionTitleGameScreen,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
    )
    Text(
        text = strings.instructionsTextGameScreen,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Justify,
    )
}


@Composable
fun ConfirmNewGamePopup(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val brush = Brush.radialGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = 1000f
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    ScrabbleTheme.colors.brightRed.copy(alpha = 0.95f),
                    ScrabbleTheme.colors.deepRed.copy(alpha = 0.95f)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = strings.areYouSureYouWantToStartANewGame,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = strings.currentProgressWillBeLost,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GradientButton(
                            onClick = onDismiss,
                            text = strings.noButton.uppercase(),
                            modifier = Modifier.alpha(0.7f)
                        )
                        GradientButton(
                            onClick = {
                                onConfirm()
                                onDismiss()
                            },
                            text = strings.yesButton.uppercase()
                        )
                    }
                }
            }
        }
    }
}