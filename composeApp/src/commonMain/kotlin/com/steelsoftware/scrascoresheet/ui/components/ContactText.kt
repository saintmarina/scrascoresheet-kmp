package com.steelsoftware.scrascoresheet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.steelsoftware.scrascoresheet.ScrabbleStrings.strings

@Composable
fun ContactText() {
    val uriHandler = LocalUriHandler.current

    val linkStyle = TextLinkStyles(
        style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
            textDecoration = TextDecoration.Underline
        )
    )

    val annotatedText = buildAnnotatedString {
        append(strings.contactText)

        withLink(
            LinkAnnotation.Url(
                url = FacebookUrl,
                styles = linkStyle,
                linkInteractionListener = {
                    uriHandler.openUri(FacebookUrl)
                }
            )
        ) {
            append(strings.onFacebook)
        }

        append(strings.orViaEmail)
        withLink(
            LinkAnnotation.Url(
                url = "mailto:theannasteel@gmail.com",
                styles = linkStyle,
                linkInteractionListener = {
                    uriHandler.openUri("mailto:theannasteel@gmail.com")
                }
            )
        ) {
            append("theannasteel@gmail.com")
        }

        append(".")
    }

    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Justify
    )
}
