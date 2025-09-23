package com.sproutjar.data.database

import androidx.room.TypeConverter
import com.sproutjar.data.models.PotIcon
import com.sproutjar.data.models.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromPotIcon(icon: PotIcon): String = icon.name

    @TypeConverter
    fun toPotIcon(value: String): PotIcon = PotIcon.valueOf(value)

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(value: Long): Date = Date(value)
}
