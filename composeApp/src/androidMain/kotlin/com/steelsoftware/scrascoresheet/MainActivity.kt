package com.steelsoftware.scrascoresheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.arkivanov.decompose.defaultComponentContext
import com.steelsoftware.scrascoresheet.i18n.EnglishStrings
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.RussianStrings
import com.steelsoftware.scrascoresheet.i18n.SpanishStrings
import com.steelsoftware.scrascoresheet.ui.root.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Create RootComponent instance with AndroidGameStorage
        val storage = AndroidGameStorage(this)
        val analytics = AnalyticsManager(
            apiKey = AppConfig.AMPLITUDE_API_KEY,
            context = this.applicationContext,
        )
        val root = RootComponent(
            componentContext = defaultComponentContext(),
            gameStorage = storage,
            analytics = analytics,
        )

        val urlOpener = AndroidUrlOpener(applicationContext)

        setContent {
            val lyricist = rememberStrings(
                translations = mapOf(
                    Locales.ENGLISH to EnglishStrings,
                    Locales.SPANISH to SpanishStrings,
                    Locales.RUSSIAN to RussianStrings,
                ),
                defaultLanguageTag = Locales.ENGLISH,
                currentLanguageTag = Locales.ENGLISH,
            )
            ScrabbleStrings.setLanguage(lyricist.languageTag)
            ProvideStrings(lyricist, LocalLyricist) {
                App(root, lyricist, urlOpener)
            }
        }
    }
}