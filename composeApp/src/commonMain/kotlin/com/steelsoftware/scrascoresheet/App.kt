package com.steelsoftware.scrascoresheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.steelsoftware.scrascoresheet.ui.root.RootComponent
import com.steelsoftware.scrascoresheet.ui.root.RootContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(root: RootComponent) {
    MaterialTheme {
        RootContent(root)
    }
}