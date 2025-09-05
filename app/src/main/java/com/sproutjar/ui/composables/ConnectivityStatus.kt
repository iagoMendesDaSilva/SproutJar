package com.sproutjar.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.ui.theme.Green
import com.sproutjar.ui.theme.Red
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.currentConnectivityState
import com.sproutjar.utils.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay


@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus(connection: ConnectionState) {

    val isConnected = connection === ConnectionState.Available
    val heightAnim = remember { Animatable(0f) }
    var previousConnection by remember { mutableStateOf<ConnectionState?>(null) }
    val heightSize = 40f


    LaunchedEffect(connection) {
        if (connection != previousConnection) {
            if (connection == ConnectionState.Available) {
                if (previousConnection == ConnectionState.Unavailable) {
                    heightAnim.animateTo(heightSize, tween(durationMillis = 500))
                    delay(2000)
                    heightAnim.animateTo(0f, tween(durationMillis = 500))
                }
            } else {
                heightAnim.animateTo(heightSize, tween(durationMillis = 500))
            }
            previousConnection = connection
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(heightAnim.value.dp)
            .background(if (isConnected) Green else Red)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            text = stringResource(id = if (isConnected) R.string.internet_connection else R.string.no_internet_connection)
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
internal fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}