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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.logic.isLetterAllowed
import com.steelsoftware.scrascoresheet.logic.scoreListsMap
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScrabbleInputBox(
    language: String,
    onInputChanged: (String) -> Unit,
    popoverAnchor: Rect? = null,
    setPopoverAnchor: (Rect?) -> Unit,
    setInputBoxBounds: (Rect?) -> Unit,
    currentWord: Word,
    setCurrentWord: (Word) -> Unit,
    setSelectedLetterTileIndex: (Int?) -> Unit,
    calculateScrabbleScore: (String, List<ModifierType>, String) -> Int,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    val tileBoundsMap = remember { mutableStateMapOf<Int, Rect>() }

    val maxTilesPerRow = 8
    val maxLetters = 15

    BoxWithConstraints(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                setInputBoxBounds(coordinates.boundsInWindow())
            }
            .border(1.dp, Color.White, RoundedCornerShape(5.dp))
            .background(Color(0xFFE66058))
            .padding(8.dp)
            .clickable {
                focusRequester.requestFocus()
                keyboardController?.show()
            },
    ) {

        val fullWidth = maxWidth
        val boxWidth = if (fullWidth > 550.dp) 550.dp else fullWidth
        val tileSize = (boxWidth / maxTilesPerRow) - 4.dp
        val rows = if (currentWord.value.isEmpty()) listOf("") else currentWord.value.chunked(
            maxTilesPerRow
        )

        Box(
            modifier = Modifier
                .width(boxWidth)
                .wrapContentHeight()
                .align(Alignment.CenterStart)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                rows.forEachIndexed { rowIndex, rowText ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val displayText =
                            if (rowText.isEmpty()) listOf() else rowText.toList()

                        displayText.forEachIndexed { columnIndex, ch ->
                            val index = rowIndex * maxTilesPerRow + columnIndex
                            val letter = ch.toString()
                            val score = scoreListsMap[language]?.get(ch.lowercaseChar()) ?: 0

                            val primaryModifier =
                                currentWord.modifiers.getOrNull(index) ?: ModifierType.BLANK
                            LetterTile(
                                letter = letter,
                                score = score,
                                modifierType = primaryModifier,
                                tileSize = tileSize,
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        tileBoundsMap[index] = coordinates.boundsInWindow()
                                    }
                                    .clickable {
                                        setSelectedLetterTileIndex(index)
                                        val bounds = tileBoundsMap[index]
                                        if (bounds != null) {
                                            // Toggle popover open/close
                                            setPopoverAnchor(if (popoverAnchor == bounds) null else bounds)
                                        }
                                    }
                            )
                        }
                        if (rowText.length < maxTilesPerRow && rowIndex == rows.lastIndex) {
                            Box(
                                modifier = Modifier.height(tileSize),
                                contentAlignment = Alignment.Center
                            ) {
                                if (hasFocus) {
                                    BlinkingCursor(height = tileSize)
                                }
                            }
                        }
                    }
                }
            }

            // Hidden real input field for typing
            BasicTextField(
                value = currentWord.value,
                onValueChange = { input ->
                    setPopoverAnchor(null)
                    val filtered = buildString {
                        input.forEach { ch ->
                            if (isLetterAllowed(ch, language)) append(ch.uppercaseChar())
                        }
                    }.take(maxLetters)

                    if (filtered != currentWord.value) {
                        val oldModifiers = currentWord.modifiers.toMutableList()
                        val newModifiers = mutableListOf<ModifierType>()

                        // For every character in the new text, either reuse an existing modifier or create NONE
                        for (i in filtered.indices) {
                            newModifiers += oldModifiers.getOrNull(i) ?: ModifierType.BLANK
                        }

                        // If the new text is shorter, drop trailing modifiers
                        val trimmedModifiers = newModifiers.take(filtered.length)

                        // Recalculate the word’s score
                        val newScore = calculateScrabbleScore(filtered, trimmedModifiers, language)
                        println("XXX new score = $newScore")

                        // Update the current word state
                        setCurrentWord(
                            currentWord.copy(
                                value = filtered,
                                modifiers = trimmedModifiers,
                                score = newScore
                            )
                        )

                        onInputChanged(filtered)
                    }
                },
                modifier = Modifier
                    .size(1.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { hasFocus = it.isFocused }
                    .alpha(0f) // invisible
                    .background(Color.Transparent)
                    .onPreviewKeyEvent { event ->
                        setPopoverAnchor(null)
                        when (event.key) {
                            Key.DirectionLeft,
                            Key.DirectionRight,
                            Key.DirectionUp,
                            Key.DirectionDown,
                            Key.Enter,
                            Key.Delete,
                            Key.Tab -> true

                            else -> false
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters
                ),
            )
        }
    }
}

@Composable
fun BlinkingCursor(height: Dp) {
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
            .height(height)
            .alpha(alpha)
            .background(Color.White)
    )
}

@Composable
@Preview
fun ScrabbleInputBoxPreview() {
    ScrabbleInputBox(
        language = Locales.ENGLISH,
        onInputChanged = {},
        popoverAnchor = null,
        setPopoverAnchor = { },
        currentWord = Word("HELLO", emptyList(), 0),
        setCurrentWord = { },
        setInputBoxBounds = { },
        calculateScrabbleScore = { _, _, _ -> 8 },
        setSelectedLetterTileIndex = {},
    )
}