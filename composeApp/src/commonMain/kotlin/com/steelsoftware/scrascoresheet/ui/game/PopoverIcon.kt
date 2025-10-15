package com.steelsoftware.scrascoresheet.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steelsoftware.scrascoresheet.i18n.LocalLyricist
import com.steelsoftware.scrascoresheet.logic.ModifierType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.double_score
import scrascoresheet.composeapp.generated.resources.star_score
import scrascoresheet.composeapp.generated.resources.tripple_score
import scrascoresheet.composeapp.generated.resources.wooden_tile_background

@Composable
fun PopoverIcon(
    modifierType: ModifierType,
    isFirstTurn: Boolean = false,
) {
    val strings = LocalLyricist.current
    val (res, color, text) = when (modifierType) {
        ModifierType.DOUBLE_LETTER -> Triple(
            Res.drawable.double_score,
            Color(0xFF9FCAE0),
            strings.doubleLetterScore,
        )

        ModifierType.TRIPLE_LETTER -> Triple(
            Res.drawable.tripple_score,
            Color(0xFF18A3E1),
            strings.tripleLetterScore
        )

        ModifierType.DOUBLE_WORD -> Triple(
            if (isFirstTurn) Res.drawable.star_score else Res.drawable.double_score,
            Color(0xFFEDA498),
            strings.doubleWordScore,
        )

        ModifierType.TRIPLE_WORD -> Triple(
            Res.drawable.tripple_score,
            Color(0xFFF74D13),
            strings.tripleWordScore,
        )

        ModifierType.BLANK -> Triple(
            Res.drawable.wooden_tile_background,
            Color.LightGray,
            strings.blankTile,
        )
    }
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(color),
    ) {
        // Background image (tile)
        Image(
            painter = painterResource(res),
            contentDescription = strings.tileBackgroundDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
        )
        if (!isFirstTurn) {
            // Centered multiline text that scales to fit
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(6.dp),

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTextStyle.current.copy(
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun PopoverIconPreview() {
    Row {
        PopoverIcon(
            modifierType = ModifierType.DOUBLE_LETTER,
        )
        PopoverIcon(
            modifierType = ModifierType.DOUBLE_WORD,
            isFirstTurn = true,
        )
        PopoverIcon(
            modifierType = ModifierType.TRIPLE_LETTER,
        )
        PopoverIcon(
            modifierType = ModifierType.TRIPLE_WORD,
        )
        PopoverIcon(
            modifierType = ModifierType.BLANK,
        )
    }
}