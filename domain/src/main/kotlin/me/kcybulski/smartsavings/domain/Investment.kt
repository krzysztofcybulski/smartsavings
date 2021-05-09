package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.Period

class Investment(
    val coin: Coin,
    val assets: List<Asset>,
    private val started: LocalDate,
    private val frequency: Frequency
) {

    fun getDailyInvestments(to: LocalDate): List<DailyInvestment> = frequency
        .buyingDaysBetween(started, to)
        .map { DailyInvestment(it, assets) }

    fun getDays(until: LocalDate): List<LocalDate> = frequency.buyingDaysBetween(started, until)

    fun totalInvestment(to: LocalDate): BigDecimal =
        assets.map { it.investment }.fold(ZERO) { a, b -> a + b } * (Period.between(started, to).days + 1).toBigDecimal()

}

data class DailyInvestment(
    val day: LocalDate,
    val assets: List<Asset>
)

data class Asset(val crypto: Coin, val investment: BigDecimal)
