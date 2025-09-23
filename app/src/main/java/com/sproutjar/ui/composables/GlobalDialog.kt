package com.sproutjar.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sproutjar.R
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.ui.theme.SproutJarTheme


@Composable
fun GlobalDialog(globalDialog: GlobalDialogState, onClose: () -> Unit) {

    Dialog(
        onDismissRequest = { onClose() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 10.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = stringResource(id = globalDialog.dialogInfo.messageDialog.titleID)
                    )
                    Text(
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = stringResource(id = globalDialog.dialogInfo.messageDialog.messageID),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    globalDialog.onSuccess?.let {
                        TwoOptionButton(
                            onSuccess = {
                                it()
                                onClose()
                            },
                            onDismiss = onClose
                        )
                    } ?: run {
                        OneOptionButton(
                            onDismiss = {
                                globalDialog.onDismiss
                                onClose()
                            }
                        )
                    }
                }
            }
        })
}

@Composable
fun OneOptionButton(onDismiss: () -> Unit) {
    Text(
        text = stringResource(id = R.string.ok),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.clickable {
            onDismiss()
        }
    )
}

@Composable
fun TwoOptionButton(onSuccess: () -> Unit, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.cancel),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(end = 20.dp)
                .clickable {
                    onDismiss()
                },
        )
        Text(
            text = stringResource(id = R.string.ok),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable {
                onSuccess()
            },
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun GlobalDialogPreviewOneOption() {
    SproutJarTheme() {
        GlobalDialog(GlobalDialogState(dialogInfo = DialogInfo())) {}
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun GlobalDialogPreviewTwoOptions() {
    SproutJarTheme {
        GlobalDialog(GlobalDialogState(dialogInfo = DialogInfo(), onSuccess = {})) {}
    }

}