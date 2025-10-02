package com.sproutjar.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sproutjar.R
import java.text.NumberFormat

enum class Currency(@StringRes val title: Int, val code: String, @DrawableRes val  image: Int) {
    USD(R.string.usd, "USD", R.drawable.usa_flag),
    BRL(R.string.brl, "BRL", R.drawable.brazil_flag),
    EUR(R.string.eur, "EUR", R.drawable.europe_flag);

    companion object {
        fun getCurrencyFormatter(currency: Currency): NumberFormat {
            return NumberFormat.getCurrencyInstance().apply {
                this.currency = java.util.Currency.getInstance(currency.code)
            }
        }
    }
}