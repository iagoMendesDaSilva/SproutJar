package com.sproutjar.ui.screens.potScreen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.MessageInfo
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.models.TransactionType
import com.sproutjar.ui.composables.LogoLabel
import com.sproutjar.ui.theme.Red
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.DateFormatPattern
import com.sproutjar.utils.DateService
import com.sproutjar.utils.Resource
import java.text.NumberFormat
import java.util.Date

private fun groupTransactionsByDay(transactions: List<Transaction>): Map<String, List<Transaction>> {
    return transactions.groupBy {
        DateService.formatDate(it.date, DateFormatPattern.DAY_MONTH)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    transactions: Resource<List<Transaction>>,
    currency: NumberFormat,
    theme: Theme
) {

    Column {
        Text(
            text = stringResource(R.string.transactions),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {

                transactions.data?.let {
                    val transactionsByDay = groupTransactionsByDay(transactions.data)
                    transactionsByDay.forEach { (day, dayTransactions) ->
                        stickyHeader {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .alpha(.5f)
                                )
                            }
                        }

                        items(dayTransactions) { transaction ->
                            TransactionCard(transaction, currency)
                        }
                    }
                }
                if (transactions is Resource.Loading)
                    item {
                        CircularProgressIndicator()
                    }
                if (transactions is Resource.Success && transactions.data.isNullOrEmpty())
                    item {
                        LogoLabel(
                            theme,
                            MessageInfo(
                                R.string.not_found_transactions,
                                R.string.add_first_transaction
                            )
                        )
                    }

            }
        )
    }
}

@Composable
internal fun TransactionCard(transaction: Transaction, currency: NumberFormat) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = DateService.formatDate(transaction.date, DateFormatPattern.HOUR_MINUTE),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alpha(.5f)
            )
        }
        Text(
            text = (if (transaction.type == TransactionType.DEPOSIT) stringResource(R.string.plus) else stringResource(
                R.string.minus
            )) +
                    currency.format(transaction.amount),
            style = MaterialTheme.typography.bodyMedium,
            color = if (transaction.type == TransactionType.DEPOSIT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
fun TransactionsListPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TransactionsList(
                transactions = Resource.Success(
                    listOf(
                        Transaction(
                            1,
                            1,
                            100.0,
                            Date(),
                            stringResource(R.string.app_name),
                            TransactionType.DEPOSIT
                        ),
                        Transaction(
                            1,
                            1,
                            200.0,
                            Date(),
                            stringResource(R.string.app_name),
                            TransactionType.WITHDRAWAL
                        )
                    )
                ),
                currency = Currency.getCurrencyFormatter(Currency.USD), theme = Theme.SPROUT_JAR
            )
        }
    }
}