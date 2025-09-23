package com.sproutjar.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.Theme
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun LogoLabel(theme: Theme, messageDialog: MessageDialog) {
    Column(
        modifier = Modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Logo(theme)
        Text(
            style = MaterialTheme.typography.titleLarge,
            text =
                stringResource(messageDialog.titleID),
        )
        Text(
            modifier = Modifier.alpha(.5f),
            style = MaterialTheme.typography.bodyMedium,
            text =
                stringResource(messageDialog.messageID),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RetryLabelPreview() {
    SproutJarTheme {
        LogoLabel(Theme.SPROUT_JAR, MessageDialog(R.string.checking_server, R.string.try_again))
    }
}