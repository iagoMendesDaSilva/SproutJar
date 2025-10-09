package com.sproutjar.ui.composables

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sproutjar.R
import com.sproutjar.data.models.MessageInfo
import com.sproutjar.data.models.PotButtonColor
import com.sproutjar.data.models.Theme
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun PotButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    potButtonColor: PotButtonColor = PotButtonColor.PRIMARY,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (potButtonColor) {
                PotButtonColor.PRIMARY -> MaterialTheme.colorScheme.primary
                PotButtonColor.SECONDARY -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Text(
            color = when (potButtonColor) {
                PotButtonColor.PRIMARY -> MaterialTheme.colorScheme.onPrimary
                PotButtonColor.SECONDARY -> MaterialTheme.colorScheme.onSurface
            },
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun PotButtonPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background){
            PotButton(R.string.deposit) {}
        }
    }
}