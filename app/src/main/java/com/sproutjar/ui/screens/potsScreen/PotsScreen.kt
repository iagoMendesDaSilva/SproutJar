package com.sproutjar.ui.screens.potsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sproutjar.MainActivityViewModel
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.CdiRate
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotStatement
import com.sproutjar.data.models.Theme
import com.sproutjar.ui.composables.LogoLabel
import com.sproutjar.ui.composables.SearchHeader
import com.sproutjar.ui.screens.potsScreen.composables.PotBottomSheetContent
import com.sproutjar.ui.screens.potsScreen.composables.PotsList
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.DevicePreviews
import com.sproutjar.utils.Resource

@Composable
fun PotsScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {

    val viewModel = hiltViewModel<MainActivityViewModel>()
    val pots by viewModel.pots.collectAsState()
    val cdiHistory by viewModel.cdiHistory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPots()
        viewModel.fetchCdiHistory()
    }

    PotsScreenUI(
        appSettings.theme,
        appSettings.currency,
        pots, cdiHistory,
        insertPot = { viewModel.insertPot(it) },
        updatePot = { viewModel.updatePot(it) },
        deletePot = {
            showGlobalDialog(
                GlobalDialogState(
                    dialogInfo = DialogInfo(
                        MessageDialog(
                            R.string.delete_pot,
                            R.string.delete_pot_desc
                        )
                    ),
                    onSuccess = { viewModel.deletePot(it) }
                ))
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotsScreenUI(
    theme: Theme,
    currency: Currency,
    pots: List<PotStatement>,
    cdiHistory: Resource<List<CdiRate>>,
    insertPot: (pot: Pot) -> Unit,
    updatePot: (pot: Pot) -> Unit,
    deletePot: (pot: Pot) -> Unit,

    ) {
    val currencyFormatter = Currency.getCurrencyFormatter(currency)

    var navBarHeightPx by remember { mutableIntStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }
    var potToEdit by remember { mutableStateOf<Pot?>(null) }

    fun closeBottomSheet() {
        showBottomSheet = false
        potToEdit = null
    }

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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (cdiHistory) {
                    is Resource.Loading -> CircularProgressIndicator()
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
                            PotsList(
                                pots,
                                searchText,
                                currencyFormatter,
                                cdiHistory.data!!,
                                onDelete = { deletePot(it) },
                                onUpdate = { potToEdit = it; showBottomSheet = true }
                            )
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
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

        if (showBottomSheet)
            ModalBottomSheet(
                onDismissRequest = { closeBottomSheet() },
                sheetState = bottomSheetState
            ) {
                PotBottomSheetContent(potToEdit = potToEdit) {
                    if (potToEdit == null)
                        insertPot(it)
                    else
                        updatePot(it)
                    closeBottomSheet()
                }
            }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun PotsPreview() {
    SproutJarTheme {
        Scaffold {
            PotsScreenUI(
                Theme.SPROUT_JAR,
                Currency.USD,
                emptyList(),
                Resource.Error(),
                insertPot = {},
                updatePot = {}
            ) {}
        }
    }
}