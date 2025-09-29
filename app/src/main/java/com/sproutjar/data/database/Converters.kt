package com.sproutjar.data.database

import androidx.room.TypeConverter
import com.sproutjar.data.models.PotCategory
import com.sproutjar.data.models.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromPotIcon(icon: PotCategory): String = icon.name

    @TypeConverter
    fun toPotIcon(value: String): PotCategory = PotCategory.valueOf(value)

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(value: Long): Date = Date(value)
}
