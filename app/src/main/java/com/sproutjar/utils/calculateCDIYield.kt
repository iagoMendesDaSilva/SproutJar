package com.sproutjar.utils

import com.sproutjar.data.models.SelicTax

fun calculateCdiYield(
    principal: Double,
    rates: List<SelicTax>,
    cdiPercentage: Double
): Double {
    var amount = principal
    rates.forEach { rate ->
        val dailyRate = rate.value.replace(",", ".").toDoubleOrNull() ?: 0.0
        amount *= (1 + (dailyRate / 100.0) * cdiPercentage)
    }
    return amount
}
