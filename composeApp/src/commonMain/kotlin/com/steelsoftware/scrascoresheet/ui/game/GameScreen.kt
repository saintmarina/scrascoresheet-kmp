package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word


@Composable
fun GameScreen(component: GameComponent, lyricist: Lyricist<Strings>) {
    val strings = LocalLyricist.current
    val state by component.state.subscribeAsState()

    var popoverAnchor by remember { mutableStateOf<Rect?>(null) }
    var inputBoxBounds by remember { mutableStateOf<Rect?>(null) }
    var currentWord by remember {
        mutableStateOf(Word("", emptyList(), 0))
    }
    var selectedLetterTileIndex by remember { mutableStateOf<Int?>(null) }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    popoverAnchor = null
                }
        ) {
            Text("Game in progress")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { component.finishGame() }) {
                Text("Finish Game")
            }
            when (val currentState = state) {
                is GameState.Game -> {
                    ScrabbleInputBox(
                        language = lyricist.languageTag,
                        onInputChanged = {},
                        popoverAnchor = popoverAnchor,
                        setPopoverAnchor = { popoverAnchor = it },
                        setInputBoxBounds = { inputBoxBounds = it },
                        currentWord = currentWord,
                        setCurrentWord = { newWord -> currentWord = newWord },
                        setSelectedLetterTileIndex = { selectedLetterTileIndex = it },
                        calculateScrabbleScore = component::calculateScrabbleScore,
                    )
                    InGameButtonControls(
                        currentGameState = currentState,
                        currentWord = currentWord,
                        onEndTurn = {
                            component.endTurn(currentWord)
                            currentWord = Word("", emptyList(), 0)
                        },
                        onAddWord = {
                            component.addWord(currentWord)
                            currentWord = Word("", emptyList(), 0)
                        },
                        onBingo = { component.toggleBingo() },
                        onUndo = {
                            component.undo()
                            currentWord = Word("", emptyList(), 0)
                        },
                        onEndGame = { component.finishGame() },
                    )

                }
            }
        }
        if (popoverAnchor != null && inputBoxBounds != null) {
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
                                    if (modifiers[index] == type) ModifierType.BLANK else type
                            }

                            val newWord = currentWord.copy(
                                modifiers = modifiers,
                                score = component.calculateScrabbleScore(
                                    word = currentWord.value,
                                    modifiers = modifiers,
                                    language = lyricist.languageTag,
                                )
                            )

                            currentWord = newWord
                        }
                        popoverAnchor = null
                    },
                    isFirstTurn = true,
                )
            }
        }
    }

}