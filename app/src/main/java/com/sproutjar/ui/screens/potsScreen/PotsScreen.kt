package com.sproutjar.ui.screens.potsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sproutjar.MainActivityViewModel
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotCategory
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
import kotlinx.coroutines.launch
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


    PotsScreenUI(appSettings.theme, pots, cdiHistory) {
        scope.launch {
            viewModel.insertPot(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotsScreenUI(
    theme: Theme,
    pots: List<PotUI>,
    cdiHistory: Resource<List<SelicTax>>,
    insertPot: (pot: Pot) -> Unit,
) {
    var navBarHeightPx by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }

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
                    .fillMaxSize(), contentAlignment = Alignment.Center
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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp),
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
                    showBottomSheet = true
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
        if (showBottomSheet)
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                NewPotBottomSheetContent {
                    insertPot(it)
                    showBottomSheet = false
                }
            }
    }
}

@Composable
internal fun NewPotBottomSheetContent(addPot: (Pot) -> Unit) {

    var category by remember { mutableStateOf(PotCategory.Emergency) }
    var title by remember { mutableStateOf("") }
    var cdiRate by remember { mutableStateOf(0) }
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
                            addPot(
                                Pot(
                                    icon = category,
                                    title = title,
                                    cdiPercent = (cdiRate / 100).toDouble()
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
            Text(
                modifier = Modifier.alpha(.5f),
                text = stringResource(R.string.cdi_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-12).dp),
                value = if (cdiRate > 0) "$cdiRate%" else "",
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() }
                    cdiRate = digitsOnly.toIntOrNull()?.coerceIn(0, 999) ?: 0
                },
                placeholder = {
                    Text(
                        stringResource(R.string.cdi_placeholder_label),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                textStyle = MaterialTheme.typography.headlineMedium,
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
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            text = stringResource(R.string.category),
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(modifier = Modifier.padding(16.dp)) {
            items(PotCategory.entries) { potCategory ->
                val isSelected = category == potCategory
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
                            category = potCategory
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
}

@Composable
internal fun PotCard(potUI: PotUI, cdiHistory: Resource<List<SelicTax>>) {
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
            PotsScreenUI(Theme.SPROUT_JAR, emptyList(), Resource.Error()) {}
        }
    }
}