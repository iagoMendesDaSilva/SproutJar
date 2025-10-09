package com.sproutjar.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sproutjar.R
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun GlobalDialog(
    globalDialog: GlobalDialogState,
) {
    val dialogInfo = globalDialog.dialogInfo

    AlertDialog(
        onDismissRequest = {
            globalDialog.onDismiss()
        },
        title = {
            Text(
                text = stringResource(id = dialogInfo.messageInfo.titleID),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(id = dialogInfo.messageInfo.messageID),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            globalDialog.onSuccess?.let {
                TextButton(onClick = {
                    it.invoke()
                    globalDialog.onDismiss()
                }) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    globalDialog.onDismiss()
                }) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    text = stringResource(id = if (globalDialog.onSuccess == null) R.string.ok else R.string.cancel),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

@Preview
@Composable
fun GlobalDialogPreviewOneOption() {
    SproutJarTheme() {
        Surface {
            GlobalDialog(GlobalDialogState(dialogInfo = DialogInfo()))
        }
    }

}

@Preview
@Composable
fun GlobalDialogPreviewTwoOptions() {
    SproutJarTheme {
        Surface {
            GlobalDialog(GlobalDialogState(dialogInfo = DialogInfo(), onSuccess = {}))
        }
    }
}