package com.sproutjar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.Transaction

@Database(
    entities = [Pot::class, Transaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun potDao(): PotDao
    abstract fun transactionDao(): TransactionDao
}