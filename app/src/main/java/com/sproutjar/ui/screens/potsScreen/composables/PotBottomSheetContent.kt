package com.sproutjar.ui.screens.potsScreen.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotCategory
import com.sproutjar.data.models.asImageVector
import com.sproutjar.ui.theme.SproutJarTheme

@Composable
internal fun PotBottomSheetContent(potToEdit: Pot? = null, insertPot: (Pot) -> Unit) {

    var category by remember(potToEdit) {
        mutableStateOf(
            potToEdit?.potCategory ?: PotCategory.Emergency
        )
    }
    var title by remember(potToEdit) { mutableStateOf(potToEdit?.title ?: "") }
    var cdiRate by remember(potToEdit) {
        mutableIntStateOf(
            (potToEdit?.cdiPercent?.times(100))?.toInt() ?: 0
        )
    }
    val enableCheck = cdiRate > 0 && title.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            value = title,
            textStyle = MaterialTheme.typography.headlineLarge,
            onValueChange = { title = it },
            placeholder = {
                Text(
                    stringResource(R.string.pot_title_placeholder),
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            trailingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .alpha(if (enableCheck) 1f else .5f)
                        .clickable(enableCheck) {
                            insertPot(
                                Pot(
                                    id = potToEdit?.id ?: 0,
                                    potCategory = category,
                                    title = title,
                                    cdiPercent = cdiRate.toDouble() / 100.0
                                )
                            )
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
                        text = stringResource(R.string.cdi_placeholder),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                value = if (cdiRate > 0) "$cdiRate%" else "",
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() }.take(3)
                    cdiRate = digitsOnly.toIntOrNull() ?: 0
                },
                placeholder = {
                    Text(
                        stringResource(R.string.cdi_placeholder_label),
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


        Text(
            text = stringResource(R.string.category).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 16.dp, top = 32.dp)

        )
        PotCategoryList(category) {
            category = it
        }
    }
}

@Composable
fun PotCategoryList(
    potCategorySelected: PotCategory? = PotCategory.Emergency,
    onPress: (potCategory: PotCategory) -> Unit
) {

    val listState = rememberLazyListState()
    LaunchedEffect(potCategorySelected) {
        potCategorySelected?.let { pot ->
            val index = PotCategory.entries.indexOf(potCategorySelected)
            if (index >= 0) {
                listState.animateScrollToItem(index)
            }
        }
    }
    LazyRow(
        state = listState,
        modifier = Modifier.padding(16.dp)
    ) {
        items(PotCategory.entries) { potCategory ->
            val isSelected = potCategorySelected == potCategory
            val selectedContentColor =
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = .5f
                )
            val borderColor =
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

            Column(
                Modifier
                    .size(105.dp)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(12.dp)
                    .clickable {
                        onPress(potCategory)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Icon(
                    tint = selectedContentColor,
                    imageVector = potCategory.asImageVector(),
                    contentDescription = stringResource(potCategory.categoryDesc)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    Text(
                        color = selectedContentColor,
                        text = stringResource(potCategory.categoryDesc),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PotBottomSheetContentPreview() {
    SproutJarTheme {
        Surface {
            PotBottomSheetContent() {}
        }
    }
}