package com.steelsoftware.scrascoresheet.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import com.steelsoftware.scrascoresheet.ui.welcome.State.ResumeGame
import com.steelsoftware.scrascoresheet.ui.welcome.State.Loading
import com.steelsoftware.scrascoresheet.ui.welcome.State.NewGame
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(strings.hello, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        when (state) {
            is Loading -> Text("Loading...", style = MaterialTheme.typography.bodyMedium)
            is ResumeGame -> ResumeGameWidget()
            is NewGame -> StartNewGameWidget(
                onStartGame = component::startGame,
                lyricist = lyricist,
            )
        }
        Button(onClick = { component.startGame() }) {
            Text("Start Game")
        }
    }
}

@Composable
private fun ResumeGameWidget() {

}

@Composable
private fun StartNewGameWidget(
    onStartGame: () -> Unit,
    lyricist: Lyricist<Strings>
) {
    var playerNames by rememberSaveable { mutableStateOf(listOf("", "", "", "")) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Select game language:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        LanguageDropdown(lyricist)
    }

    // Player name fields
    Column {
        Text("Enter player names:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        for (i in playerNames.indices) {
            OutlinedTextField(
                value = playerNames[i],
                onValueChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[i] = newName }
                                },
                placeholder = { Text("Player ${i + 1}") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
    }

    // Start button: enabled if at least 2 names are entered
    Button(
        onClick = { onStartGame() },
        enabled = playerNames.count { it.isNotBlank() } >= 2,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("START GAME")
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
            // Display the language set in the lyricist
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