package com.steelsoftware.scrascoresheet.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.Lyricist
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.steelsoftware.scrascoresheet.GLOBAL_SIDE_PADDING
import com.steelsoftware.scrascoresheet.ScrabbleStrings
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import com.steelsoftware.scrascoresheet.i18n.Locales
import com.steelsoftware.scrascoresheet.i18n.Strings
import com.steelsoftware.scrascoresheet.ui.components.GradientButton
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.Loading
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.NewGame
import com.steelsoftware.scrascoresheet.ui.welcome.WelcomeState.ResumeGame
import org.jetbrains.compose.resources.painterResource
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.logo

@Composable
fun WelcomeScreen(component: WelcomeComponent, lyricist: Lyricist<Strings>) {
    val state by component.state.subscribeAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = strings.logoDescription,
            modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = GLOBAL_SIDE_PADDING.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
        )
        Text(
            text = strings.appDescriptionForWelcomeScreen,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify,
        )

        when (val currentState = state) {
            is Loading -> { /*Left intentionally blank*/
            }

            is ResumeGame -> ResumeGameWidget(
                restartGame = component::restartGame,
                resumeGame = component::resumeGame,
            )

            is NewGame -> StartNewGameWidget(
                initialNames = currentState.playerNames,
                onStartGame = component::startGame,
                lyricist = lyricist,
            )
        }
        Text(
            text = strings.featuresTitleWelcomeScreen,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )

        Text(
            text = strings.featuresListWelcomeScreen,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify,
        )

        Text(
            text = strings.limitationTitleWelcomeScreen,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )

        Text(
            text = strings.limitationsListWelcomeScreen,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify,
        )
    }
}

@Composable
private fun ResumeGameWidget(
    restartGame: () -> Unit,
    resumeGame: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center,

        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                strings.gameInProgressTitleWelcomeScreen,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                strings.wouldYouLikeToResumeTitleWelcomeScreen,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientButton(
                    onClick = { restartGame() },
                    text = strings.noButton,
                    modifier = Modifier.alpha(0.70f)
                )
                GradientButton(
                    onClick = { resumeGame() },
                    text = strings.yesButton
                )
            }
        }
    }
}

@Composable
private fun StartNewGameWidget(
    initialNames: List<String>,
    onStartGame: (List<String>) -> Unit,
    lyricist: Lyricist<Strings>
) {
    var playerNames by rememberSaveable {
        mutableStateOf(
            (initialNames + List(4 - initialNames.size) { "" }).take(4)
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(strings.appInstructionsForWelcomeScreen, style = MaterialTheme.typography.bodyLarge)
        Text(
            strings.appSelectScoringLanguageWelcomeScreen,
            style = MaterialTheme.typography.titleLarge
        )
        LanguageDropdown(lyricist)
    }
    PlayerNameInputs(
        playerNames = playerNames,
        onNameChange = { index, newName ->
            playerNames = playerNames.toMutableList().also { it[index] = newName }
        },
    )

    val typedNames = playerNames
        .map { it.trim() }
        .filter { it.isNotEmpty() }
    val placeHolderNames = listOf(
        strings.player + " 1",
        strings.player + " 2"
    )

    GradientButton(
        onClick = {
            val finalNames = typedNames.ifEmpty {
                placeHolderNames
            }
            onStartGame(finalNames)
        },
        text = strings.startButton,
        modifier = Modifier.fillMaxWidth(0.65f),
    )
}

@Composable
fun PlayerNameInputs(
    playerNames: List<String>,
    onNameChange: (Int, String) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (i in playerNames.indices) {
            BasicTextField(
                value = playerNames[i],
                onValueChange = { newName -> onNameChange(i, newName) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(56.dp)
                            .padding(4.dp)
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (playerNames[i].isEmpty()) {
                            Text(
                                text = strings.player + " ${i + 1}",
                                color = Color(0xFF980C10),
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun LanguageDropdown(
    lyricist: Lyricist<Strings>
) {
    val languages = Locales.languageNames
    var expanded by remember { mutableStateOf(false) }


    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ScrabbleTheme.colors.brightRed.copy(alpha = 0.95f),
            ScrabbleTheme.colors.deepRed.copy(alpha = 0.95f),
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .height(47.dp)
            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .matchParentSize(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            border = null,
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = languages[ScrabbleStrings.language] ?: strings.select,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(1.dp, ScrabbleTheme.colors.offWhite, RoundedCornerShape(6.dp))
        ) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif
                        )
                    },
                    onClick = {
                        lyricist.languageTag = code
                        ScrabbleStrings.setLanguage(code)
                        expanded = false
                    }
                )
            }
        }
    }
}