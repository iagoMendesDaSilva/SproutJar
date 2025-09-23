package com.sproutjar.utils

import com.sproutjar.data.models.SelicTax
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.models.TransactionType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.round

object EarningService {
    private val iofTable = doubleArrayOf(
        0.96, 0.9333, 0.9066, 0.88, 0.8533, 0.8266, 0.8,
        0.7733, 0.7466, 0.72, 0.6933, 0.6666, 0.64, 0.6133,
        0.5866, 0.56, 0.5333, 0.5066, 0.48, 0.4533, 0.4266,
        0.4, 0.3733, 0.3466, 0.32, 0.2933, 0.2666, 0.24, 0.2133, 0.0
    )

    private fun getIrRate(daysHeld: Long): Double {
        return when {
            daysHeld <= 180 -> 0.225
            daysHeld <= 360 -> 0.20
            daysHeld <= 720 -> 0.175
            else -> 0.15
        }
    }

    private fun getIofRate(daysHeld: Long): Double {
        return if (daysHeld in 1..30) iofTable[daysHeld.toInt() - 1] else 0.0
    }

    private fun daysBetween(start: Date, end: Date): Long {
        val diff = end.time - start.time
        return diff / (1000L * 60 * 60 * 24)
    }

    fun calculatePotEarnings(
        transactions: List<Transaction>,
        cdiHistory: List<SelicTax>,
        cdiParticipation: Double
    ): Triple<Double, Double, Double> {

        val cdiInternalMultiplier = 0.6517
        val businessDays = 252.0

        if (transactions.isEmpty() || cdiHistory.isEmpty()) return Triple(0.0, 0.0, 0.0)

        val sdf = SimpleDateFormat(DateFormatPattern.SELIC_DATE_HISTORIC.pattern, Locale.getDefault())
        val cdiMap = cdiHistory.mapNotNull { tax ->
            try {
                val d = sdf.parse(tax.date)
                val v = tax.value.replace(',', '.').toDouble()
                d to v / 100.0
            } catch (e: Exception) {
                null
            }
        }.toMap()

        val cdiDates = cdiMap.keys.sorted()
        if (cdiDates.isEmpty()) return Triple(0.0, 0.0, 0.0)
        val lastCdiDate = cdiDates.last()

        val principal = transactions.fold(0.0) { acc, tx ->
            when (tx.type) {
                TransactionType.DEPOSIT -> acc + tx.amount
                TransactionType.WITHDRAWAL -> acc - tx.amount
            }
        }

        var grossEarnings = 0.0
        for (tx in transactions) {
            if (tx.type != TransactionType.DEPOSIT) continue

            val applicableDates = cdiDates.filter { it.after(tx.date) && !it.after(lastCdiDate) }
            if (applicableDates.isEmpty()) continue

            for (d in applicableDates) {
                val annualRate = cdiMap[d] ?: continue
                val dailyRate = (annualRate * (cdiParticipation * cdiInternalMultiplier)) / businessDays
                grossEarnings += tx.amount * dailyRate
            }
        }

        val grossRounded = round(grossEarnings * 100) / 100.0

        val oldestTxDate = transactions.minByOrNull { it.date }?.date ?: Date()
        val daysHeld = daysBetween(oldestTxDate, lastCdiDate)

        val iofMultiplier = getIofRate(daysHeld)
        val iofAmount = if (daysHeld in 1..30) grossRounded * (1.0 - iofMultiplier) else 0.0

        val irRate = getIrRate(daysHeld)
        val irAmount = grossRounded * irRate

        val netEarnings = grossRounded - iofAmount - irAmount
        val balance = principal + netEarnings

        fun round2(v: Double) = (round(v * 100) / 100.0)

        return Triple(round2(balance), round2(grossRounded), round2(netEarnings))
    }

    private fun isSameDay(a: Date, b: Date): Boolean {
        val ca = Calendar.getInstance().apply { time = a }
        val cb = Calendar.getInstance().apply { time = b }
        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) &&
                ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR)
    }
}
