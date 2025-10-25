package com.steelsoftware.scrascoresheet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings
import com.steelsoftware.scrascoresheet.ScrabbleTheme
import org.jetbrains.compose.resources.painterResource
import scrascoresheet.composeapp.generated.resources.Res
import scrascoresheet.composeapp.generated.resources.facebook_icon
import scrascoresheet.composeapp.generated.resources.github_icon


const val FacebookUrl = "https://www.facebook.com/ScrabbleScore.online/"
const val GitHubUrl = "https://github.com/saintmarina/scrabblescore.online"
const val PrivacyPolicyUrl = "https://scrabblescore.online/privacy_policy.html"

@Composable
fun Footer() {
    val uriHandler = LocalUriHandler.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = strings.contactTitle,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
        ContactText()
        HorizontalDivider(
            color = ScrabbleTheme.colors.deepRed30,
            modifier = Modifier.fillMaxWidth(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.facebook_icon),
                contentDescription = strings.faceBookDescription,
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        uriHandler.openUri(FacebookUrl)
                    },
                contentScale = ContentScale.FillWidth,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(Res.drawable.github_icon),
                contentDescription = strings.gitHubDescription,
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        uriHandler.openUri(GitHubUrl)
                    },
                contentScale = ContentScale.FillWidth,
            )
        }
        CopyrightTextAndPrivacyPolicy()
        Text(
            text = "SCRABBLE® is a registered trademark. All intellectual property rights in and to the game are owned in the USA and Canada by Hasbro Inc., and throughout the rest of the world by Mattel, Inc",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}
