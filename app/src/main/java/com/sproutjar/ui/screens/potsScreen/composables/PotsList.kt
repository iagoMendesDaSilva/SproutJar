package com.sproutjar.ui.screens.potsScreen.composables

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotStatement
import com.sproutjar.data.models.CdiRate
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.PotCategory
import com.sproutjar.data.models.asImageVector
import com.sproutjar.ui.theme.Red
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.EarningService
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import java.text.NumberFormat

@Composable
fun PotsList(
    pots: List<PotStatement>,
    searchText: String,
    currency: NumberFormat,
    cdiHistory: List<CdiRate>,
    onUpdate: ((pot: Pot) -> Unit)? = null,
    onDelete: ((pot: Pot) -> Unit)? = null,
) {

    val filteredPotsStatement = pots.filter { potStatement ->
        potStatement.pot.title.contains(
            searchText,
            ignoreCase = true
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(filteredPotsStatement) { potStatement ->
            PotCard(
                currency,
                potStatement,
                cdiHistory,
                onDelete = { pot -> onDelete?.invoke(pot) },
                onUpdate = { pot -> onUpdate?.invoke(pot) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PotCard(
    currency: NumberFormat,
    potStatement: PotStatement,
    cdiHistory: List<CdiRate>,
    onDelete: (Pot) -> Unit,
    onUpdate: (Pot) -> Unit,
) {
    val (balance, grossEarnings, netEarnings) =
        EarningService.calculatePotEarnings(
            transactions = potStatement.transactions,
            cdiHistory = cdiHistory,
            cdiParticipation = potStatement.pot.cdiPercent
        )

    RevealSwipe(
        modifier = Modifier.fillMaxWidth(),
        directions = setOf(RevealDirection.StartToEnd, RevealDirection.EndToStart),
        onBackgroundEndClick = {
            onDelete(potStatement.pot)
        },
        onBackgroundStartClick = {
            onUpdate(potStatement.pot)
        },
        hiddenContentStart = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    tint = Color.White,
                    contentDescription = stringResource(R.string.edit),
                    imageVector = Icons.Default.Edit,
                    modifier = Modifier.size(32.dp),
                )
            }
        },
        hiddenContentEnd = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Red)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    tint = Color.White,
                    contentDescription = stringResource(R.string.delete_pot),
                    imageVector = Icons.Default.Delete,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
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
                    imageVector = potStatement.pot.potCategory.asImageVector(),
                    contentDescription = potStatement.pot.title,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = potStatement.pot.title,
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
                modifier = Modifier.padding(end = 8.dp),
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
}

@Preview
@Composable
fun PotCardPreview() {
    SproutJarTheme {
        Surface {
            PotCard(
                Currency.getCurrencyFormatter(Currency.USD),
                potStatement = PotStatement(
                    Pot(
                        1,
                        PotCategory.Emergency,
                        stringResource(R.string.app_name),
                        1.0
                    )
                ),
                cdiHistory = emptyList(),
                onUpdate = {},
                onDelete = { }
            )
        }
    }
}