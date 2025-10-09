package com.sproutjar.ui.screens.potScreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotCategory
import com.sproutjar.ui.theme.SproutJarTheme
import java.text.NumberFormat

@Composable
fun PotDetailsHeader(
    pot: Pot,
    currency: NumberFormat,
    grossEarnings: Double,
    netEarnings: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = pot.title, style = MaterialTheme.typography.headlineMedium
        )
        Image(
            painterResource(R.drawable.pig_jar),
            contentDescription = pot.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(.5f)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = currency.format(grossEarnings),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                modifier = Modifier.alpha(.5f),
                text = currency.format(netEarnings),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
fun PotDetailsHeaderPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background){
            PotDetailsHeader(
                Pot(
                    1,
                    PotCategory.Emergency,
                    stringResource(R.string.app_name),
                    1.0
                ),
                currency = Currency.getCurrencyFormatter(Currency.USD),
                grossEarnings = 0.0,
                netEarnings = 0.0,
            )
        }
    }
}