package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(component: GameComponent) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Game in progress")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { component.finishGame() }) {
            Text("Finish Game")
        }
    }
}