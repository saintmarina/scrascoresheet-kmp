package com.steelsoftware.scrascoresheet.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

@OptIn(ExperimentalTextApi::class)
@Composable
fun CopyrightTextAndPrivacyPolicy() {
    val uriHandler = LocalUriHandler.current

    val linkStyle = TextLinkStyles(
        style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
            textDecoration = TextDecoration.Underline
        )
    )

    val text = buildAnnotatedString {
        append("© 2025 Copyright Anna Steel | ")

        withLink(
            LinkAnnotation.Url(
                url = PrivacyPolicyUrl,
                styles = linkStyle,
                linkInteractionListener = {
                    uriHandler.openUri(PrivacyPolicyUrl)
                }
            )
        ) {
            append("Privacy Policy")
        }
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center
    )
}