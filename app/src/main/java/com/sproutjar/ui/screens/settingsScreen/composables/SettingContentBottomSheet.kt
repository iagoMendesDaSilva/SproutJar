package com.sproutjar.ui.screens.settingsScreen.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.Languages
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.color
import com.sproutjar.data.models.icon
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun <T> SettingContentBottomSheet(
    @StringRes title: Int,
    @StringRes description: Int,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    onSelect: (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
        Text(
            stringResource(description),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 30.dp, top = 10.dp),
        )
        LazyRow(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(items) { index, item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .clickable { onSelect(item) })
                { itemContent(item) }
            }
        }
    }
}

@Preview
@Composable
fun SettingContentBottomSheetPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingContentBottomSheet(
                R.string.language,
                R.string.language_desc,
                Languages.entries,
                itemContent = { lang ->
                    SettingItemContentBottomSheet(
                        icon = {
                            Image(
                                painterResource(lang.image),
                                contentDescription = stringResource(
                                    lang.title
                                ),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        },
                        label = lang.title,
                    )
                }, {})
        }
    }
}