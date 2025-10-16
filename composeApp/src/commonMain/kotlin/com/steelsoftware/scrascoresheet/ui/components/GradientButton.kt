package com.steelsoftware.scrascoresheet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GradientButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDE322A).copy(alpha = if (enabled) 1f else 0.5f),
            Color(0xFF622528).copy(alpha = if (enabled) 1f else 0.5f)
        )
    )
    val borderColor = if (enabled) Color(0xFF980C10) else Color.Transparent
    val contentAlpha = if (enabled) 1f else 0.45f

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White.copy(alpha = contentAlpha),
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White.copy(alpha = contentAlpha)
        ),
        modifier = modifier
            .background(
                brush = gradient,
                shape = MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                color = Color.White.copy(alpha = if (enabled) 1f else 0.45f),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Preview
@Composable
fun GradientButtonPreview() {
    ScrabbleTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientButton(onClick = {}, text = "END TURN")
            GradientButton(onClick = {}, text = "+ ADD WORD", enabled = true)
            GradientButton(onClick = {}, text = "BINGO", enabled = false)
        }
    }
}