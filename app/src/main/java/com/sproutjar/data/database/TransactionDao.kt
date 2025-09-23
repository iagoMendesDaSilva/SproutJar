package com.sproutjar.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sproutjar.data.models.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE potId = :potId ORDER BY date ASC")
    suspend fun getTransactionsForPot(potId: Int): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}
