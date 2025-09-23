package com.sproutjar.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


enum class DateFormatPattern(val pattern: String) {
    SELIC_DATE_HISTORIC("dd/MM/yyyy"),
}


object DateService {
    fun formatDate(date: Date, format: DateFormatPattern): String {
        val dateFormat = SimpleDateFormat(format.pattern, Locale.getDefault())
        return dateFormat.format(date)
    }
}