package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.steelsoftware.scrascoresheet.GLOBAL_SIDE_PADDING
import com.steelsoftware.scrascoresheet.GLOBAL_TOP_PADDING
import com.steelsoftware.scrascoresheet.i18n.EnglishStrings
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.RussianStrings
import com.steelsoftware.scrascoresheet.i18n.SpanishStrings
import com.steelsoftware.scrascoresheet.logic.ModifierType
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun ModifierPopover(
    inputBoxBounds: Rect?,
    tileBounds: Rect?,
    onSelect: (ModifierType) -> Unit,
    shouldShowPopoverInstructions: Boolean,
) {
    if (inputBoxBounds == null || tileBounds == null) return

    val arrowWidth = 24.dp
    val arrowHeight = 8.dp
    val cornerRadius = 12.dp

    val density = LocalDensity.current

    val arrowW = with(density) { arrowWidth.toPx() }
    val arrowH = with(density) { arrowHeight.toPx() }
    val radius = with(density) { cornerRadius.toPx() }

    var popoverWidth by remember { mutableStateOf(0f) }
    var popoverGlobalLeft by remember { mutableStateOf(0f) }

    val y = with(density) { (tileBounds.bottom - GLOBAL_TOP_PADDING.dp.toPx()).toInt() }
    Column(
        modifier = Modifier.offset {
            IntOffset(
                x = with(density) {
                    // Center the popover horizontally relative to the tile
                    // but ensure it stays within the input box bounds
                    val inputLeft = inputBoxBounds.left
                    val inputRight = inputBoxBounds.right
                    val popoverHalf = popoverWidth / 2f
                    val anchorCenterX = (tileBounds.left + tileBounds.right) / 2f
                    var targetX = anchorCenterX - popoverHalf
                    val minX = inputLeft
                    val maxX = inputRight - popoverWidth + (GLOBAL_SIDE_PADDING * 2).dp.toPx()

                    targetX = targetX.coerceIn(minX, maxX)
                    (targetX - inputLeft).toInt()
                },
                y = y,
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .onGloballyPositioned { coordinates ->
                    val bounds = coordinates.boundsInWindow()
                    popoverWidth = coordinates.size.width.toFloat()
                    popoverGlobalLeft = bounds.left
                }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                if (popoverWidth == 0f) return@Canvas
                val anchorCenterX = (tileBounds.left + tileBounds.right) / 2f
                val arrowCenterX = anchorCenterX - popoverGlobalLeft
                val clampedArrowCenterX = arrowCenterX.coerceIn(
                    radius + arrowW / 2,
                    popoverWidth - radius - arrowW / 2
                )
                // Draw popover background with arrow
                val path = Path().apply {
                    moveTo(radius, arrowH)
                    lineTo(clampedArrowCenterX - arrowW / 2, arrowH)
                    lineTo(clampedArrowCenterX, 0f)
                    lineTo(clampedArrowCenterX + arrowW / 2, arrowH)
                    lineTo(size.width - radius, arrowH)
                    quadraticTo(size.width, arrowH, size.width, arrowH + radius)
                    lineTo(size.width, size.height - radius)
                    quadraticTo(size.width, size.height, size.width - radius, size.height)
                    lineTo(radius, size.height)
                    quadraticTo(0f, size.height, 0f, size.height - radius)
                    lineTo(0f, arrowH + radius)
                    quadraticTo(0f, arrowH, radius, arrowH)
                    close()
                }

                drawPath(path = path, color = Color.White)
                drawPath(
                    path = path,
                    color = Color.LightGray,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            // Content inside the popover, padded to avoid the arrow area
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = arrowHeight + 8.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
            ) {
                PopoverContent(shouldShowPopoverInstructions, onSelect)
            }
        }
    }
}

@Composable
fun PopoverContent(
    shouldShowPopoverInstructions: Boolean,
    onSelect: (ModifierType) -> Unit
) {
    val strings = LocalLyricist.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            PopoverIcon(
                modifierType = ModifierType.DOUBLE_LETTER,
                onSelect = onSelect
            )
            PopoverIcon(
                modifierType = ModifierType.DOUBLE_WORD,
                isFirstTurn = shouldShowPopoverInstructions,
                onSelect = onSelect
            )
            PopoverIcon(
                modifierType = ModifierType.TRIPLE_LETTER,
                onSelect = onSelect
            )
            PopoverIcon(
                modifierType = ModifierType.TRIPLE_WORD,
                onSelect = onSelect
            )
            PopoverIcon(
                modifierType = ModifierType.BLANK,
                onSelect = onSelect
            )
        }
        if (shouldShowPopoverInstructions) {
            Text(
                text = strings.pressOnTheStar,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }
    }
}


@Preview(
    // show full screen
    widthDp = 400,
    heightDp = 200,
    backgroundColor = 0xFF333333,
    showBackground = true,
    name = "ModifierPopover First Turn True"
)
@Composable
fun ModifierPopoverFirstTurnTruePreview() {
    val fakeInputBoxAnchor = Rect(
        left = 158f,
        top = 120f,
        right = 400f,
        bottom = 180f
    )
    val fakeAnchor = Rect(
        left = 150f,
        top = 0f,
        right = 200f,
        bottom = 160f
    )
    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    ProvideStrings(lyricist, LocalLyricist) {
        ModifierPopover(
            inputBoxBounds = fakeInputBoxAnchor,
            tileBounds = fakeAnchor,
            onSelect = {},
            shouldShowPopoverInstructions = true,
        )
    }
}

@Preview(
    widthDp = 400,
    heightDp = 200,
    backgroundColor = 0xFF333333,
    showBackground = true,
    name = "ModifierPopover FirstTurn False"
)
@Composable
fun ModifierPopoverFirstTurnFalsePreview() {
    val fakeInputBoxAnchor = Rect(
        left = 158f,
        top = 120f,
        right = 400f,
        bottom = 180f
    )
    val fakeAnchor = Rect(
        left = 150f,
        top = 0f,
        right = 200f,
        bottom = 160f
    )

    val lyricist = rememberStrings(
        translations = mapOf(
            Locales.ENGLISH to EnglishStrings,
            Locales.SPANISH to SpanishStrings,
            Locales.RUSSIAN to RussianStrings,
        ),
        defaultLanguageTag = Locales.ENGLISH,
        currentLanguageTag = Locales.ENGLISH,
    )
    ProvideStrings(lyricist, LocalLyricist) {
        ModifierPopover(
            inputBoxBounds = fakeInputBoxAnchor,
            tileBounds = fakeAnchor,
            onSelect = {},
            shouldShowPopoverInstructions = false,
        )
    }
}