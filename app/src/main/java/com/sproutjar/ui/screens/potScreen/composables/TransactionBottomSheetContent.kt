package com.sproutjar.ui.screens.potScreen.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
fun TransactionBottomSheetContent(onConfirm: (description: String, amount: Double) -> Unit) {

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableDoubleStateOf(0.0) }

    val enableCheck = amount > 0 && description.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            value = description,
            textStyle = MaterialTheme.typography.headlineLarge,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    stringResource(R.string.transactions_description_placeholder),
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            trailingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .alpha(if (enableCheck) 1f else .5f)
                        .clickable(enableCheck) {
                            onConfirm(description, amount)
                        },
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.done),
                )
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
        )
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .offset(y = (-16).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                leadingIcon = {
                    Text(
                        modifier = Modifier.alpha(.5f),
                        text = stringResource(R.string.amount_placeholder),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = if (amount > 0.0) amount.toString() else "",
                onValueChange = { newValue ->
                    val filtered = newValue.filterIndexed { index, c ->
                        c.isDigit() || (c == '.' && !newValue.substring(0, index).contains('.'))
                    }
                    amount = filtered.toDoubleOrNull() ?: 0.0
                },
                placeholder = {
                    Text(
                        stringResource(R.string.amount_placeholder_label),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                textStyle = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

@Preview
@Composable
fun TransactionBottomSheetContentPreview() {
    SproutJarTheme {
        Surface {
            TransactionBottomSheetContent { _, _ -> }
        }
    }
}