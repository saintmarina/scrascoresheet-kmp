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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steelsoftware.scrascoresheet.logic.ModifierType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.wooden_tile_background

@Composable
fun LetterTile(
    letter: String,
    score: Int,
    modifierType: ModifierType = ModifierType.BLANK,
    modifier: Modifier = Modifier,
) {
    val color = when (modifierType) {
        ModifierType.DOUBLE_LETTER -> Color(0xFF9FCAE0)
        ModifierType.DOUBLE_WORD -> Color(0xFFEDA498)
        ModifierType.TRIPLE_LETTER -> Color(0xFF18A3E1)
        ModifierType.TRIPLE_WORD -> Color(0xFFF74D13)
        ModifierType.BLANK -> Color.LightGray
    }

    Box(
        modifier = modifier
            .size(69.dp)
            .padding(2.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.wooden_tile_background),
            contentDescription = "Tile background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(color, blendMode = BlendMode.Modulate),
        )

        // Overlay letter
        Text(
            text = letter.uppercase(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        // Overlay small score number
        Text(
            text = score.toString(),
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 6.dp, bottom = 4.dp)
        )
    }
}

@Composable
@Preview
fun LetterTilePreview(
) {
    Row {
        LetterTile(
            letter = "A",
            score = 1,
            modifierType = ModifierType.TRIPLE_LETTER,
        )
        LetterTile(
            letter = "B",
            score = 1,
            modifierType = ModifierType.TRIPLE_WORD,
        )
        LetterTile(
            letter = "C",
            score = 1,
            modifierType = ModifierType.DOUBLE_LETTER,
        )
        LetterTile(
            letter = "D",
            score = 1,
            modifierType = ModifierType.DOUBLE_WORD,
        )
        LetterTile(
            letter = "E",
            score = 1,
            modifierType = ModifierType.BLANK,
        )
    }
}