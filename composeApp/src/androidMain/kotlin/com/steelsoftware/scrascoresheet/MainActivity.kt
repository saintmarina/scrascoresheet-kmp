package com.steelsoftware.scrascoresheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.steelsoftware.scrascoresheet.ui.root.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Create RootComponent instance with AndroidGameStorage
        val storage = AndroidGameStorage(this)
        val root = RootComponent(
            componentContext = defaultComponentContext(),
            gameStorage = storage,
        )

        setContent {
            App(root)
        }
    }
}