package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.ScrabbleStrings
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.logic.ModifierType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.wooden_tile_background

@Composable
fun LetterTile(
    letter: Char,
    modifierType: ModifierType,
    tileSize: Dp,
    modifier: Modifier = Modifier,
) {
    val color = when (modifierType) {
        ModifierType.DOUBLE_LETTER -> Color(0xFF9FCAE0)
        ModifierType.DOUBLE_WORD -> Color(0xFFEDA498)
        ModifierType.TRIPLE_LETTER -> Color(0xFF18A3E1)
        ModifierType.TRIPLE_WORD -> Color(0xFFF74D13)
        ModifierType.BLANK -> Color.Transparent
    }
    val colorFilter = if (modifierType == ModifierType.BLANK) null
    else ColorFilter.tint(color, blendMode = BlendMode.Modulate)

    val letterFontSize = with(LocalDensity.current) { (tileSize * 0.55f).toSp() }
    val scoreFontSize = with(LocalDensity.current) { (tileSize * 0.22f).toSp() }
    val scorePaddingEnd = tileSize * 0.10f

    val score = ScrabbleStrings.scoreMap[letter.lowercaseChar()]

    Box(modifier = modifier.size(tileSize)) {
        Image(
            painter = painterResource(Res.drawable.wooden_tile_background),
            contentDescription = strings.tileBackgroundDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            colorFilter = colorFilter,
        )

        // Overlay letter
        Text(
            text = letter.uppercase(),
            fontSize = letterFontSize,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        // Overlay small score number
        Text(
            text = score.toString(),
            fontSize = scoreFontSize,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = scorePaddingEnd)
        )
    }
}

@Composable
@Preview
fun LetterTilePreview(
) {
    Row {
        LetterTile(
            letter = 'A',
            tileSize = 48.dp,
            modifierType = ModifierType.TRIPLE_LETTER,
        )
        LetterTile(
            letter = 'B',
            tileSize = 48.dp,
            modifierType = ModifierType.TRIPLE_WORD,
        )
        LetterTile(
            letter = 'C',
            tileSize = 48.dp,
            modifierType = ModifierType.DOUBLE_LETTER,
        )
        LetterTile(
            letter = 'D',
            tileSize = 48.dp,
            modifierType = ModifierType.DOUBLE_WORD,
        )
        LetterTile(
            letter = 'E',
            tileSize = 48.dp,
            modifierType = ModifierType.BLANK,
        )
    }
}