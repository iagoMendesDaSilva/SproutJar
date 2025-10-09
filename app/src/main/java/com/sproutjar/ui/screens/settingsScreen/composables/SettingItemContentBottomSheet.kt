package com.sproutjar.ui.screens.settingsScreen.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.Languages
import com.sproutjar.data.models.SettingsSection.Companion.sections
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.color
import com.sproutjar.data.models.icon
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun SettingItemContentBottomSheet(
    icon: @Composable () -> Unit,
    @StringRes label: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(backgroundColor, CircleShape)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) { icon() }
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Preview
@Composable
fun SettingItemContentBottomSheetPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingItemContentBottomSheet(
                icon = {
                    Image(
                        painterResource(Languages.ENGLISH.image),
                        contentDescription = stringResource(
                            Languages.ENGLISH.title
                        ),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                },
                label = Languages.ENGLISH.title,
            )
        }
    }
}