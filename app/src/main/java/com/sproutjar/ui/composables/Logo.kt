package com.sproutjar.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sproutjar.R
import com.sproutjar.data.models.Theme
import com.sproutjar.ui.theme.SproutJarTheme


@Composable
fun Logo(theme: Theme, modifier: Modifier = Modifier) {

    Image(
        contentScale = ContentScale.Fit,
        modifier = modifier.fillMaxWidth(.15f),
        contentDescription = stringResource(id = R.string.logo),
        painter = painterResource(
            id = when (theme) {
                Theme.SPROUT_JAR -> R.drawable.sprout_jar_logo
                Theme.DARK -> R.drawable.sprout_jar_logo_white
                Theme.LIGHT -> R.drawable.sprout_jar_logo_black
            }
        )
    )
}


@Preview
@Composable
fun LogoPreview() {
    SproutJarTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Alignment.Center
        ) {
            Logo(
                theme = Theme.SPROUT_JAR
            )
        }
    }
}