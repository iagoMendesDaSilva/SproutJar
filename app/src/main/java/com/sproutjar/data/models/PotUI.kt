package com.sproutjar.data.models

data class PotUI(
    val pot: Pot,
    val transactions: List<Transaction> = emptyList()
) {
}
