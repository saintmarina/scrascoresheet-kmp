package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.isLetterAllowed
import com.steelsoftware.scrascoresheet.logic.scoreListsMap
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScrabbleInputBox(
    language: String,
    onInputChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .border(1.dp, Color.White, RoundedCornerShape(5.dp))
            .background(Color(0xFFE66058))
            .height(81.dp)
            .width(471.dp)
            .padding(8.dp)
            .clickable { focusRequester.requestFocus() },
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            text.forEach { ch ->
                val letter = ch.toString()
                val score = scoreListsMap[language]
                    ?.get(ch.lowercaseChar())
                    ?: 0
                LetterTile(
                    letter = letter,
                    score = score,
                    modifierType = ModifierType.BLANK
                )
            }
            BlinkingCursor()
        }

        // Hidden real input field for typing
        BasicTextField(
            value = text,
            onValueChange = { input ->
                val filtered = buildString {
                    input.forEach { ch ->
                        if (isLetterAllowed(ch, language)) append(ch.uppercaseChar())
                    }
                }

                if (filtered != text) {
                    text = filtered
                    onInputChanged(filtered)
                }
            },
            modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester)
                .alpha(0f) // invisible
                .background(Color.Transparent)
                .onPreviewKeyEvent { event ->
                    // TODO: handle forward delete key
                    when (event.key) {
                        Key.DirectionLeft,
                        Key.DirectionRight,
                        Key.DirectionUp,
                        Key.DirectionDown,
                        Key.Enter,
                        Key.Delete,
                        Key.Tab -> true

                        Key.Backspace -> {
                            if (text.isNotEmpty()) {
                                text = text.dropLast(1)
                                onInputChanged(text)
                            }
                            true
                        }

                        else -> false
                    }
                },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters
            ),
        )
    }
}

@Composable
fun BlinkingCursor() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .padding(start = 5.dp)
            .width(3.dp)
            .height(60.dp)
            .alpha(alpha)
            .background(Color.White)
    )
}

@Composable
@Preview
fun ScrabbleInputBoxPreview() {
    ScrabbleInputBox(
        language = Locales.ENGLISH,
        onInputChanged = {}
    )
}