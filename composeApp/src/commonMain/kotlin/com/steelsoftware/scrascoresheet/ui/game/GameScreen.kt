package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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


@Composable
fun GameScreen(component: GameComponent, lyricist: Lyricist<Strings>) {
    val strings = LocalLyricist.current
    val state by component.state.subscribeAsState()

    var popoverAnchor by remember { mutableStateOf<Rect?>(null) }
    var text by remember { mutableStateOf("") }

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
                    onModifierApplied = { _, _ -> },
                    popoverAnchor = popoverAnchor,
                    setPopoverAnchor = { popoverAnchor = it },
                    text = text,
                    setText = { newText -> text = newText }

                )
                ButtonControls(
                    textInInputBox = text,
                    isFirstTurn = true,
                    onPass = { component.passTurn() },
                    onEndTurn = { component.endTurn() },
                    onAddWord = { component.addWord() },
                    onBingo = { component.toggleBingo() },
                    onUndo = { component.undo() },
                    onEndGame = { component.finishGame() },
                )
            }
        }
    }
}