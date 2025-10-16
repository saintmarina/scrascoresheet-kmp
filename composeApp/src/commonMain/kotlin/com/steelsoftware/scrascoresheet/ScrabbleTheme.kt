package com.steelsoftware.scrascoresheet

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val RedCenter = Color(0xFFDE322A)
private val RedEdge = Color(0xFF622528)
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val ScrabbleWhite = Color.White
private val ScrabbleGray = Color(0xFFAAAAAA)

private val ScrabbleColorScheme = darkColorScheme(
    primary = RedCenter,
    secondary = RedEdge,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = ScrabbleWhite,
    onSecondary = ScrabbleWhite,
    onBackground = ScrabbleWhite,
    onSurface = ScrabbleWhite,
)

private val ScrabbleTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = Color.White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(1f, 1f),
            blurRadius = 0f
        )
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(1f, 1f),
            blurRadius = 0f
        )
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(1f, 1f),
            blurRadius = 0f
        )
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = Color.White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(1f, 1f),
            blurRadius = 0f
        )
    )
)

private val ScrabbleShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp)
)

@Composable
fun ScrabbleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ScrabbleColorScheme,
        typography = ScrabbleTypography,
        shapes = ScrabbleShapes
    ) {
        // Box with the radial gradient for the app background color
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val centerX = constraints.maxWidth / 2f
            val centerY = constraints.maxHeight / 2f

            val gradient = Brush.radialGradient(
                colors = listOf(RedCenter, RedEdge),
                center = Offset(centerX, centerY),
                radius = maxOf(centerX, centerY) * 1.3f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient)
            ) {
                content()
            }
        }
    }
}