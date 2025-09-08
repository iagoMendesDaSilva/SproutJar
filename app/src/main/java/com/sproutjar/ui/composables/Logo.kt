package com.sproutjar.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.Theme
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.ThemePreviews


@Composable
fun Logo(theme: Theme, modifier: Modifier = Modifier) {

    Image(
        contentScale = ContentScale.Fit,
        modifier = modifier.fillMaxWidth(.4f),
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ThemePreviews
@Composable
fun LogoPreview() {
    SproutJarTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Logo(
                    theme = Theme.SPROUT_JAR
                )
            }
        }
    }
}