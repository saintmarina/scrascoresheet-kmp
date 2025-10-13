package com.steelsoftware.scrascoresheet.ui.game

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Strings


@Composable
fun GameScreen(component: GameComponent, lyricist: Lyricist<Strings>) {
    val strings = LocalLyricist.current
    val state by component.state.subscribeAsState()

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Game in progress")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { component.finishGame() }) {
            Text("Finish Game")
        }
        when (val currentState = state) {
            is GameState.Game -> {
                ScrabbleInputBox(
                    language = lyricist.languageTag,
                    onInputChanged = {}
                )
            }
        }
    }
}