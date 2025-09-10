package com.sproutjar.data.models

import androidx.annotation.StringRes
import com.sproutjar.R
import com.sproutjar.data.models.Languages.ENGLISH
import com.sproutjar.data.models.Languages.PORTUGUESE


enum class Currency(@StringRes val title: Int) {
    USD(R.string.usd),
    BRL(R.string.brl),
    EUR(R.string.eur);

    companion object{
        fun flagCurrency(currency: Currency): Int {
            return when (currency) {
                Currency.USD -> R.drawable.usa_flag
                Currency.BRL -> R.drawable.brazil_flag
                Currency.EUR -> R.drawable.europe_flag
            }
        }
    }
}