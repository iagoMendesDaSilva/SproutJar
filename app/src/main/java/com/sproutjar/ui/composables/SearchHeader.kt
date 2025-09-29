package com.sproutjar.ui.composables

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.ui.theme.SproutJarTheme


@Composable
internal fun SearchHeader(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    onValueChange: (value: String) -> Unit
) {

    val textValue = remember { mutableStateOf("") }
    var textFieldExpanded by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(true) }

    LaunchedEffect(textFieldExpanded) {
        if (textFieldExpanded)
            focusRequester.requestFocus()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.displaySmall
            )
            Box(
                modifier = Modifier.size(48.dp, 48.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        textFieldExpanded = true
                    },
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            }
        }
        AnimatedVisibility(
            visible = textFieldExpanded,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 200)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 200)
            )
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocused.value = it.isFocused
                    },
                value = textValue.value,
                onValueChange = {
                    textValue.value = it
                    onValueChange(it)
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onValueChange("")
                            textFieldExpanded = false
                        },
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(id = R.string.go_back)
                    )
                },
                trailingIcon = {
                    if (textValue.value.isNotEmpty())
                        Icon(
                            modifier = Modifier.clickable {
                                textValue.value = ""
                                onValueChange("")
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.search),
                        )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

@Preview
@Composable
fun SearchHeaderPreview() {
    SproutJarTheme {
            SearchHeader(R.string.settings) {}
        }
}