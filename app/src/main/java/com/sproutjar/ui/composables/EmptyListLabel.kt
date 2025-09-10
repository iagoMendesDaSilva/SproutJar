package com.sproutjar.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.sproutjar.R
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.ThemePreviews


@Composable
fun EmptyListLabel(@StringRes text: Int = R.string.not_found, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@ThemePreviews
@Composable
fun EmptyListLabelPreview() {
    SproutJarTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyListLabel()
            }
        }
    }
}