package com.sproutjar.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
}

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = Pot::class,
        parentColumns = ["id"],
        childColumns = ["potId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("potId")]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val potId: Int,
    val amount: Double,
    val date: Date,
    val type: TransactionType
)
