package me.kcybulski.smartsavings.domain

import java.time.LocalDate
import java.time.Period

class Investment(
    private val started: LocalDate,
    private val frequency: Frequency,
    private val assets: List<Asset>
) {

    fun getDailyInvestments(to: LocalDate): List<DailyInvestment> = frequency
        .buyingDaysBetween(started, to)
        .map { DailyInvestment(it, assets) }

    fun totalInvestment(to: LocalDate): Money =
        assets.map { it.money }.reduce { acc, asset -> acc + asset } * (Period.between(started, to).days + 1)

}

data class DailyInvestment(
    val day: LocalDate,
    val assets: List<Asset>
)

data class Asset internal constructor(val crypto: Cryptocurrency, val money: Money)
