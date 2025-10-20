package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.GLOBAL_SIDE_PADDING
import com.steelsoftware.scrascoresheet.ScrabbleStrings
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.UrlOpener
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word
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

    Box(Modifier.fillMaxSize()) {
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
                modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = GLOBAL_SIDE_PADDING.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
            when (val currentState = state) {
                is GameState.Game -> {
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
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            winners.forEach {
                                val score = currentState.game.getTotalScore(
                                    it,
                                    turnBeforeLeftOvers
                                )
                                val points = strings.points
                                Text(
                                    text = "${currentState.game.playerNames[it]} - $score $points",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        } else {
                            val finalScore = currentState.game.getTotalScore(winners[0])
                            val wonWith = strings.wonWith
                            Text(
                                text = "${currentState.game.playerNames[winners[0]]} $wonWith $finalScore $points!",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
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
        if (popoverAnchor != null && inputBoxBounds != null) { // TODO: And not in the leftover mode
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