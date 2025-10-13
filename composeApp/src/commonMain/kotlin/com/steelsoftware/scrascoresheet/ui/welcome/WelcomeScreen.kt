package com.steelsoftware.scrascoresheet.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.ResumeGame
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.Loading
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.NewGame
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.Strings

@Composable
fun WelcomeScreen(component: WelcomeComponent, lyricist: Lyricist<Strings>) {
    val strings = LocalLyricist.current
    val state by component.state.subscribeAsState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(strings.appDescriptionForWelcomeScreen, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        when (val currentState = state) {
            is Loading -> { /*Left intentionally blank*/ }
            is ResumeGame -> ResumeGameWidget(
                restartGame = component::restartGame,
                resumeGame = component::resumeGame,
            )
            is NewGame -> StartNewGameWidget(
                initialNames = currentState.playerNames,
                onStartGame = component::startGame,
                lyricist = lyricist,
            )
        }
        Text(strings.featuresTitleWelcomeScreen, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Text(strings.featuresListWelcomeScreen, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))
        Text(strings.limitationTitleWelcomeScreen, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Text(strings.limitationsListWelcomeScreen, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ResumeGameWidget(
    restartGame: () -> Unit,
    resumeGame: () -> Unit,
) {
    val strings = LocalLyricist.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.gameInProgressTitleWelcomeScreen, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Text(strings.wouldYouLikeToResumeTitleWelcomeScreen, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { restartGame() }) {
                Text(strings.noButton)
            }
            Button(onClick = { resumeGame() }) {
                Text(strings.yesButton)
            }
        }
    }
}

@Composable
private fun StartNewGameWidget(
    initialNames: List<String>,
    onStartGame: (List<String>) -> Unit,
    lyricist: Lyricist<Strings>
) {
    val strings = LocalLyricist.current
    var playerNames by rememberSaveable {
        mutableStateOf(
            (initialNames + List(4 - initialNames.size) { "" }).take(4)
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(strings.appInstructionsForWelcomeScreen, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        Text(strings.appSelectScoringLanguageWelcomeScreen, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        LanguageDropdown(lyricist)
    }

    // Player name fields
    Column {
        Spacer(Modifier.height(8.dp))
        for (i in playerNames.indices) {
            OutlinedTextField(
                value = playerNames[i],
                onValueChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[i] = newName }
                                },
                placeholder = { Text(strings.player + "${i + 1}") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
    }
    Spacer(Modifier.height(16.dp))

    val typedNames = playerNames
        .map { it.trim() }
        .filter { it.isNotEmpty() }
    Button(
        onClick = { onStartGame(typedNames) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(strings.startButton)
    }
}

@Composable
private fun LanguageDropdown(
    lyricist: Lyricist<Strings>
) {
    val languages = Locales.languageNames
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            languages[lyricist.languageTag]?.let { Text(it) }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        lyricist.languageTag = code
                        expanded = false
                    }
                )
            }
        }
    }
}