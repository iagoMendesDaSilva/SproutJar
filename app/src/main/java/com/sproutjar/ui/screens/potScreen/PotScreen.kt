package com.sproutjar.ui.screens.potScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sproutjar.MainActivityViewModel
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.CdiRate
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotButtonColor
import com.sproutjar.data.models.PotCategory
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.models.TransactionType
import com.sproutjar.ui.composables.PotButton
import com.sproutjar.ui.screens.potScreen.composables.PotDetailsHeader
import com.sproutjar.ui.screens.potScreen.composables.PotMenuItem
import com.sproutjar.ui.screens.potScreen.composables.TransactionBottomSheetContent
import com.sproutjar.ui.screens.potScreen.composables.TransactionsList
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.DevicePreviews
import com.sproutjar.utils.EarningService
import com.sproutjar.utils.Resource
import java.text.NumberFormat
import java.util.Date

@Composable
fun PotScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    pot: Pot?,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {
    val viewModel = hiltViewModel<MainActivityViewModel>()
    val potViewModel = hiltViewModel<PotScreenViewModel>()

    val cdiHistory by viewModel.cdiHistory.collectAsState()
    val transactions by potViewModel.transactions.collectAsState()

    LaunchedEffect(pot) {
        viewModel.fetchCdiHistory()
        pot?.let { potViewModel.fetchTransactions(it.id) }
    }

    PotScreenUI(
        pot = pot!!,
        transactions = transactions,
        cdiHistory,
        appSettings.currency,
        appSettings.theme,
        onNewTransaction = { viewModel.addTransaction(it); potViewModel.fetchTransactions(pot.id) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotScreenUI(
    pot: Pot,
    transactions: Resource<List<Transaction>>,
    cdiHistory: Resource<List<CdiRate>>,
    currency: Currency,
    theme: Theme,
    onNewTransaction: (Transaction) -> Unit
) {
    val currencyFormatter: NumberFormat = Currency.getCurrencyFormatter(currency)
    var selectedTransactionType by remember { mutableStateOf<TransactionType?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    var grossEarnings: Double = 0.0
    var netEarnings: Double = 0.0

    cdiHistory.data?.let { history ->
        transactions.data?.let {
            val result = EarningService.calculatePotEarnings(
                transactions = it,
                cdiHistory = history,
                cdiParticipation = pot.cdiPercent
            )

            grossEarnings = result.second
            netEarnings = result.third
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 12.dp,
                    end = 12.dp,
                    bottom = paddingValues.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PotDetailsHeader(
                pot,
                currencyFormatter,
                grossEarnings,
                netEarnings
            )
            Spacer(Modifier.height(32.dp))
            PotMenuItem(
                Icons.Outlined.SignalCellularAlt,
                R.string.income_history
            ) {
                Text(
                    text = "+${currencyFormatter.format(grossEarnings)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(16.dp))
            PotMenuItem(
                Icons.Outlined.Schedule,
                R.string.manage_deposit
            ) {
                Text(
                    text = stringResource(R.string.manage_deposit_desc),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(Modifier.height(24.dp))
            Box(modifier = Modifier.weight(1f)) {
                TransactionsList(
                    modifier = Modifier.fillMaxSize(),
                    transactions,
                    currencyFormatter,
                    theme
                )
            }
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PotButton(
                    R.string.deposit,
                    Modifier.weight(1f),
                    PotButtonColor.PRIMARY
                ) { selectedTransactionType = TransactionType.DEPOSIT }
                PotButton(
                    R.string.withdraw,
                    Modifier.weight(1f),
                    PotButtonColor.SECONDARY
                ) { selectedTransactionType = TransactionType.WITHDRAWAL }

            }
        }
        if (selectedTransactionType != null)
            ModalBottomSheet(
                onDismissRequest = { selectedTransactionType = null },
                sheetState = bottomSheetState
            ) {
                TransactionBottomSheetContent { description, amount ->
                    val transaction = Transaction(
                        potId = pot.id,
                        date = Date(),
                        description = description,
                        amount = amount,
                        type = selectedTransactionType!!
                    )
                    onNewTransaction(transaction)
                    selectedTransactionType = null
                }
            }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun PotPreview() {
    SproutJarTheme {
        Scaffold {
            PotScreenUI(
                Pot(1, PotCategory.Emergency, "Emergency", 1.0),
                Resource.Success(emptyList()),
                Resource.Success(emptyList()),
                Currency.USD,
                Theme.SPROUT_JAR,
            ) {}
        }
    }
}