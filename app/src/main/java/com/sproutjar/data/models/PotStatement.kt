package com.sproutjar.data.models

data class PotStatement(
    val pot: Pot,
    val transactions: List<Transaction> = emptyList()
)
