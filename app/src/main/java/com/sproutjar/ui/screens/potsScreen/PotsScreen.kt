package com.sproutjar.ui.screens.potsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sproutjar.MainActivityViewModel
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.PotUI
import com.sproutjar.data.models.SelicTax
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.asImageVector
import com.sproutjar.ui.composables.LogoLabel
import com.sproutjar.ui.composables.SearchHeader
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.DevicePreviews
import com.sproutjar.utils.EarningService
import com.sproutjar.utils.Resource
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PotsScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {

    val scope = rememberCoroutineScope()
    val viewModel = hiltViewModel<MainActivityViewModel>()
    val pots by viewModel.pots.collectAsState()
    val cdiHistory by viewModel.cdiHistory.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchPots()
        viewModel.fetchCdiHistory()
    }


    PotsScreenUI(appSettings.theme, pots, cdiHistory)
}

@Composable
fun PotsScreenUI(
    theme: Theme,
    pots: List<PotUI>,
    cdiHistory: Resource<List<SelicTax>>,
) {
    var navBarHeightPx by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    navBarHeightPx = coordinates.size.height
                }) {}
        }
    ) { paddingValues ->
        val navBarHeightDp = with(LocalDensity.current) { navBarHeightPx.toDp() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), start = 12.dp, end = 12.dp),

            ) {
            SearchHeader(R.string.pots) { searchText = it }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp), contentAlignment = Alignment.Center
            ) {
                when (cdiHistory) {
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Resource.Error ->
                        LogoLabel(
                            theme,
                            MessageDialog(R.string.checking_server, R.string.try_again)
                        )

                    is Resource.Success ->
                        if (pots.none { it.pot.title.contains(searchText, ignoreCase = true) })
                            LogoLabel(
                                theme,
                                MessageDialog(R.string.not_found_pots, R.string.add_first_pot)
                            )
                        else
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                items(pots) { pot ->
                                    PotCard(pot, cdiHistory)
                                }
                            }
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = navBarHeightDp + 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add)
                )
            }
        }
    }
}

@Composable
fun PotCard(potUI: PotUI, cdiHistory: Resource<List<SelicTax>>) {
    val currency = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    val (balance, grossEarnings, netEarnings) =
        EarningService.calculatePotEarnings(
            transactions = potUI.transactions,
            cdiHistory = cdiHistory.data!!,
            cdiParticipation = potUI.pot.cdiPercent
        )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = potUI.pot.icon.asImageVector(),
                contentDescription = potUI.pot.title,
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = potUI.pot.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = currency.format(balance),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alpha(.5f)
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = currency.format(grossEarnings),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = currency.format(netEarnings),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alpha(.5f)
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun PotsPreview() {
    SproutJarTheme {
        Scaffold {
            PotsScreenUI(Theme.SPROUT_JAR, emptyList(), Resource.Error())
        }
    }
}